package com.example.cab302a1.ui.page.review.student;

import com.example.cab302a1.dao.DaoFactory;
import com.example.cab302a1.dao.QuestionDao;
import com.example.cab302a1.dao.ReviewDao;
import com.example.cab302a1.model.QuizReview;
import com.example.cab302a1.model.User;
import com.example.cab302a1.result.QuizResultController;
import com.example.cab302a1.result.QuizResultService;
import com.example.cab302a1.ui.page.review.ReviewPageController;
import com.example.cab302a1.util.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Student Review Page.
 * Displays all quiz attempts and feedback for the currently logged-in student
 * in a tabular format, providing options to view results and feedback.
 *
 * @author Mitchell
 * @version 1.0
 */
public class StudentReviewController implements Initializable, ReviewPageController {

    /**
     * The TableView displaying all quiz attempts.
     */
    @FXML public TableView<QuizReview> studentQuizTable;
    /**
     * Column for displaying the name of the quiz.
     */
    @FXML public TableColumn<QuizReview, String> quizNameCol;
    /**
     * Column for displaying the score summary (e.g., "15/20").
     */
    @FXML public TableColumn<QuizReview, String> scoreCol;
    /**
     * Column containing a button to view teacher feedback.
     */
    @FXML public TableColumn<QuizReview, Void> feedbackCol;
    /**
     * Column containing a button to navigate to the detailed quiz result page.
     */
    @FXML public TableColumn<QuizReview, Void> resultCol;

    // Sidebar Buttons - Assume FXML linked
    @FXML public Button dashboardBtn;
    @FXML public Button reviewBtn;
    @FXML public Button timetableBtn;
    @FXML public Button exitBtn;

    private final ObservableList<QuizReview> reviewData = FXCollections.observableArrayList();
    private final QuestionDao questionDao = DaoFactory.getQuestionDao();
    /**
     * Data Access Object for managing quiz review data.
     */
    private final ReviewDao reviewDao = DaoFactory.getReviewDao();

    private Stage stage;

    /**
     * Called to initialize a controller after its root element has been completely processed.
     * This method sets up the table columns and loads the review data for the current student.
     *
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns(); // Configure columns and buttons
        loadReviewData(); // Load data immediately upon entering the scene
    }

    /**
     * Configures the TableView columns, setting up cell value factories and delegating
     * button column creation to helper methods.
     */
    private void setupTableColumns() {
        if (studentQuizTable == null) return;

        studentQuizTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // Standard column setup
        quizNameCol.setCellValueFactory(data -> data.getValue().quizNameProperty());
        scoreCol.setCellValueFactory(data -> data.getValue().scoreSummaryProperty());

        // Delegate complex button logic to dedicated methods
        setupFeedbackColumn();
        setupResultColumn();
    }


    /**
     * Sets up the cell factory for the feedbackCol.
     * Each cell contains a button that, when clicked, opens an {@code Alert} dialog
     * displaying the teacher's feedback (if available) for that quiz attempt.
     */
    private void setupFeedbackColumn() {
        feedbackCol.setText("View Feedback");
        feedbackCol.setCellFactory(col -> new TableCell<QuizReview, Void>() {
            private final Button btn = new Button("View Feedback");

            {
                btn.getStyleClass().add("action-button");

                btn.setOnAction(e -> {
                    QuizReview item = getTableView().getItems().get(getIndex());
                    String feedback = item.getFeedback();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Teacher Feedback for " + item.getQuizName());
                    // Logic to display feedback status
                    alert.setHeaderText("Feedback Status: " + (feedback != null && !feedback.trim().isEmpty() ? "Reviewed" : "Pending Review"));
                    alert.setContentText(feedback != null && !feedback.trim().isEmpty()
                            ? feedback : "No specific feedback has been assigned by the teacher yet.");

                    alert.showAndWait();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn); // Display button only if row is not empty
            }
        });
    }


    /**
     * Sets up the cell factory for the resultCol.
     * Each cell contains a button that, when clicked, navigates the user to the
     * detailed result page for the corresponding quiz attempt.
     */
    private void setupResultColumn() {
        resultCol.setText("View Result");
        resultCol.setCellFactory(col -> new TableCell<QuizReview, Void>() {
            private final Button btn = new Button("View Result");

            {
                btn.getStyleClass().add("action-button");

                btn.setOnAction(e -> {
                    QuizReview item = getTableView().getItems().get(getIndex());

                    try {
                        int quizId = item.getQuizId();

                        if (quizId <= 0) {
                            throw new IllegalArgumentException("Invalid Quiz ID for result viewing. Data integrity issue.");
                        }

                        Stage stage = (Stage) btn.getScene().getWindow();
                        QuizResultController.showQuizResultFromDatabaseForCurrentUser(stage, quizId);

                    } catch (QuizResultService.QuizResultException | IOException ex) {
                        System.err.println("Error viewing quiz result: " + ex.getMessage());
                        ex.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR,
                                "Could not load quiz result. Error: " + ex.getMessage());
                        alert.showAndWait();
                    } catch (IllegalArgumentException ex) {
                        Alert alert = new Alert(Alert.AlertType.WARNING, ex.getMessage());
                        alert.showAndWait();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    /**
     * Sets the primary stage for the controller. Required by the {@code ReviewPageController} interface.
     *
     * @param stage The primary stage of the application.
     */
    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Loads quiz attempt data for the currently logged-in student from the database
     * and populates the {@code studentQuizTable}.
     * Clears the table and sets a placeholder if no attempts are found.
     */
    @Override
    public void loadReviewData() {
        reviewData.clear();

        User currentUser = Session.getCurrentUser();

        if (currentUser != null) {
            int studentId = currentUser.getUser_id();

            try {
                reviewData.addAll(reviewDao.getAllAttemptsById(studentId));
                System.out.println("Student ID " + studentId + " loaded " + reviewData.size() + " attempts.");
            } catch (Exception e) {
                System.err.println("Error fetching quiz attempts for current user: " + e.getMessage());
            }
        } else {
            System.err.println("Load data failed: No current user found in Session.");
        }

        if (reviewData.isEmpty() && studentQuizTable != null) {
            studentQuizTable.setPlaceholder(new Label("You have not completed any quizzes yet."));
        }

        if (studentQuizTable != null) {
            studentQuizTable.setItems(reviewData);
        }
    }
}
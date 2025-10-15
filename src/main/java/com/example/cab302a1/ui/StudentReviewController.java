package com.example.cab302a1.ui;

import com.example.cab302a1.dao.QuestionDao;
import com.example.cab302a1.dao.jdbc.JdbcQuestionDao;
import com.example.cab302a1.dao.jdbc.JdbcReviewDao;
import com.example.cab302a1.dao.ReviewDao;
import com.example.cab302a1.model.QuizReview;
import com.example.cab302a1.model.User;
import com.example.cab302a1.result.QuizResultController;
import com.example.cab302a1.result.QuizResultService;
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
 * Displays all quiz attempts and feedback for the currently logged-in student.
 */
public class StudentReviewController implements Initializable, ReviewPageController {

    // FXML Fields (Existing)
    @FXML public TableView<QuizReview> studentQuizTable;
    @FXML public TableColumn<QuizReview, String> quizNameCol;
    @FXML public TableColumn<QuizReview, String> scoreCol;
    @FXML public TableColumn<QuizReview, Void> feedbackCol; // Button column for feedback
    @FXML public TableColumn<QuizReview, Void> resultCol; // Button column for results

    // Sidebar Buttons (Only commented for clarity, assume linked in FXML)
    @FXML public Button dashboardBtn;
    @FXML public Button reviewBtn;
    @FXML public Button timetableBtn;
    @FXML public Button exitBtn;

    private final ObservableList<QuizReview> reviewData = FXCollections.observableArrayList();
    private final QuestionDao questionDao = new JdbcQuestionDao();
    private final ReviewDao reviewDao = new JdbcReviewDao(questionDao); // DAO dependency injection

    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns(); // Configure columns and buttons
        loadReviewData(); // Load data immediately upon entering the scene
    }

    /**
     * Configures the TableView columns, delegating button creation to helper methods.
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


    // REFACTORED METHOD 1: Setup Feedback Button Column
    private void setupFeedbackColumn() {
        feedbackCol.setText("View Feedback");
        feedbackCol.setCellFactory(col -> new TableCell<QuizReview, Void>() {
            private final Button btn = new Button("View Feedback"); // Create button instance

            {
                btn.getStyleClass().add("action-button"); // Apply common CSS style

                btn.setOnAction(e -> {
                    QuizReview item = getTableView().getItems().get(getIndex()); // Get data for the clicked row
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


    // REFACTORED METHOD 2: Setup Result Button Column
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
                            // Check for invalid data before navigation
                            throw new IllegalArgumentException("Invalid Quiz ID for result viewing. Data integrity issue.");
                        }

                        Stage stage = (Stage) btn.getScene().getWindow(); // Get current stage
                        // Navigate to the detailed quiz result page
                        QuizResultController.showQuizResultFromDatabaseForCurrentUser(stage, quizId);

                    } catch (QuizResultService.QuizResultException | IOException ex) {
                        // Catch exceptions related to result loading or scene change
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

    @Override
    public void setStage(Stage stage) {
        // Required by ReviewPageController interface, implementation may be blank
    }

    /**
     * Loads quiz attempt data for the currently logged-in student.
     */
    @Override
    public void loadReviewData() {
        reviewData.clear();

        // Get the ID of the logged-in user from Session
        User currentUser = Session.getCurrentUser();

        if (currentUser != null) {
            int studentId = currentUser.getUser_id();

            try {
                // Load quizzes specifically for the logged-in student ID
                reviewData.addAll(reviewDao.getAllAttemptsById(studentId));
                System.out.println("Student ID " + studentId + " loaded " + reviewData.size() + " attempts.");
            } catch (Exception e) {
                System.err.println("Error fetching quiz attempts for current user: " + e.getMessage());
            }
        } else {
            System.err.println("Load data failed: No current user found in Session.");
        }

        // Set placeholder if the data list is empty
        if (reviewData.isEmpty() && studentQuizTable != null) {
            studentQuizTable.setPlaceholder(new Label("You have not completed any quizzes yet."));
        }

        if (studentQuizTable != null) {
            studentQuizTable.setItems(reviewData);
        }
    }
}
package com.example.cab302a1.ui;

import com.example.cab302a1.dao.ReviewDao;
import com.example.cab302a1.dao.AttemptDao;
import com.example.cab302a1.dao.UserDao;
import com.example.cab302a1.dao.DaoFactory;
import com.example.cab302a1.model.QuizReview;
import com.example.cab302a1.model.Student;
import com.example.cab302a1.model.User;
import com.example.cab302a1.result.QuizResultController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Teacher Review Page.
 * Displays a list of all students and allows the teacher to select a student
 * to view their quiz attempts and assign feedback.
 *
 * @author Mitchell
 * @version 1.0
 */
public class TeacherReviewController implements Initializable, ReviewPageController {

    /**
     * The TableView displaying the quiz attempts for the currently selected student.
     */
    @FXML public TableView<QuizReview> quizTable;
    /**
     * Column that displays the quiz name and acts as a button to view the quiz definition.
     */
    @FXML public TableColumn<QuizReview, String> quizNameCol;
    /**
     * Column for displaying the score summary (e.g., "15/20").
     */
    @FXML public TableColumn<QuizReview, String> scoreCol;
    /**
     * Column containing a button to navigate to the detailed quiz result page.
     */
    @FXML public TableColumn<QuizReview, Void> resultCol;
    /**
     * Button to trigger the dialog for assigning or updating feedback on a selected quiz attempt.
     */
    @FXML public Button assignReviewBtn;

    /**
     * VBox container in the sidebar used to display the list of student buttons.
     */
    @FXML public VBox studentListContainer;
    /**
     * Label displaying the name of the currently selected student.
     */
    @FXML public Label studentNameLabel;

    private final ObservableList<QuizReview> reviewData = FXCollections.observableArrayList();
    private Stage stage;

    /**
     * Data Access Object for fetching quiz review data.
     */
    private final ReviewDao reviewDao = DaoFactory.getReviewDao();
    /**
     * Data Access Object for updating the feedback field in quiz attempts.
     */
    private final AttemptDao attemptDao = DaoFactory.getAttemptDao();
    /**
     * Data Access Object for fetching student user data.
     */
    private final UserDao userDao = DaoFactory.getUserDao();

    /**
     * Tracks the ID of the student whose quizzes are currently loaded and displayed.
     */
    private int currentSelectedStudentId = -1;

    /**
     * Called to initialize a controller after its root element has been completely processed.
     * This method sets up the table, action buttons, and loads the student list.
     *
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        setupActionButtons();
        loadStudentList(); // Load students dynamically into sidebar

        studentNameLabel.setText("Student Name: (Please select a student)");
        quizTable.setItems(reviewData);
    }

    /**
     * Loads the list of all users with the role 'Student' from the database
     * and populates the {@code studentListContainer} sidebar with clickable buttons.
     */
    private void loadStudentList() {
        if (studentListContainer == null) return;
        studentListContainer.getChildren().clear();

        List<Student> students = userDao.getAllStudents();

        for (User user : students) {
            if (!(user instanceof Student student)) continue;

            Button studentBtn = new Button(student.getUsername());

            studentBtn.getStyleClass().add("action-button");
            studentBtn.setMaxWidth(Double.MAX_VALUE);

            studentBtn.setOnAction(e -> {
                System.out.println("Teacher selected student: " + student.getUsername() + " (ID: " + student.getUser_id() + ")");
                studentNameLabel.setText("Student Name: " + student.getUsername());
                currentSelectedStudentId = student.getUser_id();
                loadReviewData(); // Reloads table with quizzes for this student
            });

            studentListContainer.getChildren().add(studentBtn);
        }
    }

    /**
     * Sets up the action for the {@code assignReviewBtn}.
     * When clicked, it launches a {@code TextInputDialog} to get feedback from the teacher
     * and then calls the DAO to persist the feedback to the database for the selected quiz attempt.
     */
    private void setupActionButtons() {
        if (assignReviewBtn != null) {
            assignReviewBtn.setOnAction(e -> {
                QuizReview selectedQuiz = quizTable.getSelectionModel().getSelectedItem();

                if (selectedQuiz == null || currentSelectedStudentId == -1) {
                    Alert alert = new Alert(Alert.AlertType.WARNING,
                            "Please select a quiz attempt from the table to assign a review.");
                    alert.showAndWait();
                    return;
                }

                // 1. Launch Input Dialog to get feedback
                TextInputDialog dialog = new TextInputDialog(selectedQuiz.getFeedback() != null ? selectedQuiz.getFeedback() : "");
                dialog.setTitle("Assign Review and Feedback");
                dialog.setHeaderText("Provide feedback for: " + selectedQuiz.getQuizName());

                dialog.showAndWait().ifPresent(feedbackText -> {

                    // 2. Call DAO to update the database
                    int attemptId = selectedQuiz.getAttemptId();

                    try {
                        attemptDao.updateFeedback(attemptId, feedbackText);

                        // 3. Update the UI and inform the user
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Feedback successfully assigned.");
                        successAlert.showAndWait();

                        loadReviewData(); // Reload data to show updated feedback
                    } catch (Exception ex) {
                        System.err.println("Failed to assign feedback: " + ex.getMessage());
                        ex.printStackTrace();
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                                "Failed to assign feedback. Error: " + ex.getMessage());
                        errorAlert.showAndWait();
                    }
                });
            });
        }
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
     * Loads quiz attempt data from the database for the currently selected student (via {@code currentSelectedStudentId})
     * and populates the {@code quizTable}.
     * Clears the table and adds a placeholder item if no student is selected or no attempts are found.
     */
    @Override
    public void loadReviewData() {
        reviewData.clear();

        if (currentSelectedStudentId != -1) {
            try {
                reviewData.addAll(reviewDao.getAllAttemptsById(currentSelectedStudentId));
            } catch (Exception e) {
                System.err.println("Error fetching quiz attempts for student ID " + currentSelectedStudentId + ": " + e.getMessage());
            }
        }

        if (reviewData.isEmpty()) {
            // Add a placeholder item if no attempts are found (for usability)
            reviewData.add(new QuizReview("No attempts available", 0, 0, "Select a student from the list.", -1));
        }

        if (quizTable != null) {
            quizTable.setItems(reviewData);
        }
    }

    /**
     * Configures the TableView columns, setting up cell value factories and delegating
     * button column creation to helper methods.
     */
    private void setupTableColumns() {
        if (quizTable == null) return;

        quizTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        if (scoreCol != null) {
            scoreCol.setCellValueFactory(data -> data.getValue().scoreSummaryProperty());
        }

        // Delegate complex button logic
        setupQuizNameColumnAsButton();
        setupResultColumnAsButton();
    }

    /**
     * Sets up the cell factory for the {@code quizNameCol}.
     * The quiz name is displayed inside a button which, when clicked, allows the teacher
     * to perform an action related to the quiz definition (e.g., viewing the questions).
     */
    private void setupQuizNameColumnAsButton() {
        quizNameCol.setCellValueFactory(data -> data.getValue().quizNameProperty());

        quizNameCol.setCellFactory(col -> new TableCell<QuizReview, String>() {
            private final Button btn = new Button();
            {
                btn.getStyleClass().add("action-button");

                btn.setOnAction(e -> {
                    QuizReview item = getTableView().getItems().get(getIndex());
                    System.out.println("Viewing Quiz Attempt ID: " + item.getAttemptId());
                    // NOTE: Navigation logic would be added here to view the quiz definition
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    btn.setText(item);
                    setGraphic(btn);
                }
            }
        });
    }

    /**
     * Sets up the cell factory for the {@code resultCol}.
     * Each cell contains a button that, when clicked, navigates the teacher to the
     * detailed result page for the corresponding quiz attempt of the selected student.
     */
    private void setupResultColumnAsButton() {
        if (resultCol == null) return;

        resultCol.setCellFactory(col -> new TableCell<QuizReview, Void>() {
            private final Button btn = new Button("View Result");

            {
                btn.getStyleClass().add("action-button");

                btn.setOnAction(e -> {
                    QuizReview item = getTableView().getItems().get(getIndex());

                    try {
                        Stage currentStage = (Stage) quizTable.getScene().getWindow();
                        // Navigate to the result page using the item's quiz ID and the selected student's user ID
                        QuizResultController.showQuizResultFromDatabase(currentStage, item.getQuizId(), currentSelectedStudentId);
                    } catch (Exception ex) {
                        System.err.println("Error opening quiz result page: " + ex.getMessage());
                        ex.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR,
                                "Unable to load quiz result page. Please try again.");
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
}
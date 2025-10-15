package com.example.cab302a1.ui;

import com.example.cab302a1.dao.ReviewDao;
import com.example.cab302a1.dao.AttemptDao;
import com.example.cab302a1.dao.UserDao;
import com.example.cab302a1.dao.jdbc.DaoFactory;
import com.example.cab302a1.model.QuizReview;
import com.example.cab302a1.model.Student;
import com.example.cab302a1.model.User;
import com.example.cab302a1.result.QuizResultController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Teacher Review Page.
 * Displays a list of student quiz attempts and provides actions for review.
 */
public class TeacherReviewController implements Initializable, ReviewPageController {

    // FXML Fields
    @FXML public TableView<QuizReview> quizTable;
    @FXML public TableColumn<QuizReview, String> quizNameCol;
    @FXML public TableColumn<QuizReview, String> scoreCol;
    @FXML public TableColumn<QuizReview, Void> resultCol; // Button column for viewing results
    @FXML public Button assignReviewBtn; // Button to trigger feedback dialog

    // Student List FXML Container and Label
    @FXML public VBox studentListContainer;
    @FXML public Label studentNameLabel;

    private final ObservableList<QuizReview> reviewData = FXCollections.observableArrayList();
    private Stage stage;

    // DAO Initialization using DaoFactory
    private final ReviewDao reviewDao = DaoFactory.getReviewDao();
    private final AttemptDao attemptDao = DaoFactory.getAttemptDao(); // Used for updating feedback
    private final UserDao userDao = DaoFactory.getUserDao();

    private int currentSelectedStudentId = -1; // Tracks the ID of the student whose quizzes are displayed

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        setupActionButtons();
        loadStudentList(); // Load students dynamically into sidebar

        // Initial setup text
        studentNameLabel.setText("Student Name: (Please select a student)");
        quizTable.setItems(reviewData);
    }

    /**
     * Loads the list of students from the database and populates the sidebar with styled buttons.
     */
    private void loadStudentList() {
        if (studentListContainer == null) return;
        studentListContainer.getChildren().clear();

        // Fetch list of all Users with role 'Student'
        List<User> students = userDao.getAllStudents();

        for (User user : students) {
            if (!(user instanceof Student student)) continue; // Ensure user is a Student

            Button studentBtn = new Button(student.getUsername());

            studentBtn.getStyleClass().add("action-button"); // Apply style
            studentBtn.setMaxWidth(Double.MAX_VALUE);

            studentBtn.setOnAction(e -> {
                // Logic to select the student and load their data
                System.out.println("Teacher selected student: " + student.getUsername() + " (ID: " + student.getUser_id() + ")");
                studentNameLabel.setText("Student Name: " + student.getUsername());
                currentSelectedStudentId = student.getUser_id();
                loadReviewData(); // Reloads table with quizzes for this student
            });

            studentListContainer.getChildren().add(studentBtn);
        }
    }

    /**
     * Sets up the action for the 'Assign Review' button (main feedback logic).
     */
    private void setupActionButtons() {
        if (assignReviewBtn != null) {
            assignReviewBtn.setOnAction(e -> {
                QuizReview selectedQuiz = quizTable.getSelectionModel().getSelectedItem();

                if (selectedQuiz == null || currentSelectedStudentId == -1) {
                    // Check if a quiz attempt and student is selected
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
                        // Use AttemptDao to update the feedback field in quiz_attempts table
                        attemptDao.updateFeedback(attemptId, feedbackText);

                        // 3. Update the UI and inform the user
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Feedback successfully assigned.");
                        successAlert.showAndWait();

                        loadReviewData(); // Reload data to show updated feedback in the table (if applicable)
                    } catch (Exception ex) {
                        // Handle potential SQLException from the DAO method
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

    @Override
    public void setStage(Stage stage) {
        // Required by ReviewPageController interface
    }

    @Override
    public void loadReviewData() {
        reviewData.clear();

        if (currentSelectedStudentId != -1) {
            try {
                // Load quizzes for the currently selected student
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
     * Configures the TableView columns, delegating complex cell factories.
     */
    private void setupTableColumns() {

        if (quizTable == null) return;

        quizTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // Standard column setup (Score)
        if (scoreCol != null) {
            scoreCol.setCellValueFactory(data -> data.getValue().scoreSummaryProperty());
        }

        // Delegate complex button logic
        setupQuizNameColumnAsButton();
        setupResultColumnAsButton();
    }

    // ----------------------------------------------------------------------------------
    // REFACTORED METHOD 1: Setup Quiz Name Column (as Button)
    // ----------------------------------------------------------------------------------

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

    // ----------------------------------------------------------------------------------
    // REFACTORED METHOD 2: Setup View Result Column (Button)
    // ----------------------------------------------------------------------------------

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
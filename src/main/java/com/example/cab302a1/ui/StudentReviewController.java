package com.example.cab302a1.ui;

import com.example.cab302a1.dao.ReviewDao;
import com.example.cab302a1.model.SessionManager;
import com.example.cab302a1.model.QuizReview;
import com.example.cab302a1.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Student Review Page.
 * Displays a list of the student's past quiz attempts.
 */
public class StudentReviewController implements Initializable, ReviewPageController {

    // FXML fields must now use QuizReview
    @FXML public TableView<QuizReview> studentQuizTable;
    @FXML public TableColumn<QuizReview, String> quizNameCol;
    @FXML public TableColumn<QuizReview, String> scoreCol;
    @FXML public TableColumn<QuizReview, Void> viewFeedbackCol; // Button Column

    // NOTE: Ensure your FXML has matching fx:id's

    private final ObservableList<QuizReview> reviewData = FXCollections.observableArrayList();
    private Stage stage;

    // Data Access Object for fetching real data
    private final ReviewDao reviewDao = new ReviewDao();

    // IMPORTANT: Replace this with the actual logged-in user's ID
    private static final int CURRENT_STUDENT_ID = 42;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadReviewData();
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
        System.out.println("StudentReviewController stage set.");
    }

    @Override
    public void loadReviewData() {
        reviewData.clear();

        // CRITICAL FIX: Get the ID of the logged-in user from the session
        User currentUser = SessionManager.getCurrentUser();

        if (currentUser != null) {
            int studentId = currentUser.getUser_id(); // Use the corrected getter

            try {
                // Load quizzes specifically for the logged-in student ID
                reviewData.addAll(reviewDao.getAllAttemptsById(studentId));
                System.out.println("Student ID " + studentId + " loaded " + reviewData.size() + " attempts.");
            } catch (Exception e) {
                System.err.println("Error fetching quiz attempts for current user: " + e.getMessage());
            }
        } else {
            // Handle case where no user is logged in (e.g., during testing or if login failed)
            System.err.println("Load data failed: No current user found in SessionManager.");
        }

        if (reviewData.isEmpty() && studentQuizTable != null) {
            // Display a message if no attempts exist or user is null
            reviewData.add(new QuizReview(-1, "No quiz attempts found.", 0, 0, "N/A"));
        }

        if (studentQuizTable != null) {
            studentQuizTable.setItems(reviewData);
        }
    }

    /**
     * Configures the TableView columns, including styling the action buttons.
     */
    private void setupTableColumns() {
        if (studentQuizTable == null) return;

        // Ensure the table columns fills the width of the container
        studentQuizTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // 1. Quiz Name Column
        quizNameCol.setCellValueFactory(data -> data.getValue().quizNameProperty());

        // 2. Score Column (Displays Score/Total)
        scoreCol.setCellValueFactory(data -> data.getValue().scoreSummaryProperty());

        // 3. View Feedback Column (Button)
        viewFeedbackCol.setCellFactory(col -> new TableCell<QuizReview, Void>() {
            private final Button btn = new Button("View Feedback");

            {
                btn.getStyleClass().add("action-button");
                btn.setPrefWidth(120.0);

                btn.setOnAction(e -> {
                    QuizReview item = getTableView().getItems().get(getIndex());

                    // TODO: Implement logic to open a detailed feedback view or model

                    if (item.getFeedback() != null && !item.getFeedback().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                                "Feedback for " + item.getQuizName() + ":\n\n" + item.getFeedback());
                        alert.setTitle("Teacher Feedback");
                        alert.setHeaderText(item.getQuizName() + " Result Summary");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                                "No specific feedback has been assigned yet.");
                        alert.setTitle("Teacher Feedback");
                        alert.setHeaderText("Feedback Pending");
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

    private void handleExit(javafx.event.ActionEvent event) {
        Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
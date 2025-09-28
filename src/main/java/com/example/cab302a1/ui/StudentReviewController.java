package com.example.cab302a1.ui;

import com.example.cab302a1.model.QuizResult;
import com.example.cab302a1.result.QuizResultController;
import com.example.cab302a1.result.QuizResultData;
import javafx.scene.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class StudentReviewController {

    // Sidebar buttons — public for unit tests
    @FXML public Button dashboardBtn;
    @FXML public Button timetableBtn;
    @FXML public Button reviewBtn;
    @FXML public Button exitBtn;

    @FXML
    public TableView<QuizResult> quizTable;
    @FXML
    public TableColumn<QuizResult, String> quizNameCol;
    @FXML
    public TableColumn<QuizResult, String> scoreCol;
    @FXML
    public TableColumn<QuizResult, Void> viewResultCol;
    @FXML
    public TableColumn<QuizResult, Void> feedbackCol;

    private final ObservableList<QuizResult> quizData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Table column setup
        quizNameCol.setCellValueFactory(data -> data.getValue().quizNameProperty());
        scoreCol.setCellValueFactory(data -> data.getValue().scoreProperty());

        // Set constrained resize policy
        quizTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // "View Result" buttons
        viewResultCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("View");
            {
                btn.getStyleClass().add("action-button");

                // Set up the action for the View button
                btn.setOnAction(e -> {
                    // 1. Retrieve the QuizResult object for this row
                    QuizResult item = getTableView().getItems().get(getIndex());

                    try {
                        // 2. Parse the score string (e.g., "16/20")
                        String scoreString = item.getScore();
                        String[] parts = scoreString.split("/");
                        int correctAnswers = Integer.parseInt(parts[0].trim());
                        int totalQuestions = Integer.parseInt(parts[1].trim());

                        // 3. Create the required QuizResultData object
                        // We use placeholder IDs (0) since the model QuizResult likely doesn't expose them.
                        QuizResultData resultData = new QuizResultData(
                                correctAnswers,
                                totalQuestions,
                                item.getQuizName(),
                                0, // Placeholder Quiz ID
                                0  // Placeholder User ID
                        );

                        // 4. Get the current Stage and navigate to QuizResultController
                        Stage currentStage = (Stage) ((Node)btn).getScene().getWindow();

                        // Call the static showQuizResult method
                        QuizResultController.showQuizResult(currentStage, resultData);

                        System.out.println("Navigating to results for " + item.getQuizName());

                    } catch (Exception ex) {
                        System.err.println("Error navigating to quiz results: " + ex.getMessage());
                        ex.printStackTrace();
                        // Optionally show an Alert to the user here
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                // Check if the item is null or empty, and show the button only if not empty
                setGraphic(empty ? null : new HBox(btn));
            }
        });

        // "Feedback" buttons
        feedbackCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("View");
            {
                btn.getStyleClass().add("action-button");
                btn.setOnAction(e -> {
                    QuizResult item = getTableView().getItems().get(getIndex());
                    System.out.println("Viewing feedback for " + item.getQuizName());
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(btn));
            }
        });

        // Mock data
        quizData.addAll(
                new QuizResult("Quiz 1", "16/20"),
                new QuizResult("Quiz 2", "18/20"),
                new QuizResult("Quiz 3", "12/20"),
                new QuizResult("Quiz 4", "20/20"),
                new QuizResult("Quiz 5", "15/20")
        );

        quizTable.setItems(quizData);
    }

    private void handleExit(javafx.event.ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}

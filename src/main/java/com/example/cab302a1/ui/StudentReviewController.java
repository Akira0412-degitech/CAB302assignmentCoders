package com.example.cab302a1.ui;

import com.example.cab302a1.model.QuizResult;
import com.example.cab302a1.result.QuizResultController;
import com.example.cab302a1.result.QuizResultData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

// IMPLEMENTS ReviewPageController
public class StudentReviewController implements Initializable, ReviewPageController {

    // Sidebar button fields required for testing/navigation
    /* @FXML public Button dashboardBtn;
    @FXML public Button reviewBtn;
    @FXML public Button timetableBtn;
    @FXML public Button exitBtn;
     */

    // Existing FXML components
    @FXML public TableView<QuizResult> quizTable;
    @FXML public TableColumn<QuizResult, String> quizNameCol;
    @FXML public TableColumn<QuizResult, String> scoreCol;
    @FXML public TableColumn<QuizResult, Void> viewResultCol;
    @FXML public TableColumn<QuizResult, Void> feedbackCol;

    private final ObservableList<QuizResult> quizData = FXCollections.observableArrayList();
    private Stage stage; // to store the stage

    // ============================================
    // Interface Implementation: Initializable
    // ============================================

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        // setupActionButtons();
        // Load data immediately or delegate to loadReviewData() after external setup
        loadReviewData();
    }

    // ============================================
    // Interface Implementation: ReviewPageController
    // ============================================

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
        System.out.println("StudentReviewController stage set.");
    }

    @Override
    public void loadReviewData() {
        // Mock data loading (replace with actual database/service call)
        quizData.clear();
        quizData.addAll(
                new QuizResult("Quiz 1", "16/20"),
                new QuizResult("Quiz 2", "18/20"),
                new QuizResult("Quiz 3", "12/20"),
                new QuizResult("Quiz 4", "20/20"),
                new QuizResult("Quiz 5", "15/20")
        );
        quizTable.setItems(quizData);
        System.out.println("Student review data loaded/refreshed.");
    }

    // ============================================
    // Private Setup Methods
    // ============================================

    private void setupTableColumns() {
        // Table column setup
        quizNameCol.setCellValueFactory(data -> data.getValue().quizNameProperty());
        scoreCol.setCellValueFactory(data -> data.getValue().scoreProperty());

        // Set constrained resize policy
        quizTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // "View Result" buttons (Logic from previous step)
        viewResultCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("View");
            {
                btn.getStyleClass().add("action-button");
                btn.setOnAction(e -> {
                    QuizResult item = getTableView().getItems().get(getIndex());
                    try {
                        // 1. Parse score string
                        String scoreString = item.getScore();
                        String[] parts = scoreString.split("/");
                        int correctAnswers = Integer.parseInt(parts[0].trim());
                        int totalQuestions = Integer.parseInt(parts[1].trim());

                        // 2. Create QuizResultData (using placeholder IDs 0, 0)
                        QuizResultData resultData = new QuizResultData(
                                correctAnswers,
                                totalQuestions,
                                item.getQuizName(),
                                0, 0
                        );

                        // 3. Get the current Stage and navigate
                        Stage currentStage = (Stage) ((Node)btn).getScene().getWindow();
                        QuizResultController.showQuizResult(currentStage, resultData);

                    } catch (Exception ex) {
                        System.err.println("Error navigating to quiz results: " + ex.getMessage());
                        // Consider showing an error dialog
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
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
    }

    /*private void setupActionButtons() {
        // Setup sidebar button actions
        dashboardBtn.setOnAction(e -> System.out.println("Dashboard button clicked"));
        reviewBtn.setOnAction(e -> System.out.println("Review button clicked"));
        timetableBtn.setOnAction(e -> System.out.println("Timetable button clicked"));
        exitBtn.setOnAction(this::handleExit);
    }*/

    private void handleExit(javafx.event.ActionEvent event) {
        Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
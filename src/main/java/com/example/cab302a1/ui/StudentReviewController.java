package com.example.cab302a1.ui;

import com.example.cab302a1.model.QuizReview;
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
    public TableView<QuizReview> quizTable;
    @FXML
    public TableColumn<QuizReview, String> quizNameCol;
    @FXML
    public TableColumn<QuizReview, String> scoreCol;
    @FXML
    public TableColumn<QuizReview, Void> viewResultCol;
    @FXML
    public TableColumn<QuizReview, Void> feedbackCol;

    private final ObservableList<QuizReview> quizData = FXCollections.observableArrayList();

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
                btn.setOnAction(e -> {
                    QuizReview item = getTableView().getItems().get(getIndex());
                    System.out.println("Viewing results for " + item.getQuizName());
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
                    QuizReview item = getTableView().getItems().get(getIndex());
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
                new QuizReview("Quiz 1", "16/20"),
                new QuizReview("Quiz 2", "18/20"),
                new QuizReview("Quiz 3", "12/20"),
                new QuizReview("Quiz 4", "20/20"),
                new QuizReview("Quiz 5", "15/20")
        );

        quizTable.setItems(quizData);
    }

    private void handleExit(javafx.event.ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}

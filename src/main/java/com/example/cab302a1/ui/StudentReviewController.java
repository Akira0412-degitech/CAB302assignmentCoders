package com.example.cab302a1.ui;

import com.example.cab302a1.model.QuizResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class StudentReviewController {

    @FXML private Button dashboardBtn;
    @FXML private Button quizzesBtn;
    @FXML private Button reviewBtn;
    @FXML private Button exitBtn;

    @FXML private TableView<QuizResult> quizTable;
    @FXML private TableColumn<QuizResult, String> quizNameCol;
    @FXML private TableColumn<QuizResult, String> scoreCol;
    @FXML private TableColumn<QuizResult, Void> viewResultCol;
    @FXML private TableColumn<QuizResult, Void> feedbackCol;

    private final ObservableList<QuizResult> quizData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Sidebar navigation actions
        dashboardBtn.setOnAction(e -> System.out.println("Home clicked"));
        reviewBtn.setOnAction(e -> System.out.println("Review clicked"));
        quizzesBtn.setOnAction(e -> System.out.println("Timetable clicked"));
        exitBtn.setOnAction(this::handleExit);

        // Setup table columns
        quizNameCol.setCellValueFactory(data -> data.getValue().quizNameProperty());
        scoreCol.setCellValueFactory(data -> data.getValue().scoreProperty());

        // Add "View Result" buttons
        viewResultCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("View");

            {
                btn.getStyleClass().add("action-button");
                btn.setOnAction(e -> {
                    QuizResult item = getTableView().getItems().get(getIndex());
                    System.out.println("View Result clicked for " + item.getQuizName());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(btn));
                }
            }
        });

        // Add "Feedback" buttons
        feedbackCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("View");

            {
                btn.getStyleClass().add("action-button");
                btn.setOnAction(e -> {
                    QuizResult item = getTableView().getItems().get(getIndex());
                    System.out.println("Feedback clicked for " + item.getQuizName());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(btn));
                }
            }
        });

        // Populate with mock data
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

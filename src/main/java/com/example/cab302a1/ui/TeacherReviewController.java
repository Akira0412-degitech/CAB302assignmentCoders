package com.example.cab302a1.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.example.cab302a1.model.QuizResult;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class TeacherReviewController {

    // Sidebar buttons (public so tests can access them)
    @FXML public Button dashboardBtn;
    @FXML public Button quizzesBtn;
    @FXML public Button reviewStudentsBtn;
    @FXML public Button exitBtn;

    // Student list + details
    @FXML public VBox studentListContainer;
    @FXML public Label studentNameLabel;
    @FXML public Button assignReviewBtn;

    // TableView + Columns
    @FXML public TableView<QuizResult> quizTable;
    @FXML public TableColumn<QuizResult, String> quizNameCol;
    @FXML public TableColumn<QuizResult, String> scoreCol;
    @FXML public TableColumn<QuizResult, Void> resultCol;

    // Observable data for the table
    private final ObservableList<QuizResult> quizData = FXCollections.observableArrayList();
    public TableColumn<Object, Object> viewBtnCol;
    public TableColumn<Object, Object> studentNameCol;
    public Button reviewBtn;

    @FXML
    public void initialize() {
        // Sidebar button actions
        dashboardBtn.setOnAction(event -> System.out.println("Dashboard button clicked"));
        quizzesBtn.setOnAction(event -> System.out.println("Quizzes button clicked"));
        reviewStudentsBtn.setOnAction(event -> System.out.println("Review Students button clicked"));
        exitBtn.setOnAction(this::handleExit);
        if (assignReviewBtn != null) {
            assignReviewBtn.setOnAction(event -> System.out.println("Assign Review button clicked"));
        }

        // Setup TableView
        quizNameCol.setCellValueFactory(new PropertyValueFactory<>("quizName"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));

        // Add "View" button column
        if (resultCol != null) {
            resultCol.setCellFactory(param -> new TableCell<>() {
                private final Button viewBtn = new Button("View");

                {
                    viewBtn.setOnAction(event -> {
                        QuizResult result = getTableView().getItems().get(getIndex());
                        System.out.println("Viewing result for " + result.getQuizName());
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : viewBtn);
                }
            });
        }

        // Default student selection
        if (studentNameLabel != null) {
            studentNameLabel.setText("Student Name: Name 5");
        }

        // Sample quiz data
        quizData.setAll(
                new QuizResult("Quiz 1", "16/20"),
                new QuizResult("Quiz 2", "18/20"),
                new QuizResult("Quiz 3", "16/20"),
                new QuizResult("Quiz 4", "16/20"),
                new QuizResult("Quiz 5", "16/20")
        );

        if (quizTable != null) {
            quizTable.setItems(quizData);
        }
    }

    // Exit confirmation dialog
    private void handleExit(javafx.event.ActionEvent event) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setTitle("Exit Confirmation");
        dialog.setWidth(300);
        dialog.setHeight(150);

        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.setStyle("-fx-background-color: white; -fx-padding: 20px; -fx-border-color: #ccc; -fx-border-width: 2px;");
        dialogVbox.getChildren().add(new Label("Are you sure you want to exit?"));

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button loginBtn = new Button("Go to Login");
        Button exitBtn = new Button("Exit App");
        Button cancelButton = new Button("Cancel");

        loginBtn.setOnAction(e -> {
            try {
                dialog.close();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-page.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        exitBtn.setOnAction(e -> {
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.close();
            dialog.close();
        });

        cancelButton.setOnAction(e -> dialog.close());

        buttonBox.getChildren().addAll(loginBtn, exitBtn, cancelButton);
        dialogVbox.getChildren().add(buttonBox);

        Scene dialogScene = new Scene(new StackPane(dialogVbox));
        dialog.setScene(dialogScene);
        dialog.show();
    }
}

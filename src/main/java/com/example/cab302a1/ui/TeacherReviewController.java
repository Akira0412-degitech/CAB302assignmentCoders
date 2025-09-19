package com.example.cab302a1.ui;

import com.example.cab302a1.model.QuizResult;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class TeacherReviewController {

    @FXML
    public Button dashboardBtn;

    @FXML
    public Button quizzesBtn;
    public Button reviewBtn;
    public TableColumn<Object, Object> studentNameCol;
    public TableColumn<Object, Object> quizNameCol;
    public TableColumn<Object, Object> scoreCol;
    public TableColumn<Object, Object> viewBtnCol;

    @FXML
    public Button reviewStudentsBtn;

    @FXML
    public Button exitBtn;

    @FXML
    private Button assignReviewBtn;

    @FXML
    private VBox studentListContainer;

    @FXML
    public TableView<QuizResult> quizTable;

    @FXML
    public void initialize() {
        dashboardBtn.setOnAction(event -> {
            System.out.println("Dashboard button clicked");
        });
        quizzesBtn.setOnAction(event -> {
            System.out.println("Quizzes button clicked");
        });
        reviewStudentsBtn.setOnAction(event -> {
            System.out.println("Review Students button clicked");
        });
        exitBtn.setOnAction(this::handleExit);
        assignReviewBtn.setOnAction(event -> System.out.println("Assign Review button clicked"));
    }

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

    // ✅ Getters for testing
    public TableView<QuizResult> getQuizTable() {
        return quizTable;
    }

    public VBox getStudentListContainer() {
        return studentListContainer;
    }

    public Button getDashboardBtn() {
        return dashboardBtn;
    }

    public Button getQuizzesBtn() {
        return quizzesBtn;
    }

    public Button getReviewStudentsBtn() {
        return reviewStudentsBtn;
    }

    public Button getExitBtn() {
        return exitBtn;
    }

    public Button getAssignReviewBtn() {
        return assignReviewBtn;
    }
}

package com.example.cab302a1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TeacherReviewPage extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("teacher-review-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 750);
        stage.setTitle("Interactive Quiz Creator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package com.example.cab302a1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class QuizApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                Objects.requireNonNull(getClass().getResource("/com/example/cab302a1/MainView.fxml"))
        );
        Scene scene = new Scene(loader.load(), 1000, 640);
        stage.setTitle("Quiz App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

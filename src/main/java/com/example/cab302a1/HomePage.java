package com.example.cab302a1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class HomePage extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                Objects.requireNonNull(getClass().getResource("/com/example/cab302a1/HomePage.fxml"))
        );

        Scene scene = new Scene(loader.load(), 1000, 640);
        var css = getClass().getResource("/com/example/cab302a1/HomePage.css");

        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        stage.setTitle("Quiz App");
        stage.setWidth(1000);
        stage.setHeight(600);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

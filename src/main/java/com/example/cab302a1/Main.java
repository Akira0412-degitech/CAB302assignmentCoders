package com.example.cab302a1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setTitle("CAB302 Assessment - Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
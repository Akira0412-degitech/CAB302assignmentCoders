package com.example.cab302a1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Db connection check
        try (var conn = DBconnection.getConnection()){
            System.out.println(("Connected to DB: " + conn.getCatalog()));
        } catch (Exception e){
            e.printStackTrace();
        }

        // Load the FXML file for the review page UI
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login/login-view.fxml"));

        // Changed dimensions to be smaller and more compact
        Scene scene = new Scene(fxmlLoader.load(), 1000, 450); // Adjusted dimensions: width 1100, height 750

        // Apply a basic stylesheet for common styles
//        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
//
//        stage.setTitle("Quiz App - Teacher Review Page"); // Updated title for clarity
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
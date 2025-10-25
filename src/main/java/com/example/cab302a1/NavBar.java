package com.example.cab302a1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class NavBar extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        // Load the FXML file for the navbar demo
        FXMLLoader fxmlLoader = new FXMLLoader(NavBar.class.getResource("demo-navbar-integration.fxml"));

        // Set the scene dimensions to accommodate the navbar demo
        Scene scene = new Scene(fxmlLoader.load(), 1000, 650);

        // Apply a basic stylesheet for common styles
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        stage.setTitle("Interactive Quiz Creator - Navbar Demo");
        stage.setScene(scene);
        stage.setResizable(true); // Allow resizing to test navbar responsiveness
        stage.setMinWidth(800); // Set minimum window size
        stage.setMinHeight(500);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}



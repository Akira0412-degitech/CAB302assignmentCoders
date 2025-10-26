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
        DBconnection.migrate();


        // Load the FXML file for the login page UI
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/cab302a1/Login/Login-view.fxml"));

        // Set the scene dimensions to fit the new login design
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        
        // Load CSS stylesheet for login page
        try {
            String cssPath = Main.class.getResource("/com/example/cab302a1/Login/Login.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.err.println("Could not load Login.css: " + e.getMessage());
        }

        stage.setTitle("Interactive Quiz Creator - Login");
        stage.setScene(scene);
        stage.setResizable(true); // Allow resizing for better user experience
        stage.setMinWidth(600);   // Set minimum width
        stage.setMinHeight(500);  // Set minimum height
        stage.centerOnScreen();   // Center the window
            stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

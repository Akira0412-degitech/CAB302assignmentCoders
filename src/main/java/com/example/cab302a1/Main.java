package com.example.cab302a1;

import com.example.cab302a1.dao.UserDao;
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
        try (var conn = DBconnection.getConnection()){
            System.out.println(("Connected to DB: " + conn.getCatalog()));
            UserDao userdao = new UserDao();
            userdao.printAllUsers();

        } catch (Exception e){
            e.printStackTrace();
        }

        // Load the FXML file for the review page UI
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/cab302a1/login/login-view.fxml"));

        // Set the scene dimensions to better fit the content and prevent resizing
        Scene scene = new Scene(fxmlLoader.load(), 1040, 600);

        // Apply a basic stylesheet for common styles
//        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
//
//        stage.setTitle("Quiz App - Teacher Review Page"); // Updated title for clarity
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.setResizable(false); // Make the window non-resizable
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

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

        // Load the FXML file for the navbar demo
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("demo-navbar-integration.fxml"));

        // Set the scene dimensions to accommodate the navbar demo
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);

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

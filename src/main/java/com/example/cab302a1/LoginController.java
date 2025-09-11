package com.example.cab302a1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the login page.
 * Handles the logic for a user to select between Student and Teacher views.
 */
public class LoginController {

    @FXML
    private Button teacherLoginBtn;

    @FXML
    private Button studentLoginBtn;

    /**
     * Initializes the controller. Sets up button actions.
     */
    @FXML
    public void initialize() {
        teacherLoginBtn.setOnAction(event -> handleTeacherLogin());
        studentLoginBtn.setOnAction(event -> handleStudentLogin());
    }

    /**
     * Handles the Teacher Login button click.
     * Loads the main review page for the teacher.
     */
    private void handleTeacherLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("review-page-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
            Stage stage = (Stage) teacherLoginBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("CAB302 Assessment - Tutor View");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the Student Login button click.
     * Loads the student's self-review page.
     */
    private void handleStudentLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("student-main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
            Stage stage = (Stage) studentLoginBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("CAB302 Assessment - Student View");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

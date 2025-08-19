package com.example.cab302a1.Login;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;

import javax.swing.*;
import javafx.event.ActionEvent;
import java.io.IOException;

public class LoginController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField usernameField;   // Username input field

    @FXML
    private PasswordField passwordField;   // Password input field

    @FXML
    private Hyperlink signUpLink;  // Link to go to Sign Up page

    @FXML
    protected void handleLogin(ActionEvent event) throws IOException {
        // Get entered username and password
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Temporary login check (username: admin, password: 1234)
        if (username.equals("admin") && password.equals("1234")) {
            System.out.println("Login successful!");

            // Load HomePage.fxml and switch to Home scene
            root = FXMLLoader.load(getClass().getResource("/com/example/cab302a1/HomePage.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1000, 450);
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.show();
        } else {
            // Show error message if login fails
            System.out.println("Invalid credentials.");
        }
    }

    @FXML
    private void handleSignUpClick(ActionEvent event) throws IOException {
        // Load SignUp-view.fxml and switch to Sign Up scene
        root = FXMLLoader.load(getClass().getResource("/com/example/cab302a1/SignUp/SignUp-view.fxml"));
        scene = new Scene(root, 1000, 450);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("SignUp");
        stage.setScene(scene);
        stage.show();
    }
}

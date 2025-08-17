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
    private  Parent root;
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Hyperlink signUpLink;
    @FXML
    protected void handleLogin(ActionEvent event) throws IOException{
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals("admin") && password.equals("1234")) {
            System.out.println("Login successful!");

            root = FXMLLoader.load(getClass().getResource("/com/example/cab302a1/HomePage.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1000, 450);
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Invalid credentials.");
        }
    }
    @FXML
    private void handleSignUpClick (ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("/com/example/cab302a1/SignUp/SignUp-view.fxml"));
        scene = new Scene(root, 1000, 450);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("SignUp");
        stage.setScene(scene);
        stage.show();
    }
}

package com.example.cab302a1.Login;

import com.example.cab302a1.dao.UserDao;
import com.example.cab302a1.dao.jdbc.DaoFactory;
import com.example.cab302a1.dao.jdbc.JdbcUserDao;
import com.example.cab302a1.model.Student;
import com.example.cab302a1.model.Teacher;
import com.example.cab302a1.model.User;
import com.example.cab302a1.util.Session;
import com.example.cab302a1.components.NavigationManager;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;

import javafx.event.ActionEvent;
import java.io.IOException;

public class LoginController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    TextField emailField;   // Email input field

    @FXML
    PasswordField passwordField;   // Password input field

    @FXML
    private Hyperlink signupLink;  // Link to go to Sign Up page

    @FXML
    Label errorloginLabel;


    UserDao userdao = DaoFactory.getUserDao();

    @FXML
    private void initialize() {
        // Remove auto-focus from first field by focusing on the container instead
        // This will be executed after the FXML is loaded
        javafx.application.Platform.runLater(() -> {
            if (emailField.getParent() != null) {
                emailField.getParent().requestFocus();
            }
        });
    }

    @FXML
    protected void handleLogin(ActionEvent event) throws IOException {

        String email = emailField.getText();
        String password = passwordField.getText();

        // Validation - ensure fields are not empty
        if(email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()){
            showErrorMessage("Please enter valid email and password");
            return;
        }

        User currentUser = userdao.login(email.trim(), password);

        if(currentUser != null){
            System.out.println("Login successful: " + currentUser.getEmail());
            Session.setCurrentUser(currentUser);
            String title = "";
            if(currentUser instanceof Student){
                title = "Student";
            } else if(currentUser instanceof Teacher){
                title = "Teacher";
            }
            root = FXMLLoader.load(getClass().getResource("/com/example/cab302a1/HomePage.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1000, 640);
            
            // Load HomePage CSS
            try {
                String cssPath = getClass().getResource("/com/example/cab302a1/HomePage.css").toExternalForm();
                scene.getStylesheets().add(cssPath);
                System.out.println("HomePage CSS loaded successfully: " + cssPath);
            } catch (Exception e) {
                System.err.println("Could not load HomePage.css: " + e.getMessage());
            }
            
            stage.setTitle(title + "-Home");
            stage.setScene(scene);
            
            // Clear any existing navigation history since this is a fresh login
            NavigationManager.getInstance().clearHistory();
            
            stage.show();

        }else{
            showErrorMessage("Invalid username or password. Please try again.");
        }

    }

    /**
     * Helper method to display error messages with proper UI management
     */
    private void showErrorMessage(String message) {
        errorloginLabel.setText(message);
        errorloginLabel.setVisible(true);
        errorloginLabel.setManaged(true);
    }

    @FXML
    private void handleSignUpClick(ActionEvent event) throws IOException {
        // Load SignUp-view.fxml and switch to Sign Up scene
        root = FXMLLoader.load(getClass().getResource("/com/example/cab302a1/SignUp/SignUp-view.fxml"));
        scene = new Scene(root, 800, 720);

        // Load CSS for SignUp page
        try {
            String cssPath = getClass().getResource("/com/example/cab302a1/SignUp/SignUp.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
            System.out.println("SignUp CSS loaded successfully: " + cssPath);
        } catch (Exception e) {
            System.err.println("Could not load SignUp.css: " + e.getMessage());
        }

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Interactive Quiz Creator - SignUp");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }


}

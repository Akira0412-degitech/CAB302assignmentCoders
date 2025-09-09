package com.example.cab302a1.SignUp;

import com.example.cab302a1.dao.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField useremailField;   // Username input field

    @FXML
    private PasswordField passwordField;   // Password input field

    @FXML
    private ChoiceBox<String> roleBox;   // Dropdown for selecting role (Teacher/Student)

    @FXML
    private Hyperlink loginLink;   // Link to go back to Login page

    @FXML
    private Label errorsignup;

    UserDao userdao = new UserDao();

    @FXML
    private void initialize() {
        // Add items to the role selection box
        roleBox.getItems().addAll("Teacher", "Student");
        // Default value is Student
        roleBox.setValue("Student");
    }
    @FXML
    private void handleLoginClick(ActionEvent event) throws IOException {
        // Switch to Login page
        //aaa
        root = FXMLLoader.load(getClass().getResource("/com/example/cab302a1/Login/Login-view.fxml"));
        scene = new Scene(root, 1000, 450);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleSignUpclick(ActionEvent event) throws IOException {
        // Get values from input fields
        String useremail = useremailField.getText();
        String password = passwordField.getText();
        String role = roleBox.getValue();

        // Simple check: only "akira / 1234 / Student" is accepted

        if(useremail.isEmpty() || password.isEmpty()){
            errorsignup.setText("Please fill the form to sing up");
        }

        if(userdao.signUpUser(useremail, password)){
            // Switch to Home page
            root = FXMLLoader.load(getClass().getResource("/com/example/cab302a1/HomePage.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1000, 450);
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.show();
        } else {
            // Print error if sign up fails
            System.out.println("Invalid credentials.");
        }

    }
}

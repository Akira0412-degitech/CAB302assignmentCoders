package com.example.cab302a1.SignUp;

import com.example.cab302a1.dao.UserDao;
import com.example.cab302a1.model.Student;
import com.example.cab302a1.model.Teacher;
import com.example.cab302a1.model.User;
import com.example.cab302a1.util.Session;
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

        if(useremail.isEmpty() || password.isEmpty()){
            errorsignup.setText("Please fill the form to sing up");
            return;
        }
        User currentUser = userdao.signUpUser(useremail, password, role);
        if(currentUser != null){
            Session.setCurrentUser(currentUser);
            System.out.println("Signup successfully" + currentUser.getEmail());
            if(currentUser instanceof Student){
                root = FXMLLoader.load(getClass().getResource("/com/example/cab302a1/HomePage.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root, 1000, 450);
                stage.setTitle("Home");
                stage.setScene(scene);
                stage.show();
            } else if(currentUser instanceof Teacher){
                root = FXMLLoader.load(getClass().getResource("/com/example/cab302a1/HomePage.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root, 1000, 450);
                stage.setTitle("Home");
                stage.setScene(scene);
                stage.show();
            }
        }else{
            errorsignup.setText("Something went wrong, Try again later.");
        }
    }
}

package com.example.cab302a1.Login;

import com.example.cab302a1.dao.UserDao;
import com.example.cab302a1.model.Student;
import com.example.cab302a1.model.Teacher;
import com.example.cab302a1.model.User;
import com.example.cab302a1.util.Session;
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
    TextField useremailField;   // Username input field

    @FXML
    PasswordField passwordField;   // Password input field

    @FXML
    private Hyperlink signupLink;  // Link to go to Sign Up page

    @FXML
    Label errorloginLabel;


    UserDao userdao = new UserDao();
    @FXML
    protected void handleLogin(ActionEvent event) throws IOException {

        String userEmail = useremailField.getText();
        String password = passwordField.getText();

        if(userEmail == null || password == null){
            errorloginLabel.setText("Invalid username or password");
        }

        User currentUser = userdao.login(userEmail, password);

        if(currentUser != null){
            System.out.println("Login successfully" + currentUser.getEmail());
            Session.setCurrentUser(currentUser);
            String title = "";
            if(currentUser instanceof Student){
                title = "Student";
            } else if(currentUser instanceof Teacher){
                title = "Teacher";
            }
            root = FXMLLoader.load(getClass().getResource("/com/example/cab302a1/HomePage.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1000, 450);
            stage.setTitle(title + "-Home");
            stage.setScene(scene);
            stage.show();
        }else{
            errorloginLabel.setText("Invalid username or password");
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

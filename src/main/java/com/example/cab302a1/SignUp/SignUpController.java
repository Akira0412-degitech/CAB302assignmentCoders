package com.example.cab302a1.SignUp;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;


public class SignUpController   {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ChoiceBox roleBox;

    @FXML
    private void initialize() {
        roleBox.getItems().addAll("Teacher", "Student");

        roleBox.setValue("Student");
    }

    @FXML
    private void handleLoginClick (ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("/com/example/cab302a1/SignUp/Login-view.fxml"));
        scene = new Scene(root, 320, 240);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
}

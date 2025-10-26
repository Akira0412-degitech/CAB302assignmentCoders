package com.example.cab302a1.Login;

import com.example.cab302a1.dao.UserDao;
import com.example.cab302a1.dao.DaoFactory;
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

/**
 * Controller class responsible for managing the Login page functionality.
 * <p>
 * This class handles user authentication, scene transitions to the HomePage or
 * SignUp page, and UI updates for login error handling. It interacts with
 * {@link UserDao} for authentication and uses {@link NavigationManager} to
 * manage navigation history.
 * </p>
 *
 * <p><b>Responsibilities:</b></p>
 * <ul>
 *   <li>Validate user login input and perform authentication</li>
 *   <li>Navigate to HomePage upon successful login</li>
 *   <li>Display appropriate error messages for invalid credentials</li>
 *   <li>Handle navigation to Sign Up page via hyperlink</li>
 * </ul>
 */
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

    /**
     * Initializes the Login view controller.
     * <p>
     * This method removes the default focus from the first text field
     * to improve user experience by placing focus on the container.
     * </p>
     */
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

    /**
     * Handles user login when the "Login" button is clicked.
     * <p>
     * This method validates the input, authenticates the user via {@link UserDao#login(String, String)},
     * sets the current session, and transitions to the HomePage scene if login is successful.
     * In case of invalid credentials, an error message is displayed on the UI.
     * </p>
     *
     * @param event The {@link ActionEvent} triggered by clicking the Login button.
     * @throws IOException If there is an issue loading the next FXML view.
     */
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
     * Displays an error message on the login page.
     * <p>
     * This helper method updates the error label with a custom message and ensures
     * it is visible and managed within the UI layout.
     * </p>
     *
     * @param message The error message to be displayed.
     */
    private void showErrorMessage(String message) {
        errorloginLabel.setText(message);
        errorloginLabel.setVisible(true);
        errorloginLabel.setManaged(true);
    }

    /**
     * Handles the click event of the Sign Up hyperlink.
     * <p>
     * This method switches the current scene to the Sign Up page defined in
     * {@code SignUp-view.fxml}, and applies its corresponding CSS stylesheet.
     * </p>
     *
     * @param event The {@link ActionEvent} triggered by clicking the Sign Up link.
     * @throws IOException If the FXML file or CSS cannot be loaded.
     */
    @FXML
    private void handleSignUpClick(ActionEvent event) throws IOException {
        // Load SignUp-view.fxml and switch to Sign Up scene
        root = FXMLLoader.load(getClass().getResource("/com/example/cab302a1/SignUp/SignUp-view.fxml"));
        scene = new Scene(root, 800, 720);

        // Load CSS for SignUp page
        try {
            String cssPath = getClass().getResource("/com/example/cab302a1/SignUp/SignUp.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
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

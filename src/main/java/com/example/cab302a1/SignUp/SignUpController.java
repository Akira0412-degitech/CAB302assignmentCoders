package com.example.cab302a1.SignUp;

import com.example.cab302a1.dao.UserDao;
import com.example.cab302a1.dao.jdbc.DaoFactory;
import com.example.cab302a1.dao.jdbc.JdbcUserDao;
import com.example.cab302a1.model.Student;
import com.example.cab302a1.model.Teacher;
import com.example.cab302a1.model.User;
import com.example.cab302a1.util.Session;
import com.example.cab302a1.components.NavigationManager;
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
    TextField usernameField;   // Username input field

    @FXML
    TextField emailField;   // Email input field (for database storage)

    @FXML
    PasswordField passwordField;   // Password input field

    @FXML
    ComboBox<String> roleBox;   // Dropdown for selecting role (Teacher/Student)

    @FXML
    private Hyperlink loginLink;   // Link to go back to Login page

    @FXML
    Label errorsignup;

    UserDao userdao = DaoFactory.getUserDao();




    @FXML
    private void initialize() {
        // Add items to the role selection box
        roleBox.getItems().addAll("Teacher", "Student");
        // Default value is Student
        roleBox.setValue("Student");
        
        // Custom cell factory to fix tick visibility
        setupRoleBoxCellFactory();
        
        // Remove auto-focus from first field by focusing on the container instead
        // This will be executed after the FXML is loaded
        javafx.application.Platform.runLater(() -> {
            if (usernameField.getParent() != null) {
                usernameField.getParent().requestFocus();
            }
        });
    }
    
    private void setupRoleBoxCellFactory() {
        // Set up custom cell factory for ComboBox dropdown items
        roleBox.setCellFactory(listView -> {
            return new javafx.scene.control.ListCell<String>() {
                private final javafx.scene.control.Label checkmark = new javafx.scene.control.Label("✓");
                
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        // Create HBox to hold text and checkmark
                        javafx.scene.layout.HBox container = new javafx.scene.layout.HBox();
                        container.setSpacing(8);
                        container.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                        
                        javafx.scene.control.Label textLabel = new javafx.scene.control.Label(item);
                        textLabel.setStyle("-fx-text-fill: #000000; -fx-font-size: 14px; -fx-font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', Arial, sans-serif;");
                        
                        // Setup checkmark
                        checkmark.setStyle("-fx-text-fill: #005BA1; -fx-font-weight: bold; -fx-font-size: 16px;");
                        checkmark.setVisible(false);
                        
                        container.getChildren().addAll(textLabel, checkmark);
                        
                        // Add spacer to push checkmark to the right
                        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
                        javafx.scene.layout.HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
                        container.getChildren().add(1, spacer);
                        
                        setGraphic(container);
                        setText(null);
                        
                        // Update based on selection state
                        updateCellAppearance(textLabel, roleBox.getValue() != null && roleBox.getValue().equals(item));
                        
                        // Listen for selection changes
                        roleBox.valueProperty().addListener((obs, oldVal, newVal) -> {
                            updateCellAppearance(textLabel, newVal != null && newVal.equals(item));
                        });
                        
                        // Hover effect
                        setOnMouseEntered(e -> {
                            if (!isCurrentlySelected(item)) {
                                textLabel.setStyle("-fx-text-fill: #005BA1; -fx-font-size: 14px; -fx-font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', Arial, sans-serif;");
                                setStyle("-fx-background-color: #DBEAFE;");
                            }
                        });
                        
                        setOnMouseExited(e -> {
                            if (!isCurrentlySelected(item)) {
                                textLabel.setStyle("-fx-text-fill: #000000; -fx-font-size: 14px; -fx-font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', Arial, sans-serif;");
                                setStyle("-fx-background-color: transparent;");
                            }
                        });
                    }
                }
                
                private boolean isCurrentlySelected(String item) {
                    return roleBox.getValue() != null && roleBox.getValue().equals(item);
                }
                
                private void updateCellAppearance(javafx.scene.control.Label textLabel, boolean isSelectedItem) {
                    if (isSelectedItem) {
                        checkmark.setVisible(true);
                        textLabel.setStyle("-fx-text-fill: #005BA1; -fx-font-size: 14px; -fx-font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', Arial, sans-serif; -fx-font-weight: bold;");
                        setStyle("-fx-background-color: #A5D8FF;");
                    } else {
                        checkmark.setVisible(false);
                        textLabel.setStyle("-fx-text-fill: #000000; -fx-font-size: 14px; -fx-font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', Arial, sans-serif;");
                        setStyle("-fx-background-color: transparent;");
                    }
                }
            };
        });
        
        // Also set button cell factory for the main display
        roleBox.setButtonCell(new javafx.scene.control.ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: #000000; -fx-font-size: 14px; -fx-font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', Arial, sans-serif;");
                }
            }
        });
        
        // Make ComboBox non-editable (dropdown only)
        roleBox.setEditable(false);
    }

    @FXML
    private void handleLoginClick(ActionEvent event) throws IOException {
        // Switch to Login page
        root = FXMLLoader.load(getClass().getResource("/com/example/cab302a1/Login/Login-view.fxml"));
        scene = new Scene(root, 800, 600);

        // Load CSS for Login page
        try {
            String cssPath = getClass().getResource("/com/example/cab302a1/Login/Login.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
            System.out.println("Login CSS loaded successfully: " + cssPath);
        } catch (Exception e) {
            System.err.println("Could not load Login.css: " + e.getMessage());
        }

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Interactive Quiz Creator - Login");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    void handleSignUpclick(ActionEvent event) throws IOException {
        // Get values from input fields
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String role = roleBox.getValue();

        // Validation - ensure all fields are filled
        if(username.isEmpty() || email.isEmpty() || password.isEmpty() || role == null){
            showErrorMessage("Please fill in all fields to sign up");
            return;
        }

        // Basic email validation
        if(!isValidEmail(email)){
            showErrorMessage("Something went wrong, Try again later.");
            return;
        }

        // Store username and email separately in database
        User currentUser = userdao.signUpUser(username, email, password, role);

        if(currentUser != null){
            Session.setCurrentUser(currentUser);
            System.out.println("Signup successfully: " + currentUser.getEmail());
            System.out.println("Username (display): " + username + ", Email (database): " + email);

            root = FXMLLoader.load(getClass().getResource("/com/example/cab302a1/HomePage.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1000, 640);
            
            // Load HomePage CSS
            try {
                String cssPath = getClass().getResource("/com/example/cab302a1/HomePage.css").toExternalForm();
                scene.getStylesheets().add(cssPath);
                System.out.println("HomePage CSS loaded successfully after signup: " + cssPath);
            } catch (Exception e) {
                System.err.println("Could not load HomePage.css after signup: " + e.getMessage());
            }
            
            stage.setTitle(currentUser.getRole() + "-Home");
            stage.setScene(scene);
            
            // Clear any existing navigation history since this is a fresh signup
            NavigationManager.getInstance().clearHistory();
            
            stage.show();
        }else {
            showErrorMessage("Something went wrong, Try again later.");
        }
    }

    /**
     * Helper method to display error messages with proper UI management
     */
    private void showErrorMessage(String message) {
        errorsignup.setText(message);
        errorsignup.setVisible(true);
        errorsignup.setManaged(true);
    }

    /**
     * Basic email validation
     */
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && email.length() > 5;
    }
}

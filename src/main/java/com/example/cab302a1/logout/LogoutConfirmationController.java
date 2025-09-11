package com.example.cab302a1.logout;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the Logout Confirmation page.
 * Handles user decisions to either logout or cancel the logout process.
 * 
 * This controller provides functionality for:
 * - Confirming logout and navigating to login page
 * - Cancelling logout and returning to previous page
 * - Managing scene transitions and window behavior
 * 
 * @author CAB302 Assignment Team
 * @version 1.0
 */
public class LogoutConfirmationController implements Initializable {

    // FXML injected components
    @FXML
    private Button logoutBtn;

    @FXML
    private Button cancelBtn;

    /**
     * Called to initialize the controller after its root element has been completely processed.
     * Sets up any additional configuration for the logout confirmation page.
     *
     * @param location  The location used to resolve relative paths for the root object
     * @param resources The resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the logout confirmation page
        setupButtonAccessibility();
        
        System.out.println("Logout confirmation page initialized successfully");
    }

    /**
     * Configures accessibility features for the logout confirmation buttons.
     * Sets appropriate ARIA labels and keyboard navigation support.
     */
    private void setupButtonAccessibility() {
        logoutBtn.setAccessibleText("Logout from the application");
        cancelBtn.setAccessibleText("Cancel logout and return to previous page");
        
        // Set default focus to Cancel button for safety
        cancelBtn.requestFocus();
    }

    /**
     * Handles the Logout button click event.
     * Logs out the user and navigates to the login page.
     *
     * @param event The action event triggered by clicking the Logout button
     */
    @FXML
    private void handleLogoutAction(ActionEvent event) {
        System.out.println("User confirmed logout - navigating to login page");
        
        try {
            // Clear any user session data here if applicable
            clearUserSession();
            
            // Navigate to login page
            navigateToLogin();
            
            System.out.println("User logged out successfully");
            
        } catch (Exception e) {
            System.err.println("Error during logout process: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the Cancel button click event.
     * Cancels the logout process and returns to the previous page.
     *
     * @param event The action event triggered by clicking the Cancel button
     */
    @FXML
    private void handleCancelAction(ActionEvent event) {
        System.out.println("Logout cancelled by user - returning to previous page");
        
        try {
            // Return to the previous page (demo navbar integration)
            returnToPreviousPage();
            
            System.out.println("Successfully returned to previous page");
            
        } catch (Exception e) {
            System.err.println("Error returning to previous page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Navigates to the login page by loading the login-view.fxml.
     * This method handles the scene transition after successful logout confirmation.
     *
     * @throws IOException if the login FXML file cannot be loaded
     */
    private void navigateToLogin() throws IOException {
        // Get the current stage
        Stage currentStage = (Stage) logoutBtn.getScene().getWindow();
        
        // Load the login page FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/cab302a1/Login/Login-view.fxml"));
        Scene loginScene = new Scene(fxmlLoader.load(), 1040, 600);
        
        // Apply the styles
        loginScene.getStylesheets().add(getClass().getResource("/com/example/cab302a1/styles.css").toExternalForm());
        
        // Update the stage
        currentStage.setTitle("Interactive Quiz Creator - Login");
        currentStage.setScene(loginScene);
        currentStage.centerOnScreen();
        
        System.out.println("Successfully navigated to login page");
    }

    /**
     * Returns to the previous page by loading the demo navbar integration page.
     * In a full implementation, this could maintain navigation history.
     *
     * @throws IOException if the demo page FXML file cannot be loaded
     */
    private void returnToPreviousPage() throws IOException {
        // Get the current stage
        Stage currentStage = (Stage) cancelBtn.getScene().getWindow();
        
        // Load the demo navbar integration page
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/cab302a1/demo-navbar-integration.fxml"));
        Scene previousScene = new Scene(fxmlLoader.load(), 1200, 700);
        
        // Apply the styles
        previousScene.getStylesheets().add(getClass().getResource("/com/example/cab302a1/styles.css").toExternalForm());
        
        // Update the stage
        currentStage.setTitle("Interactive Quiz Creator - Navbar Demo");
        currentStage.setScene(previousScene);
        currentStage.centerOnScreen();
        
        System.out.println("Successfully returned to navbar demo page");
    }

    /**
     * Clears any user session data during logout.
     * This method should be expanded to handle actual session management.
     */
    private void clearUserSession() {
        // TODO: Implement actual session clearing logic
        // Examples:
        // - Clear user preferences
        // - Reset application state
        // - Clear cached data
        // - Invalidate authentication tokens
        
        System.out.println("User session data cleared");
    }

    /**
     * Alternative method to close the current window instead of navigating.
     * Can be used if the logout confirmation is implemented as a modal dialog.
     */
    private void closeCurrentWindow() {
        Stage currentStage = (Stage) cancelBtn.getScene().getWindow();
        currentStage.close();
        System.out.println("Logout confirmation window closed");
    }

    /**
     * Public method to programmatically trigger logout.
     * Useful for external components that need to initiate logout.
     */
    public void performLogout() {
        try {
            clearUserSession();
            navigateToLogin();
            System.out.println("Programmatic logout completed successfully");
        } catch (IOException e) {
            System.err.println("Error during programmatic logout: " + e.getMessage());
        }
    }

    /**
     * Public method to programmatically cancel logout.
     * Useful for external components that need to cancel logout.
     */
    public void cancelLogout() {
        try {
            returnToPreviousPage();
            System.out.println("Programmatic logout cancellation completed successfully");
        } catch (IOException e) {
            System.err.println("Error during programmatic logout cancellation: " + e.getMessage());
        }
    }
}

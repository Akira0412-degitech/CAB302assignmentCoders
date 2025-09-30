package com.example.cab302a1.logout;

import com.example.cab302a1.components.NavigationManager;
import com.example.cab302a1.util.Session;
import com.example.cab302a1.model.User;
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
     * Cancels the logout process and returns to the previous page using NavigationManager.
     *
     * @param event The action event triggered by clicking the Cancel button
     */
    @FXML
    private void handleCancelAction(ActionEvent event) {
        System.out.println("Logout cancelled by user - returning to previous page");
        
        try {
            // Get the current stage
            Stage currentStage = (Stage) cancelBtn.getScene().getWindow();
            
            // Use NavigationManager to go back to previous page
            NavigationManager navigationManager = NavigationManager.getInstance();
            
            // Check if we have navigation history and user is still logged in
            if (navigationManager.hasHistory() && Session.isLoggedaIn()) {
                boolean navigationSuccessful = navigationManager.navigateBack(currentStage);
                
                if (navigationSuccessful) {
                    System.out.println("Successfully returned to previous page via NavigationManager");
                    return; // Successfully navigated back
                }
            }
            
            // If no history available or navigation failed, use intelligent fallback
            System.out.println("No navigation history available or navigation failed, using intelligent fallback");
            returnToPreviousPageFallback();
            
        } catch (Exception e) {
            System.err.println("Error returning to previous page: " + e.getMessage());
            e.printStackTrace();
            
            // Try fallback navigation as last resort
            try {
                returnToPreviousPageFallback();
            } catch (IOException fallbackError) {
                System.err.println("Fallback navigation also failed: " + fallbackError.getMessage());
            }
        }
    }

    /**
     * Navigates to the login page using NavigationManager.
     * This method handles the scene transition after successful logout confirmation.
     *
     * @throws IOException if the login FXML file cannot be loaded
     */
    private void navigateToLogin() throws IOException {
        // Get the current stage
        Stage currentStage = (Stage) logoutBtn.getScene().getWindow();
        
        // Clear navigation history since user is logging out
        NavigationManager navigationManager = NavigationManager.getInstance();
        navigationManager.clearHistory();
        
        // Navigate to login page (replace, don't add to history)
        navigationManager.navigateToReplace(currentStage, NavigationManager.Pages.LOGIN);
        
        System.out.println("Successfully navigated to login page");
    }

    /**
     * Intelligent fallback method to return to appropriate page based on user state.
     * This provides a safety net for navigation - prioritizes role-specific home if user is logged in.
     *
     * @throws IOException if the page cannot be loaded
     */
    private void returnToPreviousPageFallback() throws IOException {
        Stage currentStage = (Stage) cancelBtn.getScene().getWindow();
        NavigationManager navigationManager = NavigationManager.getInstance();
        
        // CRITICAL: Double-check that user is still logged in
        // Sometimes session might be cleared unexpectedly
        if (Session.isLoggedaIn()) {
            User currentUser = Session.getCurrentUser();
            if (currentUser != null) {
                // Navigate to role-specific home page
                navigationManager.navigateToReplace(currentStage, NavigationManager.Pages.HOME);
                
                String roleTitle = currentUser.getRole();
                currentStage.setTitle("Interactive Quiz Creator - " + roleTitle + " Home");
                
                System.out.println("Used intelligent fallback navigation to " + roleTitle + " home page");
                return;
            }
        }
        
        // If no user is logged in or session is invalid, go to login page instead of navbar demo
        // This is safer than navbar demo and provides a clear path for the user
        System.out.println("User session invalid or not logged in, redirecting to login page");
        navigationManager.navigateToReplace(currentStage, NavigationManager.Pages.LOGIN);
        
        System.out.println("Used fallback navigation to login page (session invalid)");
    }

    /**
     * Clears any user session data during logout.
     * This method clears the current user session and any cached data.
     */
    private void clearUserSession() {
        // Clear the current user session
        Session.clearUser();
        
        // TODO: Implement additional session clearing logic as needed
        // Examples:
        // - Clear user preferences
        // - Reset application state
        // - Clear cached data
        // - Invalidate authentication tokens
        
        System.out.println("User session data cleared successfully");
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
            // Try to navigate back using NavigationManager
            Stage currentStage = (Stage) cancelBtn.getScene().getWindow();
            NavigationManager navigationManager = NavigationManager.getInstance();
            
            boolean navigationSuccessful = navigationManager.navigateBack(currentStage);
            if (!navigationSuccessful) {
                returnToPreviousPageFallback();
            }
            
            System.out.println("Programmatic logout cancellation completed successfully");
        } catch (IOException e) {
            System.err.println("Error during programmatic logout cancellation: " + e.getMessage());
        }
    }
}

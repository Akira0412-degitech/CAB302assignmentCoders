package com.example.cab302a1.components;

import com.example.cab302a1.util.Session;
import com.example.cab302a1.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the reusable Navbar component.
 * Handles navigation actions for the Interactive Quiz Creator application.
 * 
 * This controller provides navigation functionality for:
 * - Home page navigation
 * - Review page navigation  
 * - Timetable page navigation
 * - Application exit functionality
 * 
 * @author CAB302 Assignment Team
 * @version 1.0
 */
public class NavbarController implements Initializable {

    // FXML injected components
    @FXML
    private VBox navbarContainer;

    @FXML
    private Button homeBtn;

    @FXML
    private Button reviewBtn;

    @FXML
    private Button timetableBtn;

    @FXML
    private Button exitBtn;

    /**
     * Called to initialize the controller after its root element has been completely processed.
     * Sets up any additional configuration for the navbar component.
     *
     * @param location  The location used to resolve relative paths for the root object
     * @param resources The resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the navbar component
        setupButtonStates();
        configureAccessibility();
        loadStylesheet();
        
        System.out.println("Navbar component initialized successfully");
    }

    /**
     * Loads the component-specific CSS stylesheet.
     */
    private void loadStylesheet() {
        try {
            URL cssUrl = getClass().getResource("/com/example/cab302a1/components/Navbar.css");
            if (cssUrl != null) {
                navbarContainer.getStylesheets().add(cssUrl.toExternalForm());
                System.out.println("Navbar stylesheet loaded successfully");
            } else {
                System.out.println("Warning: Navbar.css not found");
            }
        } catch (Exception e) {
            System.err.println("Error loading navbar stylesheet: " + e.getMessage());
        }
    }

    /**
     * Sets up initial button states and visual indicators.
     * Currently sets Home as the active/selected button by default.
     */
    private void setupButtonStates() {
        // Set Home button as the default active button
        setActiveButton(homeBtn);
    }

    /**
     * Configures accessibility features for the navbar buttons.
     * Sets appropriate ARIA labels and keyboard navigation support.
     */
    private void configureAccessibility() {
        homeBtn.setAccessibleText("Navigate to Home page");
        reviewBtn.setAccessibleText("Navigate to Review page");
        timetableBtn.setAccessibleText("Navigate to Timetable page");
        exitBtn.setAccessibleText("Exit the application");
    }

    /**
     * Sets the specified button as the active/selected navigation button.
     * Updates visual styling to indicate the current page.
     *
     * @param activeButton The button to mark as active
     */
    private void setActiveButton(Button activeButton) {
        // Remove active styling from all buttons
        homeBtn.getStyleClass().removeAll("navbar-button-active");
        reviewBtn.getStyleClass().removeAll("navbar-button-active");
        timetableBtn.getStyleClass().removeAll("navbar-button-active");
        
        // Add active styling to the specified button
        if (activeButton != null && activeButton != exitBtn) {
            activeButton.getStyleClass().add("navbar-button-active");
        }
    }

    /**
     * Handles the Home button click event.
     * Navigates to the user's role-specific home page and updates the active button state.
     *
     * @param event The action event triggered by clicking the Home button
     */
    @FXML
    private void handleHomeAction(ActionEvent event) {
        System.out.println("Navigation: Home button clicked");
        setActiveButton(homeBtn);
        
        try {
            navigateToUserHome();
        } catch (IOException e) {
            System.err.println("Error navigating to home page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the Review button click event.
     * Navigates to the review page and updates the active button state.
     *
     * @param event The action event triggered by clicking the Review button
     */
    @FXML
    private void handleReviewAction(ActionEvent event) {
        System.out.println("Navigation: Review button clicked");
        setActiveButton(reviewBtn);
        
        // TODO: Implement actual navigation to Review page
        // Example: SceneManager.switchToReview();
        navigateToPage("Review");
    }

    /**
     * Handles the Timetable button click event.
     * Navigates to the timetable page and updates the active button state.
     *
     * @param event The action event triggered by clicking the Timetable button
     */
    @FXML
    private void handleTimetableAction(ActionEvent event) {
        System.out.println("Navigation: Timetable button clicked");
        setActiveButton(timetableBtn);
        
        // TODO: Implement actual navigation to Timetable page
        // Example: SceneManager.switchToTimetable();
        navigateToPage("Timetable");
    }

    /**
     * Handles the EXIT button click event.
     * Opens the logout confirmation page for user to confirm or cancel logout.
     *
     * @param event The action event triggered by clicking the EXIT button
     */
    @FXML
    private void handleExitAction(ActionEvent event) {
        System.out.println("Navigation: EXIT button clicked - opening logout confirmation");
        
        try {
            // Navigate to logout confirmation page
            navigateToLogoutConfirmation();
            
        } catch (IOException e) {
            System.err.println("Error opening logout confirmation page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Navigates to the user's role-specific home page.
     * Uses NavigationManager to ensure proper navigation history and CSS loading.
     *
     * @throws IOException if the home page cannot be loaded
     */
    private void navigateToUserHome() throws IOException {
        Stage currentStage = (Stage) homeBtn.getScene().getWindow();
        NavigationManager navigationManager = NavigationManager.getInstance();
        
        // Check if user is logged in
        if (Session.isLoggedaIn()) {
            // Navigate to role-specific home page
            navigationManager.navigateTo(currentStage, NavigationManager.Pages.HOME);
            
            // Update title based on user role
            User currentUser = Session.getCurrentUser();
            if (currentUser != null) {
                String roleTitle = currentUser.getRole();
                currentStage.setTitle("Interactive Quiz Creator - " + roleTitle + " Home");
                System.out.println("Successfully navigated to " + roleTitle + " home page");
            }
        } else {
            // No user logged in, redirect to login page
            navigationManager.navigateTo(currentStage, NavigationManager.Pages.LOGIN);
            System.out.println("No user logged in, redirected to login page");
        }
    }
    
    /**
     * Helper method to simulate navigation to different pages.
     * In a full implementation, this would integrate with a scene manager or router.
     *
     * @param pageName The name of the page to navigate to
     */
    private void navigateToPage(String pageName) {
        System.out.println("Navigating to: " + pageName + " page");
        
        // TODO: Integrate with actual navigation system
        // This could involve:
        // 1. Loading new FXML files
        // 2. Updating the main content area
        // 3. Managing application state
        // 4. Handling navigation history
    }

    /**
     * Public method to programmatically set the active navigation button.
     * Useful for external components that need to update the navbar state.
     *
     * @param buttonName The name of the button to activate ("Home", "Review", "Timetable")
     */
    public void setActiveNavigation(String buttonName) {
        switch (buttonName.toLowerCase()) {
            case "home":
                setActiveButton(homeBtn);
                break;
            case "review":
                setActiveButton(reviewBtn);
                break;
            case "timetable":
                setActiveButton(timetableBtn);
                break;
            default:
                System.out.println("Warning: Unknown navigation button name: " + buttonName);
        }
    }

    /**
     * Navigates to the logout confirmation page using the NavigationManager.
     * This method handles the scene transition when the EXIT button is clicked.
     *
     * @throws IOException if the logout confirmation FXML file cannot be loaded
     */
    private void navigateToLogoutConfirmation() throws IOException {
        // Get the current stage
        Stage currentStage = (Stage) exitBtn.getScene().getWindow();
        
        // Use NavigationManager to navigate to logout confirmation
        // This will automatically save the current page to history
        NavigationManager navigationManager = NavigationManager.getInstance();
        navigationManager.navigateTo(currentStage, NavigationManager.Pages.LOGOUT_CONFIRMATION);
        
        System.out.println("Successfully navigated to logout confirmation page");
    }

    /**
     * Gets the navbar container VBox for external manipulation if needed.
     *
     * @return The main navbar container VBox
     */
    public VBox getNavbarContainer() {
        return navbarContainer;
    }
}

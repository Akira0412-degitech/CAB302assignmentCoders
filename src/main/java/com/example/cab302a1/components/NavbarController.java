package com.example.cab302a1.components;

import com.example.cab302a1.util.Session;
import com.example.cab302a1.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
 * - Application exit functionality
 * (Timetable page navigation - commented out, no longer needed)
 * 
 * @author CAB302 Assignment Team
 * @version 1.0
 */
public class NavbarController implements Initializable {

    // Static reference to current navbar instance for external updates
    private static NavbarController currentInstance;

    // FXML injected components
    @FXML
    private VBox navbarContainer;

    @FXML
    private Button homeBtn;

    @FXML
    private Button reviewBtn;

    // Timetable button commented out - no longer needed for this project
    // @FXML
    // private Button timetableBtn;

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
        // Set this as the current instance
        currentInstance = this;
        
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
     * Sets Home as the default active button.
     */
    private void setupButtonStates() {
        // Set Home button as the default active button
        setActiveButton(homeBtn);
    }
    
    /**
     * Public method to update button states from external navigation.
     * This can be called by NavigationManager or other components when navigation occurs.
     */
    public void updateButtonState(String pageType) {
        switch (pageType.toLowerCase()) {
            case "home":
                setActiveButton(homeBtn);
                break;
            case "review":
                setActiveButton(reviewBtn);
                break;
            // Timetable button commented out - no longer needed
            // case "timetable":
            //     setActiveButton(timetableBtn);
            //     break;
            default:
                setActiveButton(homeBtn);
                break;
        }
    }
    
    /**
     * Static method to update navbar button state from external controllers.
     * This allows page controllers to update the navbar when they load.
     */
    public static void updateNavbarState(String pageType) {
        if (currentInstance != null) {
            currentInstance.updateButtonState(pageType);
        }
    }

    /**
     * Configures accessibility features for the navbar buttons.
     * Sets appropriate ARIA labels and keyboard navigation support.
     */
    private void configureAccessibility() {
        homeBtn.setAccessibleText("Navigate to Home page");
        reviewBtn.setAccessibleText("Navigate to Review page");
        // Timetable button commented out - no longer needed
        // timetableBtn.setAccessibleText("Navigate to Timetable page");
        exitBtn.setAccessibleText("Exit the application");
    }

    /**
     * Sets the specified button as the active/selected navigation button.
     * Updates visual styling to indicate the current page.
     *
     * @param activeButton The button to mark as active
     */
    private void setActiveButton(Button activeButton) {
        // Reset all buttons to their default state
        resetButtonToDefault(homeBtn);
        resetButtonToDefault(reviewBtn);
        // Timetable button commented out - no longer needed
        // resetButtonToDefault(timetableBtn);
        
        // Add active styling to the specified button
        if (activeButton != null && activeButton != exitBtn) {
            activeButton.getStyleClass().add("navbar-button-active");
        }
    }
    
    /**
     * Sets the active button and clears focus (for user interactions, not initialization).
     *
     * @param activeButton The button to mark as active
     */
    private void setActiveButtonWithFocusClear(Button activeButton) {
        // Reset all buttons to their default state
        resetButtonToDefault(homeBtn);
        resetButtonToDefault(reviewBtn);
        // Timetable button commented out - no longer needed
        // resetButtonToDefault(timetableBtn);
        
        // Clear focus from all buttons to prevent :focused pseudo-class issues
        clearFocusFromAllButtons();
        
        // Add active styling to the specified button
        if (activeButton != null && activeButton != exitBtn) {
            activeButton.getStyleClass().add("navbar-button-active");
        }
    }
    
    /**
     * Resets a button to its default state by ensuring proper CSS classes.
     */
    private void resetButtonToDefault(Button button) {
        // Remove active class
        button.getStyleClass().removeAll("navbar-button-active");
        
        // Clear any inline styles that might be stuck
        button.setStyle("");
        
        // Ensure base classes are present (in case they got removed somehow)
        if (!button.getStyleClass().contains("navbar-button")) {
            button.getStyleClass().add("navbar-button");
        }
        if (!button.getStyleClass().contains("navbar-button-secondary")) {
            button.getStyleClass().add("navbar-button-secondary");
        }
        
        // Force a complete style refresh by removing all classes and re-adding them
        java.util.List<String> originalClasses = new java.util.ArrayList<>(button.getStyleClass());
        button.getStyleClass().clear();
        button.getStyleClass().addAll(originalClasses);
    }
    
    /**
     * Clears focus from navigation buttons to prevent :focused pseudo-class styling issues.
     * The :focused state can cause buttons to appear active when they shouldn't be.
     * Note: EXIT button is excluded to maintain its normal behavior.
     */
    private void clearFocusFromAllButtons() {
        // Request focus on the navbar container instead of any button
        if (navbarContainer != null) {
            navbarContainer.requestFocus();
        }
        
        // Only clear focus from navigation buttons (exclude EXIT button)
        homeBtn.setFocusTraversable(false);
        reviewBtn.setFocusTraversable(false);
        // Timetable button commented out - no longer needed
        // timetableBtn.setFocusTraversable(false);
        // Note: exitBtn is intentionally excluded to maintain its normal state
        
        // Re-enable focus traversal after a short delay
        javafx.application.Platform.runLater(() -> {
            homeBtn.setFocusTraversable(true);
            reviewBtn.setFocusTraversable(true);
            // Timetable button commented out - no longer needed
            // timetableBtn.setFocusTraversable(true);
            // Note: exitBtn remains unaffected
        });
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
        setActiveButtonWithFocusClear(homeBtn);
        
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
     * @throws IOException if the FXML file cannot be loaded
     */
    @FXML
    private void handleReviewAction(ActionEvent event) throws IOException {
        System.out.println("Review button clicked. Checking user role...");
        
        // Set active button state first, before navigation (with focus clearing)
        setActiveButtonWithFocusClear(reviewBtn);

        // Determine which review page to navigate to based on user role


        if (Session.isTeacher()) {
            System.out.println("Navigating to Teacher Review Page...");
            NavigationManager.getInstance().navigateTo(
                    (Stage) ((Node) event.getSource()).getScene().getWindow(),
                    NavigationManager.Pages.TEACHER_REVIEW
            );
        } else if(Session.isStudent()){
            System.out.println("Navigating to Student Review Page...");
            NavigationManager.getInstance().navigateTo(
                    (Stage) ((Node) event.getSource()).getScene().getWindow(),
                    NavigationManager.Pages.STUDENT_REVIEW
            );
        }
    }

    // Timetable button commented out - no longer needed for this project
    /**
     * Handles the Timetable button click event.
     * Navigates to the timetable page and updates the active button state.
     *
     * @param event The action event triggered by clicking the Timetable button
     */


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
        

    }

    /**
     * Public method to programmatically set the active navigation button.
     * Useful for external components that need to update the navbar state.
     *
     * @param buttonName The name of the button to activate ("Home", "Review")
     */
    public void setActiveNavigation(String buttonName) {
        switch (buttonName.toLowerCase()) {
            case "home":
                setActiveButton(homeBtn);
                break;
            case "review":
                setActiveButton(reviewBtn);
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

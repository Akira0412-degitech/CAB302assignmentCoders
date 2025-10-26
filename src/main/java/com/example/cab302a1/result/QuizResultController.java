package com.example.cab302a1.result;

import com.example.cab302a1.components.NavigationManager;
import com.example.cab302a1.dao.AttemptDao;
import com.example.cab302a1.dao.DaoFactory;
import com.example.cab302a1.ui.page.review.result.StudentResultDetailController;
import com.example.cab302a1.util.Session;
import com.example.cab302a1.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Controller class for the Quiz Result page.
 * Handles display of quiz results and provides integration points for team members.
 * 
 * This controller provides functionality for:
 * - Displaying quiz scores and results
 * - Navigation to answer review page
 * - Navigation back to home page
 * - Team integration interfaces for easy connection
 */
public class QuizResultController implements Initializable {

    // FXML injected components
    @FXML
    private Label scoreLabel;

    @FXML
    private Label percentageLabel;

    @FXML
    private Label gradeLabel;

    @FXML
    private Button viewAnswerBtn;

    @FXML
    private Button backHomeBtn;

    @FXML
    private VBox additionalScoreInfo;

    @FXML
    private VBox quizInfoSection;

    @FXML
    private Label quizTitleLabel;

    @FXML
    private Label completionTimeLabel;

    // Instance variables
    private QuizResultData currentQuizResult;
    private AnswerReviewHandler answerReviewHandler;
    private HomeNavigationHandler homeNavigationHandler;
    private QuizResultService quizResultService;

    /**
     * Interface for handling answer review navigation.
     * Team members implementing answer review functionality should implement this interface.
     */
    @FunctionalInterface
    public interface AnswerReviewHandler {
        /**
         * Called when user clicks "View the answer" button.
         * @param stage Current stage for scene transitions
         * @param quizResultData The quiz result data
         */
        void showAnswerReview(Stage stage, QuizResultData quizResultData);
    }

    /**
     * Interface for handling home navigation.
     * Team members can implement custom home navigation logic.
     */
    @FunctionalInterface
    public interface HomeNavigationHandler {
        /**
         * Called when user clicks "Back to home page" button.
         * @param stage Current stage for scene transitions
         */
        void navigateToHome(Stage stage);
    }

    /**
     * Called to initialize the controller after its root element has been completely processed.
     * Sets up default configuration and accessibility features.
     *
     * @param location  The location used to resolve relative paths for the root object
     * @param resources The resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize database service
        this.quizResultService = new QuizResultService();
        
        // Initialize with default values
        setupDefaultDisplay();
        setupAccessibility();
        loadStylesheet();
        
        // Register this page with NavigationManager to ensure proper CSS loading
        NavigationManager.getInstance().setCurrentPage(NavigationManager.Pages.QUIZ_RESULT);
    }

    /**
     * Loads the component-specific CSS stylesheet.
     */
    private void loadStylesheet() {
        // CSS will be loaded by the scene creator
        // Verify resource exists for debugging purposes
        URL cssUrl = getClass().getResource("/com/example/cab302a1/result/QuizResult.css");
    }

    /**
     * Sets up default display values for testing.
     */
    private void setupDefaultDisplay() {
        if (currentQuizResult == null) {
            // Use sample data for testing
            setQuizResult(new QuizResultData(13, 20, "Sample Quiz", 1, 1));
        }
    }

    /**
     * Configures accessibility features for the quiz result page.
     */
    private void setupAccessibility() {
        viewAnswerBtn.setAccessibleText("View the answers for this quiz");
        backHomeBtn.setAccessibleText("Return to the home page");
        scoreLabel.setAccessibleText("Quiz score display");
        
        // Set default focus to back home button for safety
        backHomeBtn.requestFocus();
    }

    /**
     * Sets the quiz result data and updates the display.
     * This is the main integration point for team members.
     *
     * @param quizResultData The quiz result data to display
     */
    public void setQuizResult(QuizResultData quizResultData) {
        if (quizResultData == null) {
            System.err.println("Warning: Null quiz result data provided");
            return;
        }

        if (!quizResultData.isValid()) {
            System.err.println("Warning: Invalid quiz result data provided");
            return;
        }

        this.currentQuizResult = quizResultData;
        updateDisplay();
        
        // Register this page with NavigationManager
        NavigationManager.getInstance().setCurrentPage(NavigationManager.Pages.QUIZ_RESULT);
    }

    /**
     * Updates the display with current quiz result data.
     */
    private void updateDisplay() {
        if (currentQuizResult == null) return;

        // Update score display
        scoreLabel.setText(currentQuizResult.getScoreString());
        percentageLabel.setText(String.format("%.1f%%", currentQuizResult.getScorePercentage()));
        gradeLabel.setText("Grade: " + currentQuizResult.getLetterGrade());

        // Update quiz information
        quizTitleLabel.setText(currentQuizResult.getQuizTitle());
        completionTimeLabel.setText("Completed: " + 
            currentQuizResult.getCompletionTime().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")));

        // Show additional information
        additionalScoreInfo.setVisible(true);
        quizInfoSection.setVisible(true);

        // Apply visual styling based on score
        applyScoreBasedStyling();
    }

    /**
     * Applies different styling based on the score achieved.
     */
    private void applyScoreBasedStyling() {
        if (currentQuizResult == null) return;

        // Remove existing style classes
        scoreLabel.getStyleClass().removeAll("score-success", "score-warning");

        // Apply appropriate styling
        if (currentQuizResult.getScorePercentage() >= 80) {
            scoreLabel.getStyleClass().add("score-success");
        } else if (currentQuizResult.getScorePercentage() < 60) {
            scoreLabel.getStyleClass().add("score-warning");
        }
    }

    /**
     * Handles the "View the answer" button click event.
     * Delegates to the answer review handler if set, otherwise opens the result detail page.
     *
     * @param event The action event triggered by clicking the button
     */
    @FXML
    private void handleViewAnswerAction(ActionEvent event) {
        if (answerReviewHandler != null && currentQuizResult != null) {
            try {
                Stage currentStage = (Stage) viewAnswerBtn.getScene().getWindow();
                answerReviewHandler.showAnswerReview(currentStage, currentQuizResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (currentQuizResult != null) {
            // Default behavior - open the result detail page
            try {
                Stage currentStage = (Stage) viewAnswerBtn.getScene().getWindow();
                int quizId = currentQuizResult.getQuizId();
                int userId = currentQuizResult.getUserId();
                
                // Get the attempt ID
                AttemptDao attemptDao = DaoFactory.getAttemptDao();
                Integer attemptId = attemptDao.getAttemptId(quizId, userId);
                
                if (attemptId != null) {
                    StudentResultDetailController.open(currentStage, quizId, attemptId, null);
                } else {
                    showPlaceholderMessage("No quiz attempt found");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showPlaceholderMessage("No quiz data available");
        }
    }

    /**
     * Handles the "Back to home page" button click event.
     * Delegates to the home navigation handler if set, otherwise provides default navigation.
     *
     * @param event The action event triggered by clicking the button
     */
    @FXML
    private void handleBackHomeAction(ActionEvent event) {
        if (homeNavigationHandler != null) {
            try {
                Stage currentStage = (Stage) backHomeBtn.getScene().getWindow();
                homeNavigationHandler.navigateToHome(currentStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Default behavior - navigate to navbar demo
            try {
                navigateToDefaultHome();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Default navigation to home page (role-specific home page).
     * Navigates to the proper home page based on user role, or navbar demo if no user is logged in.
     *
     * @throws IOException if the home page cannot be loaded
     */
    private void navigateToDefaultHome() throws IOException {
        Stage currentStage = (Stage) backHomeBtn.getScene().getWindow();
        
        // Check if user is logged in and navigate to appropriate home page
        if (Session.isLoggedaIn()) {
            // Navigate to role-specific home page using NavigationManager
            NavigationManager navigationManager = NavigationManager.getInstance();
            navigationManager.navigateToReplace(currentStage, NavigationManager.Pages.HOME);
            
            // Update title based on user role
            User currentUser = Session.getCurrentUser();
            String roleTitle = currentUser.getRole();
            currentStage.setTitle("Interactive Quiz Creator - " + roleTitle + " Home");
        } else {
            // No user logged in, fallback to navbar demo
            NavigationManager navigationManager = NavigationManager.getInstance();
            navigationManager.navigateToReplace(currentStage, NavigationManager.Pages.NAVBAR_DEMO);
        }
    }

    /**
     * Shows a placeholder message for unimplemented functionality.
     * Useful for development and team integration testing.
     *
     * @param message The message to display
     */
    private void showPlaceholderMessage(String message) {
        // In a full implementation, this could show a dialog or toast notification
    }

    // ============================================
    // Database Integration Methods (NEW)
    // ============================================
    
    /**
     * Sets quiz result data by fetching from database using AttemptDao.getScore().
     * This method uses the quiz_id and user_id to fetch the actual score from database.
     * 
     * @param quizId The ID of the quiz
     * @param userId The ID of the user
     * @throws QuizResultService.QuizResultException if data cannot be fetched from database
     */
    public void setQuizResultFromDatabase(int quizId, int userId) throws QuizResultService.QuizResultException {
        QuizResultData resultData = quizResultService.getQuizResult(quizId, userId);
        setQuizResult(resultData);
    }
    
    /**
     * Sets quiz result data for the currently logged-in user.
     * Uses Session.getCurrentUser() to get the current user ID.
     * 
     * @param quizId The ID of the quiz
     * @throws QuizResultService.QuizResultException if no user is logged in or data cannot be fetched
     */
    public void setQuizResultFromDatabaseForCurrentUser(int quizId) throws QuizResultService.QuizResultException {
        QuizResultData resultData = quizResultService.getQuizResultForCurrentUser(quizId);
        setQuizResult(resultData);
    }
    
    /**
     * Static method for showing quiz results directly from database.
     * Team members can use this method to navigate from their quiz completion logic.
     * 
     * @param stage The current stage
     * @param quizId The ID of the quiz
     * @param userId The ID of the user
     * @throws IOException if the quiz result FXML cannot be loaded
     * @throws QuizResultService.QuizResultException if database data cannot be fetched
     */
    public static void showQuizResultFromDatabase(Stage stage, int quizId, int userId) 
            throws IOException, QuizResultService.QuizResultException {
        
        FXMLLoader fxmlLoader = new FXMLLoader(QuizResultController.class.getResource("/com/example/cab302a1/result/QuizResult.fxml"));
        Scene resultScene = new Scene(fxmlLoader.load(), 1000, 650);
        
        // Load CSS stylesheet
        URL cssUrl = QuizResultController.class.getResource("/com/example/cab302a1/result/QuizResult.css");
        if (cssUrl != null) {
            resultScene.getStylesheets().add(cssUrl.toExternalForm());
        }
        
        // Get the controller and set the quiz result data from database
        QuizResultController controller = fxmlLoader.getController();
        controller.setQuizResultFromDatabase(quizId, userId);
        
        // Update the stage
        stage.setTitle("Interactive Quiz Creator - Quiz Results");
        stage.setScene(resultScene);
        stage.setResizable(true);
        stage.centerOnScreen();
    }
    
    /**
     * Static method for showing quiz results for the current user from database.
     * 
     * @param stage The current stage
     * @param quizId The ID of the quiz
     * @throws IOException if the quiz result FXML cannot be loaded
     * @throws QuizResultService.QuizResultException if database data cannot be fetched
     */
    public static void showQuizResultFromDatabaseForCurrentUser(Stage stage, int quizId) 
            throws IOException, QuizResultService.QuizResultException {
        
        FXMLLoader fxmlLoader = new FXMLLoader(QuizResultController.class.getResource("/com/example/cab302a1/result/QuizResult.fxml"));
        Scene resultScene = new Scene(fxmlLoader.load(), 1000, 650);
        
        // Load CSS stylesheet
        URL cssUrl = QuizResultController.class.getResource("/com/example/cab302a1/result/QuizResult.css");
        if (cssUrl != null) {
            resultScene.getStylesheets().add(cssUrl.toExternalForm());
        }
        
        // Get the controller and set the quiz result data from database
        QuizResultController controller = fxmlLoader.getController();
        controller.setQuizResultFromDatabaseForCurrentUser(quizId);
        
        // Update the stage
        stage.setTitle("Interactive Quiz Creator - Quiz Results");
        stage.setScene(resultScene);
        stage.setResizable(true);
        stage.centerOnScreen();
    }

    // ============================================
    // Team Integration Methods
    // ============================================

    /**
     * Sets the answer review handler for team integration.
     * Team members implementing answer review should call this method.
     *
     * @param handler The answer review handler implementation
     */
    public void setAnswerReviewHandler(AnswerReviewHandler handler) {
        this.answerReviewHandler = handler;
    }

    /**
     * Sets the home navigation handler for team integration.
     * Team members can customize home navigation by calling this method.
     *
     * @param handler The home navigation handler implementation
     */
    public void setHomeNavigationHandler(HomeNavigationHandler handler) {
        this.homeNavigationHandler = handler;
    }

    /**
     * Static method for easy scene navigation to quiz result page.
     * Team members can use this method to navigate from their quiz completion logic.
     *
     * @param stage The current stage
     * @param quizResultData The quiz result data to display
     * @throws IOException if the quiz result FXML cannot be loaded
     */
    public static void showQuizResult(Stage stage, QuizResultData quizResultData) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(QuizResultController.class.getResource("/com/example/cab302a1/result/QuizResult.fxml"));
        Scene resultScene = new Scene(fxmlLoader.load(), 1000, 650);
        
        // Load CSS stylesheet using NavigationManager approach
        URL cssUrl = QuizResultController.class.getResource("/com/example/cab302a1/result/QuizResult.css");
        if (cssUrl != null) {
            resultScene.getStylesheets().add(cssUrl.toExternalForm());
        }
        
        // Get the controller and set the quiz result data
        QuizResultController controller = fxmlLoader.getController();
        controller.setQuizResult(quizResultData);
        
        // Update the stage
        stage.setTitle("Interactive Quiz Creator - Quiz Results");
        stage.setScene(resultScene);
        stage.setResizable(true);
        stage.centerOnScreen();
    }

    /**
     * Convenience method for showing quiz results with basic score data.
     * Useful for quick integration during development.
     *
     * @param stage The current stage
     * @param correctAnswers Number of correct answers
     * @param totalQuestions Total number of questions
     * @throws IOException if the quiz result page cannot be loaded
     */
    public static void showQuizResult(Stage stage, int correctAnswers, int totalQuestions) throws IOException {
        QuizResultData resultData = new QuizResultData(correctAnswers, totalQuestions);
        showQuizResult(stage, resultData);
    }

    // ============================================
    // Getter Methods for Integration
    // ============================================

    /**
     * Gets the current quiz result data.
     * Useful for team members who need access to the data.
     *
     * @return The current quiz result data, or null if not set
     */
    public QuizResultData getCurrentQuizResult() {
        return currentQuizResult;
    }

    /**
     * Checks if answer review handler is registered.
     * Useful for conditional UI behavior.
     *
     * @return true if answer review handler is set
     */
    public boolean hasAnswerReviewHandler() {
        return answerReviewHandler != null;
    }

    /**
     * Checks if home navigation handler is registered.
     * Useful for conditional UI behavior.
     *
     * @return true if home navigation handler is set
     */
    public boolean hasHomeNavigationHandler() {
        return homeNavigationHandler != null;
    }
}

package com.example.cab302a1.result;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * Standalone demonstration application for the Quiz Result page.
 * This class provides a way to run and test the quiz result page independently.
 * 
 * Usage:
 * - Run this class directly to see the quiz result page
 * - Test different score scenarios
 * - Verify navbar integration
 * - Demonstrate team integration capabilities
 * 
 * @author CAB302 Assignment Team
 * @version 1.0
 */
public class QuizResultDemo extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize database connection (if available)
            initializeDatabase();
            
            // Show demo selection dialog
            showDemoSelectionDialog(primaryStage);
            
        } catch (Exception e) {
            System.err.println("Error starting Quiz Result Demo: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback to default demo
            showDefaultDemo(primaryStage);
        }
    }

    /**
     * Shows a dialog for selecting different demo scenarios.
     * Allows testing various score conditions.
     */
    private void showDemoSelectionDialog(Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quiz Result Demo");
        alert.setHeaderText("Select Demo Scenario");
        alert.setContentText("Choose a demo scenario to test:");
        
        // Create custom buttons for different scenarios
        ButtonType perfectScore = new ButtonType("Perfect Score (20/20)");
        ButtonType goodScore = new ButtonType("Good Score (13/20)");
        ButtonType lowScore = new ButtonType("Low Score (1/2)");
        ButtonType failingScore = new ButtonType("Failing Score (8/20)");
        ButtonType customScore = new ButtonType("Random Score");
        
        alert.getButtonTypes().setAll(perfectScore, goodScore, lowScore, failingScore, customScore);
        
        alert.showAndWait().ifPresent(buttonType -> {
            try {
                if (buttonType == perfectScore) {
                    showQuizResultDemo(primaryStage, new QuizResultData(20, 20, "Mathematics Final Exam", 101, 1));
                } else if (buttonType == goodScore) {
                    showQuizResultDemo(primaryStage, new QuizResultData(13, 20, "Science Quiz", 102, 1));
                } else if (buttonType == lowScore) {
                    showQuizResultDemo(primaryStage, new QuizResultData(1, 2, "Quick Assessment", 103, 1));
                } else if (buttonType == failingScore) {
                    showQuizResultDemo(primaryStage, new QuizResultData(8, 20, "History Test", 104, 1));
                } else if (buttonType == customScore) {
                    showRandomScoreDemo(primaryStage);
                }
            } catch (Exception e) {
                System.err.println("Error showing selected demo: " + e.getMessage());
                showDefaultDemo(primaryStage);
            }
        });
    }

    /**
     * Shows a demo with a randomly generated score.
     */
    private void showRandomScoreDemo(Stage primaryStage) {
        try {
            // Generate random score data
            QuizResultData[] sampleData = QuizResultData.getSampleData();
            int randomIndex = (int) (Math.random() * sampleData.length);
            QuizResultData randomResult = sampleData[randomIndex];
            
            showQuizResultDemo(primaryStage, randomResult);
            
        } catch (Exception e) {
            System.err.println("Error generating random demo: " + e.getMessage());
            showDefaultDemo(primaryStage);
        }
    }

    /**
     * Shows the quiz result page with the provided data.
     * Sets up integration handlers for demonstration.
     */
    private void showQuizResultDemo(Stage primaryStage, QuizResultData quizResultData) {
        try {
            // Show the quiz result page
            QuizResultController.showQuizResult(primaryStage, quizResultData);
            
            // Optional: Setup custom integration handlers for demo
            setupDemoIntegrationHandlers(primaryStage);
            
            System.out.println("Quiz Result Demo started successfully");
            System.out.println("Demo data: " + quizResultData.toString());
            
        } catch (Exception e) {
            System.err.println("Error showing quiz result demo: " + e.getMessage());
            e.printStackTrace();
            showErrorDialog("Failed to load quiz result page", e.getMessage());
        }
    }

    /**
     * Shows the default demo with sample data.
     */
    private void showDefaultDemo(Stage primaryStage) {
        try {
            QuizResultData defaultData = new QuizResultData(13, 20, "Demo Quiz", 999, 1);
            showQuizResultDemo(primaryStage, defaultData);
        } catch (Exception e) {
            System.err.println("Critical error: Cannot show default demo: " + e.getMessage());
            showErrorDialog("Critical Error", "Cannot start Quiz Result Demo");
        }
    }

    /**
     * Sets up demonstration integration handlers.
     * Shows how team members can integrate their components.
     */
    private void setupDemoIntegrationHandlers(Stage primaryStage) {
        // Note: In a real integration, team members would get the controller instance
        // and set their handlers. This is just for demonstration.
        
        System.out.println("Setting up demo integration handlers...");
        
        // Example of how answer review integration would work:
        // controller.setAnswerReviewHandler((stage, data) -> {
        //     System.out.println("Answer review requested for: " + data.getQuizTitle());
        //     // Team member's answer review logic would go here
        // });
        
        // Example of how custom home navigation would work:
        // controller.setHomeNavigationHandler((stage) -> {
        //     System.out.println("Custom home navigation requested");
        //     // Team member's home navigation logic would go here
        // });
        
        System.out.println("Demo integration handlers setup completed");
    }

    /**
     * Initializes database connection if available.
     * This is where team members would initialize their database connections.
     */
    private void initializeDatabase() {
        try {
            // Check if database connection is available
            System.out.println("Checking database connection...");
            
            // For demo purposes, we'll just log that database would be initialized here
            System.out.println("Database connection would be initialized here");
            System.out.println("Quiz results would be saved to database in full implementation");
            
        } catch (Exception e) {
            System.out.println("Database not available for demo - using mock data");
        }
    }

    /**
     * Shows an error dialog with the specified message.
     */
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Quiz Result Demo - Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Main method to run the Quiz Result Demo.
     * Team members can run this directly to test the result page.
     */
    public static void main(String[] args) {
        System.out.println("Starting Quiz Result Demo...");
        System.out.println("This demo showcases the quiz result page with various score scenarios");
        System.out.println("Use this to test integration points and visual design");
        
        launch(args);
    }

    /**
     * Utility method for team members to quickly test with custom data.
     * Can be called from other parts of the application for integration testing.
     */
    public static void runQuickDemo(int correctAnswers, int totalQuestions, String quizTitle) {
        System.out.println("Running quick demo with score: " + correctAnswers + "/" + totalQuestions);
        
        Application.launch(QuizResultDemo.class, 
            "--correct=" + correctAnswers, 
            "--total=" + totalQuestions, 
            "--title=" + quizTitle);
    }

    /**
     * Processes command line arguments for custom demo scenarios.
     * Allows running specific test cases from command line.
     */
    @Override
    public void init() throws Exception {
        super.init();
        
        // Process command line parameters
        var params = getParameters().getNamed();
        
        if (params.containsKey("correct") && params.containsKey("total")) {
            System.out.println("Custom demo parameters detected:");
            System.out.println("Correct: " + params.get("correct"));
            System.out.println("Total: " + params.get("total"));
            System.out.println("Title: " + params.getOrDefault("title", "Custom Quiz"));
        }
    }

    /**
     * Cleanup method called when the demo application stops.
     */
    @Override
    public void stop() throws Exception {
        System.out.println("Quiz Result Demo stopped");
        super.stop();
    }
}

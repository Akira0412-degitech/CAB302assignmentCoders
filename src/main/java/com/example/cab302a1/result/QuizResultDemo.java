package com.example.cab302a1.result;

import com.example.cab302a1.components.NavigationManager;
import com.example.cab302a1.model.User;
import com.example.cab302a1.util.Session;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;

/**
 * Simple demonstration application for the Quiz Result page.
 * Shows the quiz result page with a sample score of 13/20.
 * 
 * @author CAB302 Assignment Team
 * @version 1.0
 */
public class QuizResultDemo extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Set up a mock logged-in user for demo purposes
        setupMockUser();
        
        // Load the Quiz Result FXML
        FXMLLoader fxmlLoader = new FXMLLoader(QuizResultDemo.class.getResource("/com/example/cab302a1/result/QuizResult.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
        
        // Load the CSS stylesheet
        scene.getStylesheets().add(getClass().getResource("/com/example/cab302a1/result/QuizResult.css").toExternalForm());
        
        // Get the controller and try to load from database
        QuizResultController controller = fxmlLoader.getController();
        
        // Try to load from database first, fallback to hardcoded data if database fails
        boolean databaseLoaded = false;
        try {
            // Attempt to load real data from database
            // These are sample IDs - in real usage, these would come from your quiz flow
            controller.setQuizResultFromDatabaseForCurrentUser(1); // quiz_id = 1
            databaseLoaded = true;
            System.out.println("DATABASE INTEGRATION: Successfully loaded quiz result from database using AttemptDao.getScore()");
            System.out.println("Score fetched from quiz_attempts table for current user");
        } catch (QuizResultService.QuizResultException e) {
            // Fallback to sample data if database doesn't have the data
            System.out.println("Database integration available but no data found: " + e.getMessage());
            System.out.println("Falling back to sample data for demo purposes");
            QuizResultData sampleData = new QuizResultData(13, 20, "Mathematics Quiz (Sample)", 123, 1);
            controller.setQuizResult(sampleData);
        }
        
        // Configure and show the stage
        stage.setTitle("Interactive Quiz Creator - Quiz Result Demo" + (databaseLoaded ? " (Database)" : " (Sample)"));
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.show();
        
        // Initialize NavigationManager with this page as the starting point
        NavigationManager.getInstance().setCurrentPage(NavigationManager.Pages.QUIZ_RESULT);
        
        System.out.println("Quiz Result Demo started");
        System.out.println("NavigationManager initialized - EXIT -> Cancel will return to this page");
        if (databaseLoaded) {
            System.out.println("TEAM INTEGRATION: This demo shows how your quiz completion logic should call the database methods");
        }
    }
    
    /**
     * Sets up a mock user session for demo purposes.
     * In real usage, the user would already be logged in through the login system.
     */
    private void setupMockUser() {
        if (!Session.isLoggedaIn()) {
            // Create a mock user for demo purposes
            User mockUser = new User(1, "demo_user", "demo@example.com", "12345", "Student", new Timestamp(System.currentTimeMillis()));
            Session.setCurrentUser(mockUser);
            System.out.println("Mock user session created for demo (ID: 1, Role: Student)");
            System.out.println("In real usage, user would be logged in through LoginController");
        }
    }

    /**
     * Main method to run the Quiz Result Demo.
     * Shows the quiz result page with database integration using AttemptDao.getScore().
     * Falls back to sample data if no database data is available.
     */
    public static void main(String[] args) {
        System.out.println("Starting Quiz Result Demo with Database Integration...");
        System.out.println("Will attempt to load quiz results from database using AttemptDao.getScore()");
        System.out.println("If no database data found, will show sample score: 13/20");
        System.out.println("This demonstrates how your quiz completion logic should integrate");
        launch(args);
    }
}

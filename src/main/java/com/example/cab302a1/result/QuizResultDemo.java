package com.example.cab302a1.result;

import com.example.cab302a1.components.NavigationManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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
        // Load the Quiz Result FXML
        FXMLLoader fxmlLoader = new FXMLLoader(QuizResultDemo.class.getResource("/com/example/cab302a1/result/QuizResult.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
        
        // Load the CSS stylesheet
        scene.getStylesheets().add(getClass().getResource("/com/example/cab302a1/result/QuizResult.css").toExternalForm());
        
        // Get the controller and set sample data
        QuizResultController controller = fxmlLoader.getController();
        QuizResultData sampleData = new QuizResultData(13, 20, "Mathematics Quiz", 123, 1);
        controller.setQuizResult(sampleData);
        
        // Configure and show the stage
        stage.setTitle("Interactive Quiz Creator - Quiz Result Demo");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.show();
        
        // Initialize NavigationManager with this page as the starting point
        NavigationManager.getInstance().setCurrentPage(NavigationManager.Pages.QUIZ_RESULT);
        
        System.out.println("Quiz Result Demo started - showing score: 13/20");
        System.out.println("NavigationManager initialized - EXIT -> Cancel will return to this page");
    }

    /**
     * Main method to run the Quiz Result Demo.
     * Shows the quiz result page with a sample score of 13/20.
     */
    public static void main(String[] args) {
        System.out.println("Starting Quiz Result Demo...");
        System.out.println("This will show the quiz result page with score: 13/20");
        launch(args);
    }
}

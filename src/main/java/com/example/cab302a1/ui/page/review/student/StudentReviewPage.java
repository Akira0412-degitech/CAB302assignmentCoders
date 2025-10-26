package com.example.cab302a1.ui.page.review.student;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX application entry point for the Student Review Page.
 * <p>
 * This class launches the UI where students can review their completed
 * quizzes and see detailed results. It loads the corresponding FXML layout
 * and sets up the main application window.
 * </p>
 */
public class StudentReviewPage extends Application {

    /**
     * Starts the JavaFX application by loading the student review page FXML layout
     * and displaying it in the primary stage.
     *
     * @param stage the primary stage for this application
     * @throws Exception if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/cab302a1/ReviewPage/student-review-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 640);
        stage.setTitle("Interactive Quiz Creator - Student");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches the Student Review Page application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

package com.example.cab302a1.ui.page.review.student;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StudentReviewPage extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/cab302a1/ReviewPage/student-review-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 640);
        stage.setTitle("Interactive Quiz Creator - Student");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package com.example.cab302a1.navigation;

import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.result.QuizResultController;
import com.example.cab302a1.result.QuizResultData;
import com.example.cab302a1.ui.Student.StudentTakeQuizController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.Consumer;

//JavaFX navigator implementation
public class FxQuizNavigator implements QuizNavigator {

    @Override
    public void toStudentTakeQuiz(Stage owner, Quiz quiz, Consumer<List<Integer>> onSubmitted) {
        StudentTakeQuizController.open(owner, quiz, onSubmitted);
    }

    @Override
    public void toQuizResult(Stage owner, QuizResultData data) {
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/com/example/cab302a1/result/QuizResult.fxml"));
            Parent root = fxml.load();
            QuizResultController rc = fxml.getController();
            rc.setQuizResult(data);

            Scene scene = new Scene(root, 1000, 650);
            var cssUrl = getClass().getResource("/com/example/cab302a1/result/QuizResult.css");
            if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());

            owner.setScene(scene);
        } catch (Exception e) {
            throw new RuntimeException("Failed to open QuizResult view", e);
        }
    }

    @Override
    public void toHome(Stage owner) {
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/com/example/cab302a1/ui/HomePage.fxml"));
            Parent root = fxml.load();
            owner.setScene(new Scene(root, 1000, 650));
        } catch (Exception e) {
            throw new RuntimeException("Failed to open Home view", e);
        }
    }
}

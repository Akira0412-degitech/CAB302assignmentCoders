package com.example.cab302a1.navigation;

import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.result.QuizResultData;
import javafx.stage.Stage;
import java.util.List;
import java.util.function.Consumer;

//Navigation abstraction to avoid duplicating FXML loading in controllers.
public interface QuizNavigator {
    void toStudentTakeQuiz(Stage owner, Quiz quiz, Consumer<List<Integer>> onSubmitted);
    void toQuizResult(Stage owner, QuizResultData data);
    void toHome(Stage owner);
}

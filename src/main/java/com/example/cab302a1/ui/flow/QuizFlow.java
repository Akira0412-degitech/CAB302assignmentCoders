package com.example.cab302a1.ui.flow;

import com.example.cab302a1.model.Quiz;
import javafx.stage.Stage;

/**
 * QuizFlow (Strategy)
 * - Role-specific navigation/logic when a quiz card is clicked.
 */
public interface QuizFlow {
    void open(Stage owner, Quiz quiz, Runnable onAfterClose);
}

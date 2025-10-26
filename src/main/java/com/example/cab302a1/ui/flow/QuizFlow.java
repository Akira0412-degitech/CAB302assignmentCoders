package com.example.cab302a1.ui.flow;

import com.example.cab302a1.model.Quiz;
import javafx.stage.Stage;

/**
 * Strategy interface for role-specific quiz navigation and interaction logic.
 * <p>
 * This interface allows different user roles (Student, Teacher) to implement
 * custom behavior when opening or interacting with a quiz. By using the Strategy
 * pattern, the system can easily switch between different quiz flows based on the
 * logged-in user's role without conditional logic in the UI layer.
 * </p>
 */
public interface QuizFlow {
    
    /**
     * Opens the quiz with role-specific behavior.
     * <p>
     * Implementations define what happens when a quiz is opened, such as:
     * </p>
     * <ul>
     *   <li>For students: Check completion status, open quiz-taking UI, or show results</li>
     *   <li>For teachers: Open quiz management and editing interface</li>
     * </ul>
     *
     * @param owner the parent Stage that owns this dialog or window
     * @param quiz the Quiz object to open
     * @param onAfterClose callback to execute after the quiz interaction is complete (may be null)
     */
    void open(Stage owner, Quiz quiz, Runnable onAfterClose);
}

package com.example.cab302a1.ui.flow;

import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.ui.page.teacher.TeacherQuizDetailController;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * Implementation of QuizFlow for teacher users.
 * <p>
 * When a teacher clicks on a quiz card, this flow opens the quiz detail
 * page where teachers can view quiz information, edit questions, and
 * manage quiz settings. This provides the teacher-specific navigation
 * and interaction logic for quiz management.
 * </p>
 */
public class TeacherQuizFlow implements QuizFlow {

    /**
     * Opens the teacher quiz detail view for the specified quiz.
     * <p>
     * This method launches the quiz detail controller, allowing teachers
     * to view and manage quiz details. If an error occurs during opening,
     * an error dialog is displayed to the user.
     * </p>
     *
     * @param owner the parent Stage that owns this dialog
     * @param quiz the Quiz object to display and manage
     * @param onAfterClose callback executed after the detail view is closed (may be null)
     */
    @Override
    public void open(Stage owner, Quiz quiz, Runnable onAfterClose) {
        try {
            TeacherQuizDetailController.open(owner, quiz, updated -> {
                if (onAfterClose != null) onAfterClose.run();
            });
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Open detail failed: " + e.getMessage(), ButtonType.OK)
                    .showAndWait();
        }
    }
}

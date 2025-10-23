package com.example.cab302a1.ui.flow;

import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.ui.page.teacher.TeacherQuizDetailController; // 실제 경로에 맞춰 조정
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;


public class TeacherQuizFlow implements QuizFlow {

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

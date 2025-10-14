package com.example.cab302a1.ui.action;

import com.example.cab302a1.dao.QuizDao;
import com.example.cab302a1.model.Quiz;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.TilePane;

import java.util.Optional;

/**
 * HideQuizAction
 * - Handles the soft-delete (hide) behavior for teacher's '×' button.
 * - Shows confirmation, updates DB (is_hidden=1), and removes the card node on success.
 */
public class HideQuizAction {

    private final QuizDao quizDao;

    public HideQuizAction(QuizDao quizDao) {
        this.quizDao = quizDao;
    }

    /**
     * Confirm and hide the quiz. Removes the card node from the grid on success.
     */
    public boolean confirmAndHide(Quiz quiz, Node cardNode, TilePane grid) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete this quiz?");
        alert.setHeaderText("This quiz will be hidden from the list.//test comment");
        ButtonType ok = new ButtonType("Delete", ButtonType.OK.getButtonData());
        ButtonType cancel = ButtonType.CANCEL;
        alert.getButtonTypes().setAll(ok, cancel);

        Optional<ButtonType> res = alert.showAndWait();
        if (res.isPresent() && res.get() == ok) {
            quizDao.updateQuizStatus(quiz.getQuizId(), true); // Soft delete: is_hidden = 1
            grid.getChildren().remove(cardNode);
            return true;
        }
        return false;
    }
}

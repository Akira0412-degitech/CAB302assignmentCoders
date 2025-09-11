package com.example.cab302a1.ui;

// NEW: use full quiz model
import com.example.cab302a1.model.Quiz;

import com.example.cab302a1.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import com.example.cab302a1.dao.QuizDao;
import com.example.cab302a1.model.Quiz;

/**
 * Shared controller for both Student and Teacher home.
 * Behavior (read-only vs. editable) is toggled by UserRole.
 */
public class HomeController {

    @FXML private TilePane grid;
    // (Optional) If you added a title label in FXML:  <Label fx:id="titleLabel" .../>
    // @FXML private Label titleLabel;


    // CHANGED: Store full Quiz objects instead of just titles
    private final List<Quiz> quizzes = new ArrayList<>();

    @FXML
    public void initialize() {
        // Seed with sample quizzes for preview; later this will come from DB.
//        for (String t : List.of("Java Basics", "OOP Essentials", "Exceptions")) {
//            Quiz q = new Quiz();
//            q.setTitle(t);
//            q.setDescription(""); // optional
//            quizzes.add(q);
//        }
        refresh();
    }

//    // Sets whether this screen is for a STUDENT or a TEACHER
//    public void setRole(UserRole role) {
//        this.role = role;
//    }

    /** Rebuild the grid according to the current role and quiz list. */
    public void refresh() {
        // if (titleLabel != null) titleLabel.setText("Home — " + role);
        grid.getChildren().clear();
        QuizDao quizDao = new QuizDao();

        quizzes.addAll(quizDao.getAllQuizzes());
        for (Quiz q : quizzes) {
            grid.getChildren().add(createQuizCard(q));
        }
        if (Session.isTeacher()) {
            grid.getChildren().add(createPlusCard());
        }
    }

    /** Create a square 'quiz card' button with role-specific action. */
    private Button createQuizCard(Quiz quiz) {
        Button card = new Button(quiz.getTitle());
        card.getStyleClass().add("quiz-card"); // CSS: .quiz-card
        card.setPrefSize(160, 160);            // square card
        card.setWrapText(true);                // line wrap for long titles

        card.setOnAction(e -> {
            if (Session.isStudent()) {
                info("Start Quiz", "Selected: " + quiz.getTitle() + "\n(Next: navigate to attempt screen)");
            }else {
                Stage owner = (Stage) grid.getScene().getWindow();
                TeacherQuizDetailController.open(owner, quiz, updated -> refresh());
            }
        });
        return card;
    }

    /** Teacher-only '+' card to open the quiz editor modal. */
    private Button createPlusCard() {
        Button plus = new Button("+");
        plus.getStyleClass().add("plus-card"); // CSS: .plus-card
        plus.setPrefSize(160, 160);
        plus.setOnAction(e -> openCreateQuizEditor());
        return plus;
    }

    /** Open QuizEditor as a modal dialog; when saved, append to list and refresh UI. */
    private void openCreateQuizEditor() {
        Stage owner = (Stage) grid.getScene().getWindow();
        QuizEditorController.open(owner, quiz -> {
            // Receive the created Quiz from the editor via callback
            quizzes.add(quiz);
            refresh(); // re-render grid with the new card
        });
    }

    // (REMOVED) Old title-only dialog method was here:
    // private void onAddQuiz() { ... TextInputDialog ... }

    /** Convenience info dialog. */
    private void info(String header, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(header);
        a.showAndWait();
    }

    /** Convenience warning dialog. */
    private void warn(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }
}

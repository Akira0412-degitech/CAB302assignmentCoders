package com.example.cab302a1.ui;

import com.example.cab302a1.dao.QuizDao;
import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.result.QuizResultController;
import com.example.cab302a1.result.QuizResultData;
import com.example.cab302a1.service.QuizService;
import com.example.cab302a1.util.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared controller for both Student and Teacher home.
 * Behavior (read-only vs. editable) is toggled by UserRole.
 */
public class HomeController {

    @FXML
    private TilePane grid;



    // CHANGED: Store full Quiz objects instead of just titles
    private final List<Quiz> quizzes = new ArrayList<>();

    @FXML
    public void initialize() {
        refresh();
    }


    /** Rebuild the grid according to the current role and quiz list. */
    public void refresh() {
        // if (titleLabel != null) titleLabel.setText("Home — " + role);
        grid.getChildren().clear();
        quizzes.clear();
        QuizDao quizDao = new QuizDao();

        quizzes.addAll(quizDao.getAllQuizzes());
        for (Quiz q : quizzes) {
            grid.getChildren().add(createQuizCard(q));
        }
        if (Session.isTeacher()) {
            grid.getChildren().add(createPlusCard());
        }
    }

//Create a square 'quiz card' button with role-specific action
    private Button createQuizCard(Quiz quiz) {
        Button card = new Button(quiz.getTitle());
        card.getStyleClass().add("quiz-card");
        card.setPrefSize(160, 160);
        card.setWrapText(true);

        card.setOnAction(e -> {
            Stage owner = (Stage) grid.getScene().getWindow();
            handleQuizCardClick(owner, quiz);  // Click action connect sperate method
        });
        return card;
    }

    //When click - S or T using session
    private void handleQuizCardClick(Stage owner, Quiz quiz) {
        if (Session.isStudent()) {
            // before Quiz take (fill quiz data)
            QuizService quizService = new QuizService();
            Quiz fullQuiz = quizService.loadQuizFully(quiz);

            // take quiz - result
            StudentTakeQuizController.open(owner, fullQuiz, selections -> {
                try {
                    int correct = 0;
                    var qs = quiz.getQuestions();
                    for (int i = 0; i < qs.size(); i++) {
                        int selected = (i < selections.size() ? selections.get(i) : -1);
                        int answerIdx = -1;
                        var choices = qs.get(i).getChoices();
                        for (int j = 0; j < choices.size(); j++) {
                            if (choices.get(j).isCorrect()) { answerIdx = j; break; }
                        }
                        if (selected == answerIdx) correct++;
                    }

                    FXMLLoader fxml = new FXMLLoader(getClass().getResource(
                            "/com/example/cab302a1/result/QuizResult.fxml"));
                    Parent root = fxml.load();
                    QuizResultController rc = fxml.getController();

                    int quizId = quiz.getQuizId();
                    int userId = (Session.getCurrentUser() != null) ? Session.getCurrentUser().getUser_id() : 0;

                    rc.setQuizResult(new QuizResultData(
                            correct, qs.size(), quiz.getTitle(), quizId, userId
                    ));

                    owner.setScene(new Scene(root, 1100, 680));
                    owner.setTitle("Quiz Result");
                    owner.show();

                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR,
                            "Failed to open result page:\n" + ex.getMessage(),
                            ButtonType.OK).showAndWait();
                }
            });

        } else {
            // Teacher quiz detail and edit
            TeacherQuizDetailController.open(owner, quiz, updated -> refresh());
        }
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

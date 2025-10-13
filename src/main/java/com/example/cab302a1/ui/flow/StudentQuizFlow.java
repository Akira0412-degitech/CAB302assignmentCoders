package com.example.cab302a1.ui.flow;

import com.example.cab302a1.dao.AttemptDao;
import com.example.cab302a1.dao.ResponseDao;
import com.example.cab302a1.model.QuestionResponse;
import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.result.QuizResultController;
import com.example.cab302a1.result.QuizResultData;
import com.example.cab302a1.service.QuizService;
import com.example.cab302a1.ui.StudentTakeQuizController; // 실제 경로에 맞춰 조정
import com.example.cab302a1.util.Session;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles: completed → result, not-completed → take quiz → submit → result.
 */
public class StudentQuizFlow implements QuizFlow {

    private final AttemptDao attemptDao;
    private final ResponseDao responseDao;
    private final QuizService quizService;

    public StudentQuizFlow(AttemptDao attemptDao, ResponseDao responseDao, QuizService quizService) {
        this.attemptDao = attemptDao;
        this.responseDao = responseDao;
        this.quizService = quizService;
    }

    @Override
    public void open(Stage owner, Quiz quiz, Runnable onAfterClose) {
        try {
            int userId = (Session.getCurrentUser() != null) ? Session.getCurrentUser().getUser_id() : 0;
            int quizId = quiz.getQuizId();

            // If user already completed it, go straight to the results
            if (attemptDao.hasCompleted(quizId, userId)) {
                Integer score = attemptDao.getScore(quizId, userId);
                Quiz fullQuiz = quizService.loadQuizFully(quiz);
                int total = fullQuiz.getQuestions().size();

                FXMLLoader fxml = new FXMLLoader(getClass().getResource(
                        "/com/example/cab302a1/result/QuizResult.fxml"));
                Parent root = fxml.load();
                QuizResultController rc = fxml.getController();

                rc.setQuizResult(new QuizResultData(
                        score != null ? score : 0, total, fullQuiz.getTitle(), quizId, userId
                ));

                Scene resultScene = new Scene(root, 1000, 650);
                java.net.URL cssUrl = getClass().getResource("/com/example/cab302a1/result/QuizResult.css");
                if (cssUrl != null) resultScene.getStylesheets().add(cssUrl.toExternalForm());
                owner.setScene(resultScene);
                if (onAfterClose != null) onAfterClose.run();
                return;
            }

            // Incomplete → Start attempt
            Quiz fullQuiz = quizService.loadQuizFully(quiz);
            int attemptId = attemptDao.startAttempt(quizId, userId);
            if (attemptId <= 0) {
                new Alert(Alert.AlertType.ERROR, "Failed to start attempt.", ButtonType.OK).showAndWait();
                return;
            }

            // Open the quiz screen
            StudentTakeQuizController.open(owner, fullQuiz, selections -> {
                try {
                    List<QuestionResponse> responses = new ArrayList<>();
                    var questions = fullQuiz.getQuestions();

                    for (int i = 0; i < questions.size(); i++) {
                        int selectedIndex = (i < selections.size() ? selections.get(i) : -1);
                        if (selectedIndex < 0) continue;

                        var q = questions.get(i);
                        var choices = q.getChoices();
                        if (selectedIndex >= choices.size()) continue;

                        var chosen = choices.get(selectedIndex);

                        QuestionResponse r = new QuestionResponse();
                        r.setAttempt_id(attemptId);
                        r.setQuestion_id(q.getQuestionId());
                        r.setOption_id(chosen.getOption_id());
                        r.setIs_correct(chosen.isCorrect());
                        responses.add(r);
                    }

                    if (responses.isEmpty()) {
                        new Alert(Alert.AlertType.WARNING,
                                "Please select at least one answer before submitting.").showAndWait();
                        return;
                    }

                    responseDao.saveResponse(attemptId, responses);
                    attemptDao.endAttempt(attemptId);

                    Integer score = attemptDao.getScore(quizId, userId);
                    int total = fullQuiz.getQuestions().size();

                    FXMLLoader fxml = new FXMLLoader(getClass().getResource(
                            "/com/example/cab302a1/result/QuizResult.fxml"));
                    Parent root = fxml.load();
                    QuizResultController rc = fxml.getController();

                    rc.setQuizResult(new QuizResultData(
                            score != null ? score : 0, total, fullQuiz.getTitle(), quizId, userId
                    ));

                    Scene resultScene = new Scene(root, 1000, 650);
                    java.net.URL cssUrl = getClass().getResource("/com/example/cab302a1/result/QuizResult.css");
                    if (cssUrl != null) resultScene.getStylesheets().add(cssUrl.toExternalForm());
                    owner.setScene(resultScene);

                    if (onAfterClose != null) onAfterClose.run();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Failed to submit quiz. " + ex.getMessage())
                            .showAndWait();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Open quiz failed: " + e.getMessage()).showAndWait();
        }
    }
}

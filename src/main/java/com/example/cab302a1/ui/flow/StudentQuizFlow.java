package com.example.cab302a1.ui.flow;

import com.example.cab302a1.dao.AttemptDao;
import com.example.cab302a1.dao.ResponseDao;
import com.example.cab302a1.model.QuestionResponse;
import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.result.QuizResultController;
import com.example.cab302a1.result.QuizResultData;
import com.example.cab302a1.service.QuizService;
import com.example.cab302a1.ui.page.student.StudentTakeQuizController;
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
 * Implementation of QuizFlow for student users.
 * <p>
 * This flow manages the complete student quiz-taking lifecycle including:
 * </p>
 * <ul>
 *   <li>Checking if a quiz has already been completed</li>
 *   <li>Displaying results for completed quizzes</li>
 *   <li>Opening the quiz-taking interface for incomplete quizzes</li>
 *   <li>Saving student responses and calculating scores</li>
 *   <li>Displaying final results after submission</li>
 * </ul>
 * <p>
 * The flow handles: completed quiz → result view, incomplete quiz → take quiz → submit → result view.
 * </p>
 */
public class StudentQuizFlow implements QuizFlow {

    private final AttemptDao attemptDao;
    private final ResponseDao responseDao;
    private final QuizService quizService;

    /**
     * Constructs a StudentQuizFlow with required DAO dependencies.
     *
     * @param attemptDao DAO for managing quiz attempts
     * @param responseDao DAO for storing student responses
     * @param quizService service for loading complete quiz data
     */
    public StudentQuizFlow(AttemptDao attemptDao, ResponseDao responseDao, QuizService quizService) {
        this.attemptDao = attemptDao;
        this.responseDao = responseDao;
        this.quizService = quizService;
    }

    /**
     * Opens a quiz or displays results based on completion status.
     * <p>
     * If the user has already completed this quiz, displays the result page.
     * If not completed, opens the quiz-taking interface.
     * Hidden quizzes are blocked with an informational message.
     * </p>
     *
     * @param owner the parent Stage
     * @param quiz the Quiz to open or show results for
     */
    public void openOrShowResult(Stage owner, Quiz quiz) {
        try {
            // Hidden quiz guard
            if (Boolean.TRUE.equals(quiz.getIsHidden())) {
                new Alert(Alert.AlertType.INFORMATION,
                        "This quiz is not available. Please choose another quiz.",
                        ButtonType.OK).showAndWait();
                return;
            }

            int userId = (Session.getCurrentUser() != null) ? Session.getCurrentUser().getUser_id() : 0;
            boolean completed = attemptDao.hasCompleted(quiz.getQuizId(), userId);
            if (completed) {
                showResult(owner, quiz, userId);
            } else {
                open(owner, quiz, null); // proceed to take quiz
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unable to open quiz: " + ex.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    /**
     * Opens the quiz-taking interface for the student.
     * <p>
     * This method:
     * </p>
     * <ol>
     *   <li>Loads the complete quiz with questions and options</li>
     *   <li>Starts a new quiz attempt in the database</li>
     *   <li>Opens the student quiz-taking UI</li>
     *   <li>Saves responses on submission</li>
     *   <li>Calculates and displays the final result</li>
     * </ol>
     *
     * @param owner the parent Stage
     * @param quiz the Quiz to take
     * @param onFinish callback executed after quiz completion (may be null)
     */
    @Override
    public void open(Stage owner, Quiz quiz, Runnable onFinish) {
        try {
            // Load full quiz (questions + choices)
            Quiz fullQuiz = quizService.loadQuizFully(quiz);

            int userId = (Session.getCurrentUser() != null) ? Session.getCurrentUser().getUser_id() : 0;
            int quizId = quiz.getQuizId();

            // Start attempt
            int attemptId = attemptDao.startAttempt(quizId, userId);
            if (attemptId <= 0) {
                new Alert(Alert.AlertType.ERROR, "Failed to start attempt.", ButtonType.OK).showAndWait();
                return;
            }

            // Open take-quiz UI
            StudentTakeQuizController.open(owner, fullQuiz, selections -> {
                try {
                    // Build responses from user selections
                    List<QuestionResponse> responses = getQuestionResponses(selections, fullQuiz, attemptId);

                    if (responses.isEmpty()) {
                        new Alert(Alert.AlertType.WARNING,
                                "Please select at least one answer before submitting.").showAndWait();
                        return;
                    }

                    // Save responses & end attempt
                    responseDao.saveResponse(attemptId, responses);
                    attemptDao.endAttempt(attemptId);

                    // Show result
                    showResult(owner, fullQuiz, userId);

                    if (onFinish != null) onFinish.run();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Error during submission: " + ex.getMessage(),
                            ButtonType.OK).showAndWait();
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Loading quiz failed: " + ex.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    /**
     * Converts student selections into QuestionResponse objects.
     * <p>
     * Maps the list of selected option indices to QuestionResponse objects
     * containing the attempt ID, question ID, chosen option ID, and correctness.
     * </p>
     *
     * @param selections list of selected option indices (one per question)
     * @param fullQuiz the complete Quiz with questions and options
     * @param attemptId the ID of the current quiz attempt
     * @return list of QuestionResponse objects representing student answers
     */
    private static List<QuestionResponse> getQuestionResponses(List<Integer> selections, Quiz fullQuiz, int attemptId) {
        List<QuestionResponse> responses = new ArrayList<>();
        var questions = fullQuiz.getQuestions();

        for (int i = 0; i < questions.size(); i++) {
            int selectedIndex = (i < selections.size() ? selections.get(i) : -1);
            if (selectedIndex < 0) continue; // unanswered

            var q = questions.get(i);
            var choices = q.getChoices();
            if (selectedIndex >= choices.size()) continue;

            var chosen = choices.get(selectedIndex);

            QuestionResponse r = new QuestionResponse(attemptId, q.getQuestionId(), chosen.getOption_id(),chosen.isCorrect() );

            responses.add(r);
        }
        return responses;
    }

    /**
     * Displays the quiz result screen for the student.
     * <p>
     * Fetches the student's score from the database and displays it
     * along with quiz metadata in a formatted result view.
     * </p>
     *
     * @param owner the parent Stage
     * @param quiz the Quiz that was completed
     * @param userId the ID of the student user
     */
    private void showResult(Stage owner, Quiz quiz, int userId) {
        try {
            Integer score = attemptDao.getScore(quiz.getQuizId(), userId);
            Quiz full = quizService.loadQuizFully(quiz);
            int total = full.getQuestions().size();

            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/com/example/cab302a1/result/QuizResult.fxml"));
            Parent root = fxml.load();
            QuizResultController rc = fxml.getController();
            rc.setQuizResult(new QuizResultData(score != null ? score : 0, total, full.getTitle(),
                    quiz.getQuizId(), userId));

            Scene scene = new Scene(root, 1000, 650);
            var cssUrl = getClass().getResource("/com/example/cab302a1/result/QuizResult.css");
            if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());
            owner.setScene(scene);

        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading quiz results: " + ex.getMessage(), ButtonType.OK).showAndWait();
        }
    }
}
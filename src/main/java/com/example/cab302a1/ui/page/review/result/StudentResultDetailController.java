package com.example.cab302a1.ui.page.review.result;

import com.example.cab302a1.dao.DaoFactory;
import com.example.cab302a1.model.QuizChoiceCreate;
import com.example.cab302a1.result.ResultDetail;
import com.example.cab302a1.result.ResultQuestion;
import com.example.cab302a1.service.QuizResultDetailService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for displaying a student's detailed quiz results.
 * <p>
 * Loads quiz data, questions, answers, and highlights correct and
 * selected responses. Used in the result review page after a student
 * completes a quiz.
 * </p>
 *
 * @since 1.0
 */
public class StudentResultDetailController {

    /** Label displaying quiz title. */
    @FXML private Label titleLabel;

    /** Label displaying quiz description. */
    @FXML private Label descriptionLabel;

    /** Container for dynamically loaded question items. */
    @FXML private VBox questionsBox;

    /** Button to close the review window. */
    @FXML private Button doneButton;

    /** The stage of this modal dialog. */
    private Stage stage;

    /** Optional callback executed when the done button is pressed. */
    private Runnable onDone;

    /**
     * Opens the result detail page in a modal dialog window.
     *
     * @param owner the parent stage
     * @param quizId the quiz ID
     * @param attemptId the attempt ID
     * @param onDone callback to execute when the done button is clicked
     */
    public static void open(Stage owner, int quizId, int attemptId, Runnable onDone) {
        try {
            FXMLLoader loader = new FXMLLoader(StudentResultDetailController.class.getResource(
                    "/com/example/cab302a1/result/StudentResultDetailPage.fxml"));
            Parent root = loader.load();
            StudentResultDetailController controller = loader.getController();

            Stage stage = new Stage();
            stage.initOwner(owner);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Quiz Results - Detailed Review");

            Scene scene = new Scene(root, 920, 720);

            // Load CSS if available
            java.net.URL cssUrl = StudentResultDetailController.class.getResource(
                    "/com/example/cab302a1/result/StudentResultDetail.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            stage.setScene(scene);

            controller.stage = stage;
            controller.onDone = onDone;
            controller.loadResultDetail(quizId, attemptId);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to open result detail page:\n" + e.getMessage()).showAndWait();
        }
    }

    /**
     * Loads and displays the detailed quiz result for a given attempt.
     * <p>
     * Retrieves data via {@link QuizResultDetailService}, populates
     * question views, and highlights correct/selected answers.
     * </p>
     *
     * @param quizId the quiz ID
     * @param attemptId the attempt ID
     */
    private void loadResultDetail(int quizId, int attemptId) {
        try {
            QuizResultDetailService service = new QuizResultDetailService(
                    DaoFactory.getQuizDao(), DaoFactory.getQuestionDao(), DaoFactory.getOptionDao(), DaoFactory.getResponseDao()
            );

            ResultDetail resultDetail = service.getResultDetail(attemptId, quizId);

            if (resultDetail == null) {
                new Alert(Alert.AlertType.ERROR, "Failed to load quiz results").showAndWait();
                if (stage != null) stage.close();
                return;
            }

            var quiz = resultDetail.getQuiz();
            String title = (quiz != null && quiz.getTitle() != null) ? quiz.getTitle() : "Quiz Results";
            String desc  = (quiz != null && quiz.getDescription() != null) ? quiz.getDescription() : "Review your answers below";

            titleLabel.setText(title);
            descriptionLabel.setText(desc);

            // Populate question list
            questionsBox.getChildren().clear();
            List<ResultQuestion> questions = resultDetail.getResultQuestions();

            if (questions == null || questions.isEmpty()) {
                Label noQuestions = new Label("No questions found");
                questionsBox.getChildren().add(noQuestions);
                return;
            }

            int total = questions.size();
            for (int i = 0; i < total; i++) {
                ResultQuestion rq = questions.get(i);

                try {
                    FXMLLoader fxml = new FXMLLoader(getClass().getResource(
                            "/com/example/cab302a1/result/ResultQuestionItem.fxml"));
                    Node node = fxml.load();
                    ResultQuestionItemController item = fxml.getController();

                    item.setIndex(i + 1, total);
                    item.setQuestionText(rq.getQuestion().getQuestionText());

                    List<QuizChoiceCreate> choices = rq.getQuestion().getChoices();
                    List<String> answerTexts = choices.stream()
                            .map(QuizChoiceCreate::getText)
                            .collect(Collectors.toList());
                    item.setAnswers(answerTexts);

                    int correctIndex = -1;
                    int selectedIndex = -1;
                    int chosenOptionId = rq.getChosenOption_id();

                    for (int j = 0; j < choices.size(); j++) {
                        QuizChoiceCreate choice = choices.get(j);
                        if (choice.isCorrect()) correctIndex = j;
                        if (choice.getOption_id() == chosenOptionId) selectedIndex = j;
                    }

                    item.highlightAnswers(correctIndex, selectedIndex);
                    item.setExplanation(rq.getQuestion().getExplanation());
                    questionsBox.getChildren().add(node);

                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.err.println("Failed to load question item: " + ex.getMessage());
                }
            }

            doneButton.setOnAction(ev -> {
                if (onDone != null) onDone.run();
                stage.close();
            });

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading quiz results: " + e.getMessage()).showAndWait();
            if (stage != null) stage.close();
        }
    }
}

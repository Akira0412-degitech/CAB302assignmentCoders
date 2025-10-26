package com.example.cab302a1.ui.page.student;

import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.model.QuizChoiceCreate;
import com.example.cab302a1.ui.view.components.question.StudentQuestionItemController;
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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Controller responsible for handling the student quiz-taking interface.
 * <p>
 * This class loads quiz questions, displays them dynamically,
 * collects the student's selected answers, and triggers a callback when
 * the quiz is submitted.
 * </p>
 */
public class StudentTakeQuizController {

    /** Label showing the quiz title. */
    @FXML private Label titleLabel;

    /** Label showing the quiz description. */
    @FXML private Label descriptionLabel;

    /** Container for dynamically loaded question items. */
    @FXML private VBox questionsBox;

    /** Button for submitting answers and closing the quiz. */
    @FXML private Button doneButton;

    /** List of controllers for each displayed question. */
    private final List<StudentQuestionItemController> itemControllers = new ArrayList<>();

    /** Reference to the quiz stage (modal window). */
    private Stage stage;

    /** Callback executed when the quiz is submitted. */
    private Consumer<List<Integer>> onSubmit;

    /**
     * Opens the student quiz page as a modal dialog window.
     *
     * @param owner    the parent stage
     * @param quiz     the {@link Quiz} object containing questions and choices
     * @param onSubmit callback that receives a list of selected option indices
     */
    public static void open(Stage owner, Quiz quiz, Consumer<List<Integer>> onSubmit) {
        try {
            FXMLLoader loader = new FXMLLoader(StudentTakeQuizController.class.getResource(
                    "/com/example/cab302a1/StudentQuizPage/StudentTakeQuizPage.fxml"));
            Parent root = loader.load();
            StudentTakeQuizController c = loader.getController();

            Stage s = new Stage();
            s.initOwner(owner);
            s.initModality(Modality.WINDOW_MODAL);
            s.setTitle("Take Quiz");
            s.setScene(new Scene(root, 920, 720));

            c.stage = s;
            c.onSubmit = onSubmit;
            c.loadQuiz(quiz);

            s.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to open quiz page:\n" + e.getMessage()).showAndWait();
        }
    }

    /**
     * Loads the quiz content into the UI and prepares question items.
     * <p>
     * This method dynamically generates each question component,
     * binds question text and answer choices, and attaches submit logic
     * to the Done button.
     * </p>
     *
     * @param quiz the {@link Quiz} object to load and display
     */
    private void loadQuiz(Quiz quiz) {
        titleLabel.setText(quiz.getTitle() == null ? "Quiz" : quiz.getTitle());
        descriptionLabel.setText(quiz.getDescription() == null ? "" : quiz.getDescription());

        questionsBox.getChildren().clear();
        itemControllers.clear();

        // Load each question if available
        if (quiz.getQuestions() == null || quiz.getQuestions().isEmpty()) {
            // No questions to display
        } else {
            int total = quiz.getQuestions().size();
            for (int i = 0; i < total; i++) {
                var q = quiz.getQuestions().get(i);

                try {
                    FXMLLoader fxml = new FXMLLoader(getClass().getResource(
                            "/com/example/cab302a1/StudentQuizPage/StudentQuestionItem.fxml"));
                    Node node = fxml.load();
                    StudentQuestionItemController item = fxml.getController();

                    item.setIndex(i + 1, total);
                    item.setQuestionText(q.getQuestionText());

                    var texts = q.getChoices().stream()
                            .map(QuizChoiceCreate::getText)
                            .collect(Collectors.toList());
                    item.setAnswers(texts);

                    itemControllers.add(item);
                    questionsBox.getChildren().add(node);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        doneButton.setOnAction(ev -> {
            List<Integer> selections = itemControllers.stream()
                    .map(StudentQuestionItemController::getSelectedIndex)
                    .toList();
            if (onSubmit != null) onSubmit.accept(selections);
            stage.close();
        });
    }
}

package com.example.cab302a1.ui.page.teacher;

import com.example.cab302a1.dao.OptionDao;
import com.example.cab302a1.dao.QuestionDao;
import com.example.cab302a1.dao.DaoFactory;
import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.model.QuizChoiceCreate;
import com.example.cab302a1.model.QuizQuestionCreate;
import com.example.cab302a1.ui.page.editor.QuizEditorController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.function.Consumer;

/**
 * Controller for displaying detailed information about a quiz in the teacher view.
 * <p>
 * This controller allows teachers to view quiz questions, choices, and explanations,
 * as well as open the quiz editor for modifications. It retrieves full quiz details
 * (including questions and options) from DAOs and displays them in a formatted layout.
 * </p>
 */
public class TeacherQuizDetailController {

    /** Label displaying the quiz title. */
    @FXML private Label titleLabel;

    /** Label displaying the quiz description. */
    @FXML private Label descLabel;

    /** Container holding dynamically generated question and answer blocks. */
    @FXML private VBox questionsBox;

    /** Reference to the modal stage for this window. */
    private Stage stage;

    /** The quiz currently being displayed. */
    private Quiz quiz;

    /** Callback executed when the quiz is edited and updated. */
    private Consumer<Quiz> onUpdated;

    /**
     * Opens the quiz detail window as a modal dialog for teachers.
     *
     * @param owner     the parent stage
     * @param quiz      the quiz to display
     * @param onUpdated callback executed when the quiz is modified and saved
     */
    public static void open(Stage owner, Quiz quiz, Consumer<Quiz> onUpdated) {
        try {
            URL fxml = TeacherQuizDetailController.class.getResource(
                    "/com/example/cab302a1/TeacherQuizPage/TeacherQuizDetail.fxml");
            if (fxml == null) {
            }

            if (fxml == null) {
                throw new IllegalStateException(
                        "Detail FXML not found. Expected at /com/example/cab302a1/TeacherQuizPage/TeacherQuizDetail.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxml);
            VBox root = loader.load();

            TeacherQuizDetailController c = loader.getController();
            c.stage = new Stage();
            c.stage.initOwner(owner);
            c.stage.initModality(Modality.WINDOW_MODAL);
            c.stage.setTitle("Quiz Detail");
            c.stage.setScene(new Scene(root, 700, 650));

            // Load questions and options from the database
            QuestionDao questionDao = DaoFactory.getQuestionDao();
            List<QuizQuestionCreate> loadedQuestions = questionDao.getAllQuestions(quiz.getQuizId());

            OptionDao optionDao = DaoFactory.getOptionDao();
            for (QuizQuestionCreate q : loadedQuestions) {
                q.getChoices().addAll(optionDao.getOptionsByQuestionId(q.getQuestionId()));
            }
            quiz.setQuestions(loadedQuestions);

            c.setData(quiz, onUpdated);
            c.stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            javafx.scene.control.Alert a = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR,
                    "Failed to open detail: " + e.getMessage());
            a.setHeaderText("Load Error");
            a.showAndWait();
        }
    }

    /**
     * Sets the quiz data and update callback, then renders the content.
     *
     * @param quiz      the {@link Quiz} object containing all question and choice data
     * @param onUpdated callback to invoke when quiz data changes
     */
    private void setData(Quiz quiz, Consumer<Quiz> onUpdated) {
        this.quiz = quiz;
        this.onUpdated = onUpdated;
        render();
    }

    /**
     * Renders quiz details including questions, choices, and explanations.
     * <p>
     * Each question is displayed in a styled block with answer choices
     * and the correct answers visually highlighted.
     * </p>
     */
    private void render() {
        titleLabel.setText(quiz.getTitle() == null ? "(Untitled)" : quiz.getTitle());
        descLabel.setText(quiz.getDescription() == null ? "No Description" : quiz.getDescription());
        titleLabel.setWrapText(true);
        descLabel.setWrapText(true);

        questionsBox.getChildren().clear();
        int qIdx = 1;
        for (QuizQuestionCreate q : quiz.getQuestions()) {
            VBox block = new VBox(8);
            block.getStyleClass().add("question-block");

            Label qLabel = new Label("Q" + qIdx + ": " + (q.getQuestionText() == null ? "" : q.getQuestionText()));
            qLabel.getStyleClass().add("question-text");
            qLabel.setWrapText(true);
            block.getChildren().add(qLabel);

            VBox choicesBox = new VBox(4);
            int cIdx = 0;
            for (QuizChoiceCreate ch : q.getChoices()) {
                String mark = ch.isCorrect() ? " ✓" : "";
                String text = ch.getText() == null ? "" : ch.getText();
                Label cLabel = new Label((char) ('A' + cIdx) + ". " + text + mark);
                cLabel.getStyleClass().add("choice-text");
                if (ch.isCorrect()) {
                    cLabel.getStyleClass().add("correct-choice");
                }
                cLabel.setWrapText(true);
                choicesBox.getChildren().add(cLabel);
                cIdx++;
            }
            block.getChildren().add(choicesBox);

            String exp = q.getExplanation();
            if (exp != null && !exp.isBlank()) {
                Label expLabel = new Label("💡 Explanation: " + exp);
                expLabel.getStyleClass().add("explanation-text");
                expLabel.setWrapText(true);
                expLabel.maxWidthProperty().bind(questionsBox.widthProperty().subtract(40));
                block.getChildren().add(expLabel);
            }

            questionsBox.getChildren().add(block);
            qIdx++;
        }
    }

    /**
     * Opens the quiz editor window to allow teachers to modify quiz content.
     * <p>
     * After editing, updates the displayed quiz details and invokes
     * the {@code onUpdated} callback if provided.
     * </p>
     */
    @FXML
    private void onEdit() {
        Stage owner = (Stage) questionsBox.getScene().getWindow();
        QuizEditorController.openForEdit(owner, quiz, updated -> {
            this.quiz = updated;
            render();
            if (onUpdated != null) onUpdated.accept(updated);
        });
    }

    /**
     * Closes the quiz detail window.
     */
    @FXML
    private void onClose() {
        stage.close();
    }
}

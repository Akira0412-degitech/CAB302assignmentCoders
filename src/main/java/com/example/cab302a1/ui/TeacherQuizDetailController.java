package com.example.cab302a1.ui;

import com.example.cab302a1.dao.OptionDao;
import com.example.cab302a1.dao.QuestionDao;
import com.example.cab302a1.dao.DaoFactory;
import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.model.QuizChoiceCreate;
import com.example.cab302a1.model.QuizQuestionCreate;
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

public class TeacherQuizDetailController {

    @FXML private Label titleLabel;
    @FXML private Label descLabel;
    @FXML private VBox questionsBox;

    private Stage stage;
    private Quiz quiz;
    private Consumer<Quiz> onUpdated;


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


            QuestionDao questionDao = DaoFactory.getQuestionDao();
            List<QuizQuestionCreate> loadedQuestions = questionDao.getAllQuestions(quiz.getQuizId());

            OptionDao optionDao = DaoFactory.getOptionDao();
            for(QuizQuestionCreate q : loadedQuestions){
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

    private void setData(Quiz quiz, Consumer<Quiz> onUpdated) {
        this.quiz = quiz;
        this.onUpdated = onUpdated;
        render();
    }

    private void render() {
        titleLabel.setText(quiz.getTitle() == null ? "(Untitled)" : quiz.getTitle());
        descLabel.setText(quiz.getDescription() == null ? "No Description" : quiz.getDescription());
        titleLabel.setWrapText(true);
        descLabel.setWrapText(true);

        questionsBox.getChildren().clear();
        int qIdx = 1;
        for (QuizQuestionCreate q : quiz.getQuestions()) {
            // Create styled question block
            VBox block = new VBox(8);
            block.getStyleClass().add("question-block");

            // Question text with styling
            Label qLabel = new Label("Q" + qIdx + ": " + (q.getQuestionText() == null ? "" : q.getQuestionText()));
            qLabel.getStyleClass().add("question-text");
            qLabel.setWrapText(true);
            block.getChildren().add(qLabel);

            // Answer choices with styling
            VBox choicesBox = new VBox(4);
            int cIdx = 0;
            for (QuizChoiceCreate ch : q.getChoices()) {
                String mark = ch.isCorrect() ? " ✓" : "";
                String text = ch.getText() == null ? "" : ch.getText();
                Label cLabel = new Label((char)('A' + cIdx) + ". " + text + mark);
                cLabel.getStyleClass().add("choice-text");
                if (ch.isCorrect()) {
                    cLabel.getStyleClass().add("correct-choice");
                }
                cLabel.setWrapText(true);
                choicesBox.getChildren().add(cLabel);
                cIdx++;
            }
            block.getChildren().add(choicesBox);

            // Explanation (if there is)
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

    @FXML
    private void onEdit() {
        Stage owner = (Stage) questionsBox.getScene().getWindow();
        QuizEditorController.openForEdit(owner, quiz, updated -> {
            this.quiz = updated;
            render();
            if (onUpdated != null) onUpdated.accept(updated);
        });
    }

    @FXML
    private void onClose() {
        stage.close();
    }
}

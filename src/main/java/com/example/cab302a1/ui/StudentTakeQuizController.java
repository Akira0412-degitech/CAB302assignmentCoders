package com.example.cab302a1.ui;

import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.model.QuizChoiceCreate;
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

public class StudentTakeQuizController {

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private VBox questionsBox;
    @FXML private Button doneButton;

    private final List<StudentQuestionItemController> itemControllers = new ArrayList<>();
    private Stage stage;
    private Consumer<List<Integer>> onSubmit;

    /** Open as modal dialog. */
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
            s.setScene(new Scene(root, 900, 680));

            c.stage = s;
            c.onSubmit = onSubmit;
            c.loadQuiz(quiz);

            s.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to open quiz page:\n" + e.getMessage()).showAndWait();
        }
    }

    private void loadQuiz(Quiz quiz) {
        titleLabel.setText(quiz.getTitle() == null ? "Quiz" : quiz.getTitle());
        descriptionLabel.setText(quiz.getDescription() == null ? "" : quiz.getDescription());

        questionsBox.getChildren().clear();
        itemControllers.clear();


        //done yet need to explain
        if (quiz.getQuestions() == null || quiz.getQuestions().isEmpty()) {
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
        // This part needs to be fixed. this part is runninng when students finish the quiz. should make Object questionresponse.
        doneButton.setOnAction(ev -> {
            List<Integer> selections = itemControllers.stream()
                    .map(StudentQuestionItemController::getSelectedIndex)
                    .toList();
            if (onSubmit != null) onSubmit.accept(selections);
            stage.close();
        });
    }
}

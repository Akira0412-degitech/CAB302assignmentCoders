

package com.example.cab302a1.ui;
/*
 * QuestionItemController
 * This class handles the behavior of QuestionItem.fxml.
 *
 * What it does:
 * - Manages the input of the question sentence and four answer choices.
 * - Marks the answer the user clicks as the correct answer (highlighted with a background color).
 * - Sends a "delete" request to the parent (QuizEditor) when the X button is pressed.
 * - Converts the current input to a QuizQuestionCreate/QuizChoiceCreate model.
 *
 * Where is it used?
 * - It is retrieved from QuizEditorController and used to create a quiz.
 */

import com.example.cab302a1.model.QuizChoiceCreate;
import com.example.cab302a1.model.QuizQuestionCreate;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.function.Consumer;

public class QuestionItemController {

    @FXML private Label indexLabel;
    @FXML private TextField questionField;
    @FXML private TextField answer1;
    @FXML private TextField answer2;
    @FXML private TextField answer3;
    @FXML private TextField answer4;

    // Which answer index is correct (0..3), -1 if none selected
    private int correctIndex = -1;

    // Callback from parent to remove this item from the list
    private Consumer<QuestionItemController> onRemove;

    @FXML
    public void initialize() {
        // Click on an answer field marks it as the correct one (visual + state)
        List<TextField> fields = List.of(answer1, answer2, answer3, answer4);
        for (int i = 0; i < fields.size(); i++) {
            final int idx = i;
            TextField tf = fields.get(i);
            tf.setOnMouseClicked(e -> setCorrect(idx));
        }
    }

    private void setCorrect(int idx) {
        this.correctIndex = idx;
        // Toggle CSS class "correct-answer" to highlight the chosen field
        List<TextField> fields = List.of(answer1, answer2, answer3, answer4);
        for (int i = 0; i < fields.size(); i++) {
            TextField tf = fields.get(i);
            tf.getStyleClass().remove("correct-answer");
            if (i == idx) tf.getStyleClass().add("correct-answer");
        }
    }

    public void setOnRemove(Consumer<QuestionItemController> cb) {
        this.onRemove = cb;
    }

    @FXML
    private void onRemoveSelf() {
        if (onRemove != null) onRemove.accept(this);
    }

    public void setIndexLabel(String text) {
        indexLabel.setText(text);
    }

    /** Convert current UI state to a Question model (with 4 choices). */
    public QuizQuestionCreate toQuestion() {
        String q = safe(questionField.getText());
        String a1 = safe(answer1.getText());
        String a2 = safe(answer2.getText());
        String a3 = safe(answer3.getText());
        String a4 = safe(answer4.getText());

        if (q.isEmpty()) throw new IllegalStateException("Question text is empty.");
        if (a1.isEmpty() || a2.isEmpty() || a3.isEmpty() || a4.isEmpty())
            throw new IllegalStateException("All answer fields must be filled.");
        if (correctIndex < 0) throw new IllegalStateException("Please choose a correct answer.");

        QuizQuestionCreate question = new QuizQuestionCreate();
        question.setQuestionText(q);
        List<String> answers = List.of(a1, a2, a3, a4);
        for (int i = 0; i < answers.size(); i++) {
            question.getChoices().add(new QuizChoiceCreate(answers.get(i), i == correctIndex));
        }
        return question;
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }
}

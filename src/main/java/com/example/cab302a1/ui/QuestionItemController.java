package com.example.cab302a1.ui;

import com.example.cab302a1.model.QuizChoiceCreate;
import com.example.cab302a1.model.QuizQuestionCreate;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;
import java.util.function.Consumer;

public class QuestionItemController {

    @FXML private Label indexLabel;
    @FXML private TextField questionField;
    @FXML private TextField answer1;
    @FXML private TextField answer2;
    @FXML private TextField answer3;
    @FXML private TextField answer4;

    // ★ New: Radio buttons for explicit correct selection
    @FXML private RadioButton rb1, rb2, rb3, rb4;
    private final ToggleGroup correctGroup = new ToggleGroup();

    /** Which answer is correct (0..3). -1 means not selected yet. */
    private int correctIndex = -1;

    /** Parent callback to remove this card. */
    private Consumer<QuestionItemController> onRemove;

    @FXML
    public void initialize() {
        // Basic fx:id sanity checks
        assert indexLabel != null : "fx:id 'indexLabel' not injected";
        assert questionField != null : "fx:id 'questionField' not injected";
        assert answer1 != null : "fx:id 'answer1' not injected";
        assert answer2 != null : "fx:id 'answer2' not injected";
        assert answer3 != null : "fx:id 'answer3' not injected";
        assert answer4 != null : "fx:id 'answer4' not injected";
        assert rb1 != null && rb2 != null && rb3 != null && rb4 != null : "RadioButtons not injected";

        // ★ ToggleGroup wiring
        rb1.setToggleGroup(correctGroup);
        rb2.setToggleGroup(correctGroup);
        rb3.setToggleGroup(correctGroup);
        rb4.setToggleGroup(correctGroup);

        // ★ When radio selection changes, update correctIndex and highlight the chosen TextField
        correctGroup.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            if (newT == rb1) correctIndex = 0;
            else if (newT == rb2) correctIndex = 1;
            else if (newT == rb3) correctIndex = 2;
            else if (newT == rb4) correctIndex = 3;
            else correctIndex = -1;
            updateCorrectHighlight();
        });

    }

    /** After CSS // Keep same green highlight behavior on the selected answer field (optional). */
    private void updateCorrectHighlight() {
        List<TextField> fields = List.of(answer1, answer2, answer3, answer4);
        for (int i = 0; i < fields.size(); i++) {
            TextField tf = fields.get(i);
            tf.getStyleClass().remove("correct-answer");
            if (i == correctIndex) tf.getStyleClass().add("correct-answer");
        }
    }

    public void setOnRemove(Consumer<QuestionItemController> cb) { this.onRemove = cb; }

    @FXML
    private void onRemoveSelf() { if (onRemove != null) onRemove.accept(this); }

    public void setIndexLabel(String text) { indexLabel.setText(text); }

    /** Public setter used by prefill: set the question text. */
    public void setQuestionText(String text) { questionField.setText(text == null ? "" : text); }

    /** Public setter used by prefill: set 4 answers (shorter fills "", longer is ignored). */
    public void setAnswerTexts(String[] texts) {
        String t1 = (texts != null && texts.length > 0 && texts[0] != null) ? texts[0] : "";
        String t2 = (texts != null && texts.length > 1 && texts[1] != null) ? texts[1] : "";
        String t3 = (texts != null && texts.length > 2 && texts[2] != null) ? texts[2] : "";
        String t4 = (texts != null && texts.length > 3 && texts[3] != null) ? texts[3] : "";
        answer1.setText(t1); answer2.setText(t2); answer3.setText(t3); answer4.setText(t4);
    }

    /** Public setter used by prefill: mark the correct answer programmatically. */
    public void setCorrectIndex(int idx) {
        // Select radio button
        if (idx == 0) rb1.setSelected(true);
        else if (idx == 1) rb2.setSelected(true);
        else if (idx == 2) rb3.setSelected(true);
        else if (idx == 3) rb4.setSelected(true);
        else correctGroup.selectToggle(null);

        // Maintain internal state & CSS highlight
        correctIndex = (idx >= 0 && idx <= 3) ? idx : -1;
        updateCorrectHighlight();
    }

    /** Build model from current UI (validates). */
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

    private static String safe(String s) { return s == null ? "" : s.trim(); }
}

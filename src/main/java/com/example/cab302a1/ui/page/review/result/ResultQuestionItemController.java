package com.example.cab302a1.ui.page.review.result;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Controller class responsible for displaying a single quiz question result item
 * in the quiz review page. It handles showing the question text, possible answers,
 * highlighting correct and selected options, and displaying explanations.
 * <p>
 * This controller is designed to be used with an FXML layout that includes
 * labels, toggle buttons for answers, and an explanation box.
 * </p>
 */
public class ResultQuestionItemController {

    /** Label showing the question index (e.g., 01/10). */
    @FXML private Label indexLabel;

    /** Text field displaying the question text (read-only). */
    @FXML private TextField questionField;

    /** Toggle buttons for answer options A–D. */
    @FXML private ToggleButton btnA;
    @FXML private ToggleButton btnB;
    @FXML private ToggleButton btnC;
    @FXML private ToggleButton btnD;

    /** Container and label for displaying explanations. */
    @FXML private VBox explanationBox;
    @FXML private Label explanationLabel;

    /**
     * Initializes the controller by disabling user interaction
     * with the question and answer fields.
     */
    @FXML
    public void initialize() {
        // Make everything non-interactive
        questionField.setEditable(false);
        questionField.setMouseTransparent(true);
        questionField.setFocusTraversable(false);

        btnA.setDisable(true);
        btnB.setDisable(true);
        btnC.setDisable(true);
        btnD.setDisable(true);
    }

    /**
     * Sets the question index display (e.g., "02/10").
     *
     * @param indexOneBased the current question index (1-based)
     * @param total the total number of questions
     */
    public void setIndex(int indexOneBased, int total) {
        indexLabel.setText(String.format("%02d/%02d", indexOneBased, total));
    }

    /**
     * Sets the question text in the display field.
     *
     * @param text the question text; if null, sets an empty string
     */
    public void setQuestionText(String text) {
        questionField.setText(text != null ? text : "");
    }

    /**
     * Sets the answer text for the four available options.
     *
     * @param answers a list of answer texts (up to four items)
     */
    public void setAnswers(List<String> answers) {
        btnA.setText(answers.size() > 0 ? "A. " + answers.get(0) : "A. Answer 1");
        btnB.setText(answers.size() > 1 ? "B. " + answers.get(1) : "B. Answer 2");
        btnC.setText(answers.size() > 2 ? "C. " + answers.get(2) : "C. Answer 3");
        btnD.setText(answers.size() > 3 ? "D. " + answers.get(3) : "D. Answer 4");
    }

    /**
     * Highlights the correct and user-selected answers visually.
     * Adds check (✓) for correct answers and cross (✗) for incorrect selections.
     *
     * @param correctIndex  the index (0–3) of the correct answer
     * @param selectedIndex the index (0–3) of the user’s selected answer,
     *                      or -1 if no answer was selected
     */
    public void highlightAnswers(int correctIndex, int selectedIndex) {
        ToggleButton[] buttons = {btnA, btnB, btnC, btnD};

        // Reset all button styles
        for (ToggleButton btn : buttons) {
            btn.getStyleClass().removeAll("correct-answer", "wrong-answer", "not-selected");
        }

        // Highlight correct answer with checkmark icon
        if (correctIndex >= 0 && correctIndex < buttons.length) {
            buttons[correctIndex].getStyleClass().add("correct-answer");
            String currentText = buttons[correctIndex].getText();
            // Add checkmark if not already present
            if (!currentText.contains("✓")) {
                buttons[correctIndex].setText("✓ " + currentText);
            }
        }

        // Highlight user's selected answer if it's wrong with X icon
        if (selectedIndex >= 0 && selectedIndex < buttons.length && selectedIndex != correctIndex) {
            buttons[selectedIndex].getStyleClass().add("wrong-answer");
            String currentText = buttons[selectedIndex].getText();
            // Add X mark if not already present
            if (!currentText.contains("✗")) {
                buttons[selectedIndex].setText("✗ " + currentText);
            }
        }

        // Mark not selected answers
        for (int i = 0; i < buttons.length; i++) {
            if (i != correctIndex && i != selectedIndex) {
                buttons[i].getStyleClass().add("not-selected");
            }
        }
    }

    /**
     * Sets or hides the explanation text for the question.
     * If the explanation is null or empty, the box is hidden.
     *
     * @param explanation the explanation text to display
     */
    public void setExplanation(String explanation) {
        if (explanation == null || explanation.trim().isEmpty()) {
            explanationBox.setVisible(false);
            explanationBox.setManaged(false);
        } else {
            explanationBox.setVisible(true);
            explanationBox.setManaged(true);
            explanationLabel.setText(explanation);
        }
    }
}

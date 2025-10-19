package com.example.cab302a1.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class ResultQuestionItemController {

    @FXML private Label indexLabel;
    @FXML private TextField questionField;
    @FXML private ToggleButton btnA;
    @FXML private ToggleButton btnB;
    @FXML private ToggleButton btnC;
    @FXML private ToggleButton btnD;
    @FXML private VBox explanationBox;
    @FXML private Label explanationLabel;

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

    public void setIndex(int indexOneBased, int total) {
        indexLabel.setText(String.format("%02d/%02d", indexOneBased, total));
    }

    public void setQuestionText(String text) {
        questionField.setText(text != null ? text : "");
    }

    public void setAnswers(List<String> answers) {
        btnA.setText(answers.size() > 0 ? "A. " + answers.get(0) : "A. Answer 1");
        btnB.setText(answers.size() > 1 ? "B. " + answers.get(1) : "B. Answer 2");
        btnC.setText(answers.size() > 2 ? "C. " + answers.get(2) : "C. Answer 3");
        btnD.setText(answers.size() > 3 ? "D. " + answers.get(3) : "D. Answer 4");
    }

    /**
     * Highlights the correct and user-selected answers
     * @param correctIndex The index of the correct answer (0-3)
     * @param selectedIndex The index of the user's selected answer (0-3), or -1 if not answered
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
     * Sets the explanation text. If null or empty, hides the explanation box.
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


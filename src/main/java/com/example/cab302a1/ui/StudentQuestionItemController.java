package com.example.cab302a1.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

import java.util.List;

public class StudentQuestionItemController {

    @FXML private Label indexLabel;
    @FXML private TextField questionField;
    @FXML private ToggleButton btnA;
    @FXML private ToggleButton btnB;
    @FXML private ToggleButton btnC;
    @FXML private ToggleButton btnD;

    private final ToggleGroup group = new ToggleGroup();

    @FXML
    public void initialize() {
        btnA.setToggleGroup(group);
        btnB.setToggleGroup(group);
        btnC.setToggleGroup(group);
        btnD.setToggleGroup(group);

        // only button can click
        questionField.setEditable(false);
        questionField.setMouseTransparent(true);
        questionField.setFocusTraversable(false);
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

    public int getSelectedIndex() {
        if (group.getSelectedToggle() == null) return -1;
        if (group.getSelectedToggle() == btnA) return 0;
        if (group.getSelectedToggle() == btnB) return 1;
        if (group.getSelectedToggle() == btnC) return 2;
        if (group.getSelectedToggle() == btnD) return 3;
        return -1;
    }
}

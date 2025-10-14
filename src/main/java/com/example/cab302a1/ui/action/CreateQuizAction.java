package com.example.cab302a1.ui.action;

import javafx.scene.control.Button;

import java.util.function.Consumer;

/**
 * CreateQuizAction
 * - Builds a '+' card Button and wires the provided handler to open editor.
 * - Controller injects the handler (so action stays UI-agnostic).
 */
public class CreateQuizAction {

    private final Consumer<javafx.event.ActionEvent> openEditorHandler;

    /**
     * @param openEditorHandler a handler that opens the editor and refreshes UI on save
     */
    public CreateQuizAction(Consumer<javafx.event.ActionEvent> openEditorHandler) {
        this.openEditorHandler = openEditorHandler;
    }

    /** Build the '+' card button */
    public Button buildPlusCard() {
        Button plus = new Button("+");
        plus.getStyleClass().add("plus-card");
        plus.setPrefSize(200, 150);
        plus.setOnAction(openEditorHandler::accept);
        return plus;
    }
}

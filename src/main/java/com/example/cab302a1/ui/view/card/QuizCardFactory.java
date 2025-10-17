package com.example.cab302a1.ui.view.card;

import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.ui.view.components.HoverInfoButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;


import java.util.Objects;
import java.util.function.Consumer;

import java.util.function.Supplier;

/**
 * QuizCardFactory
 * Button Attachment + Position
 * View-only factory that assembles quiz cards for Student/Teacher roles.
 * No DAO/Service calls here.
 * All business decisions (tooltip content, click behaviors) are passed in from Controller.
 */
public class QuizCardFactory {

    /** Shared base: center title button only. */
    private StackPane buildBaseCard(Quiz quiz) {
        StackPane card = new StackPane();
        Button titleBtn = new Button(Objects.toString(quiz.getTitle(), "Untitled"));
        titleBtn.getStyleClass().add("quiz-card");
        titleBtn.setPrefSize(200, 150);
        titleBtn.setWrapText(true);
        StackPane.setAlignment(titleBtn, Pos.CENTER);
        card.getChildren().add(titleBtn);
        return card;
    }

    /** Student card: add tooltip('!') + title click handler. */
    public Node buildStudentCard(Quiz quiz,
                                 Supplier<String> tooltipSupplier,
                                 EventHandler<ActionEvent> onTitleClick) {
        return buildStudentCard(quiz, tooltipSupplier, onTitleClick, false);
    }

    /**
     * [신규 오버로드]
     * Student card with completion style support.
     * completed==true 이면 CSS 클래스 'quiz-card-completed' 추가.
     */
    public Node buildStudentCard(Quiz quiz,
                                 Supplier<String> tooltipSupplier,
                                 EventHandler<ActionEvent> onTitleClick,
                                 boolean completed) {
        StackPane card = buildBaseCard(quiz);
        Button titleBtn = (Button) card.getChildren().get(0);

        if (completed) {
            titleBtn.getStyleClass().add("quiz-card-completed");
        }

        if (onTitleClick != null) {
            titleBtn.setOnAction(onTitleClick);
        }

        if (tooltipSupplier != null) {
            Node infoBtn = HoverInfoButton.of(tooltipSupplier);
            StackPane.setAlignment(infoBtn, Pos.TOP_RIGHT);
            StackPane.setMargin(infoBtn, new Insets(6, 6, 0, 0));
            card.getChildren().add(infoBtn);
        }
        return card;
    }


    /**
     * Teacher card: add '×' (top-left) + info badge (top-right) + title click handler.
     * onHide receives the whole card Node so controller can remove it from the grid.
     */
    public Node buildTeacherCard(Quiz quiz,
                                 EventHandler<ActionEvent> onTitleClick,
                                 Consumer<Node> onHide,
                                 Supplier<String> tooltipSupplier) {
        StackPane card = buildBaseCard(quiz);
        Button titleBtn = (Button) card.getChildren().get(0);

        if (onTitleClick != null) {
            titleBtn.setOnAction(onTitleClick);
        }

        // Delete button (top-left)
        Button closeBtn = new Button("×");
        closeBtn.getStyleClass().add("delete-btn");
        StackPane.setAlignment(closeBtn, Pos.TOP_LEFT);
        StackPane.setMargin(closeBtn, new Insets(6, 0, 0, 6));
        closeBtn.setOnAction(e -> {
            e.consume();
            if (onHide != null) onHide.accept(card);
        });
        card.getChildren().add(closeBtn);

        // Info badge (top-right) - same as student cards
        if (tooltipSupplier != null) {
            Node infoBtn = HoverInfoButton.of(tooltipSupplier);
            StackPane.setAlignment(infoBtn, Pos.TOP_RIGHT);
            StackPane.setMargin(infoBtn, new Insets(6, 6, 0, 0));
            card.getChildren().add(infoBtn);
        }

        return card;
    }
    
    /**
     * Overload for backward compatibility - teacher card without info badge
     */
    public Node buildTeacherCard(Quiz quiz,
                                 EventHandler<ActionEvent> onTitleClick,
                                 Consumer<Node> onHide) {
        return buildTeacherCard(quiz, onTitleClick, onHide, null);
    }
}

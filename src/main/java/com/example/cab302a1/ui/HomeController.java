package com.example.cab302a1.ui;

import com.example.cab302a1.UserRole;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared controller for both Student and Teacher home.
 * Behavior (read-only vs. editable) is toggled by UserRole.
 */
public class HomeController {

    @FXML private TilePane grid;

    // Current role (default to STUDENT for safety).
    private UserRole role = UserRole.STUDENT;

    // In-memory quiz titles for UI preview; will be replaced by DB later
    private final List<String> quizzes = new ArrayList<>(List.of(
            "Java Basics", "OOP Essentials", "Exceptions",
            "Collections", "Streams/Lambda", "File I/O"
    ));

    @FXML
    public void initialize() {
        // Runs automatically after the FXML screen is ready and UI parts are linked
        // Shows the screen in its default state; later we can call refresh() to update it

        refresh();
    }

    //Sets whether this screen is for a STUDENT or a TEACHER
    public void setRole(UserRole role) {
        this.role = role;
    }

    /** Rebuild the grid according to the current role and quiz list. */
    public void refresh() {
        grid.getChildren().clear();
        quizzes.forEach(name -> grid.getChildren().add(createQuizCard(name)));
        if (role == UserRole.TEACHER) {
            grid.getChildren().add(createPlusCard());
        }
    }

    /** Create a square 'quiz card' button with role-specific action. */
    private Button createQuizCard(String title) {
        Button card = new Button(title);
        card.getStyleClass().add("quiz-card"); // CSS: .quiz-card
        card.setPrefSize(160, 160);            // square card
        card.setWrapText(true);                // line wrap for long titles

        card.setOnAction(e -> {
            if (role == UserRole.STUDENT) {
                info("Start Quiz", "Selected: " + title + "\n(Next: navigate to attempt screen)");
            } else {
                info("Open Quiz (Edit)", "Selected: " + title + "\n(Next: navigate to editor)");
            }
        });
        return card;
    }

    /** Create the teacher-only '+' card to add a new quiz title. */
    private Button createPlusCard() {
        Button plus = new Button("+");
        plus.getStyleClass().add("plus-card"); // CSS: .plus-card
        plus.setPrefSize(160, 160);
        plus.setOnAction(e -> onAddQuiz());
        return plus;
    }

    /** Prompt for a new quiz title and append it to the list (teacher mode). */
    private void onAddQuiz() {
        TextInputDialog d = new TextInputDialog();
        d.setTitle("Create New Quiz");
        d.setHeaderText("Enter a title for the new quiz");
        d.setContentText("Title:");
        d.showAndWait().ifPresent(title -> {
            String t = title.trim();
            if (t.isEmpty()) {
                warn("Title cannot be empty.");
                return;
            }
            quizzes.add(t);
            refresh(); // re-render grid with the new card
        });
    }

    /** Convenience info dialog. */
    private void info(String header, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(header);
        a.showAndWait();
    }

    /** Convenience warning dialog. */
    private void warn(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }
}

package com.example.cab302a1.ui;

/*
 * Purpose:
 *  - Drives QuizEditor.fxml (title/description + a list of QuestionItem cards).
 *  - Adds/removes question cards, and on "Done" builds a Quiz model.
 *
 * How it works:
 *  - "ADD MORE": loads QuestionItem.fxml, attaches remove callback, appends to questionsBox.
 *  - "Done": validates inputs, calls toQuestion() on each card, returns a Quiz via onSave callback.
 *
 * Where used:
 *  - Opened from HomeController (teacher '+' button) as a modal dialog.
 */

import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.model.QuizQuestionCreate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class QuizEditorController {

    @FXML private TextField titleField;
    @FXML private TextArea descArea;
    @FXML private VBox questionsBox;
    @FXML private Button addQuestionBtn;
    @FXML private Button doneBtn;

    // Parent callback: deliver the newly created Quiz back to HomeController
    private Consumer<Quiz> onSave;

    // Keep child controllers to read data later
    private final List<QuestionItemController> items = new ArrayList<>();

    // Keep my Stage to close dialog
    private Stage myStage;

    @FXML
    public void initialize() {
        // Start with one empty question
        addQuestion();

        addQuestionBtn.setOnAction(e -> addQuestion());
        doneBtn.setOnAction(e -> onDone());
    }

    /** Parent (HomeController) sets this to receive the created quiz. */
    public void setOnSave(Consumer<Quiz> cb) {
        this.onSave = cb;
    }

    /** Stage setter (called by open()). */
    public void setStage(Stage stage) {
        this.myStage = stage;
    }

    /** Add a new QuestionItem card (load FXML and wire callbacks). */
    // src/main/java/com/example/cab302a1/ui/QuizEditorController.java
    private void addQuestion() {
        try {
            var url = Objects.requireNonNull(
                    getClass().getResource("/com/example/cab302a1/QuestionItem.fxml"));
            System.out.println("Loading FXML: " + url);

            FXMLLoader loader = new FXMLLoader(url);
            Node node = loader.load();

            // IMPORTANT: explicit type
            QuestionItemController item = loader.getController();
            item.setOnRemove(this::removeQuestion);

            // IMPORTANT: add to the list 'items', not 'item.add(...)'
            items.add(item);

            questionsBox.getChildren().add(node);
            refreshIndices();
        } catch (Exception ex) {
            // Show the real root cause class/message
            Throwable cause = ex;
            while (cause.getCause() != null) cause = cause.getCause();
            ex.printStackTrace();
            showAlert("Error",
                    "Failed to add question:\n" + cause.getClass().getName() + "\n" + String.valueOf(cause.getMessage()),
                    Alert.AlertType.ERROR);
        }
    }


    /** Remove the given QuestionItem card. */
    private void removeQuestion(QuestionItemController item) {
        int idx = items.indexOf(item);
        if (idx >= 0) {
            items.remove(idx);
            questionsBox.getChildren().remove(idx);
            refreshIndices();
        }
    }

    /** Update "01/03" labels on each QuestionItem. */
    private void refreshIndices() {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setIndexLabel(String.format("%02d/%02d", i + 1, items.size()));
        }
    }

    /** Build Quiz from UI and return to parent via onSave. */
    private void onDone() {
        try {
            Quiz quiz = buildQuizFromUI();
            if (onSave != null) onSave.accept(quiz);
            if (myStage != null) myStage.close();
        } catch (IllegalStateException ex) {
            showAlert("Validation", ex.getMessage(), Alert.AlertType.WARNING);
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to build quiz: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /** Collect & validate fields; convert child cards to model objects. */
    private Quiz buildQuizFromUI() {
        String title = safe(titleField.getText());
        String desc  = safe(descArea.getText());

        if (title.isEmpty()) throw new IllegalStateException("Title is required.");
        if (items.isEmpty()) throw new IllegalStateException("Please add at least one question.");

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setDescription(desc);

        for (QuestionItemController item : items) {
            QuizQuestionCreate q = item.toQuestion(); // may throw if invalid
            quiz.getQuestions().add(q);
        }
        return quiz;
    }

    private static String safe(String s) { return s == null ? "" : s.trim(); }

    private void showAlert(String header, String msg, Alert.AlertType type) {
        Alert a = new Alert(type, msg, ButtonType.OK);
        a.setHeaderText(header);
        a.showAndWait();
    }

    /* Convenience: open this editor as a modal dialog and return the created quiz via callback. */
    public static void open(Stage owner, Consumer<Quiz> onSave) {
        try {
            URL url = Objects.requireNonNull(
                    QuizEditorController.class.getResource("/com/example/cab302a1/QuizEditor.fxml"));
            FXMLLoader loader = new FXMLLoader(url);
            var root = loader.load();

            QuizEditorController c = loader.getController();
            c.setOnSave(onSave);

            Stage dialog = new Stage();
            dialog.initOwner(owner);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Create Quiz");
            dialog.setScene(new Scene((Parent) root, 900, 640));

            // Attach shared CSS if present
            var css1 = QuizEditorController.class.getResource("/HomePage.css");
            var css2 = QuizEditorController.class.getResource("/com/example/cab302a1/HomePage.css");
            if (css1 != null) dialog.getScene().getStylesheets().add(css1.toExternalForm());
            else if (css2 != null) dialog.getScene().getStylesheets().add(css2.toExternalForm());

            c.setStage(dialog);
            dialog.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

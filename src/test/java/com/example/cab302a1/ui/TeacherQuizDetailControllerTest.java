package com.example.cab302a1.ui;

import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.model.QuizChoiceCreate;
import com.example.cab302a1.model.QuizQuestionCreate;
import com.example.cab302a1.ui.Teacher.TeacherQuizDetailController;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class TeacherQuizDetailControllerTest {

    @BeforeAll
    static void initToolkit() {
        try { Platform.startup(() -> {}); } catch (IllegalStateException ignored) {}
    }

    private static void set(Object target, String field, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(target, value);
    }
    private static Object get(Object target, String field) throws Exception {
        Field f = target.getClass().getDeclaredField(field);
        f.setAccessible(true);
        return f.get(target);
    }

    @Test
    void render_displaysTitleDesc_andCorrectMark() throws Exception {
        // Quiz dummy data
        Quiz quiz = new Quiz();
        quiz.setTitle("T");
        quiz.setDescription("D");
        QuizQuestionCreate q = new QuizQuestionCreate();
        q.setQuestionText("Q1");
        q.getChoices().add(new QuizChoiceCreate("A", false));
        q.getChoices().add(new QuizChoiceCreate("B", true));   // correct
        q.getChoices().add(new QuizChoiceCreate("C", false));
        q.getChoices().add(new QuizChoiceCreate("D", false));
        quiz.getQuestions().add(q);

        // Controller render
        TeacherQuizDetailController c = new TeacherQuizDetailController();
        set(c, "titleLabel", new Label());
        set(c, "descLabel", new Label());
        set(c, "questionsBox", new VBox());
        set(c, "quiz", quiz);

        Method render = TeacherQuizDetailController.class.getDeclaredMethod("render");
        render.setAccessible(true);
        render.invoke(c);

        // Title check
        assertEquals("T", ((Label) get(c, "titleLabel")).getText());
        assertEquals("D", ((Label) get(c, "descLabel")).getText());

        // Correct mark
        VBox box = (VBox) get(c, "questionsBox");
        boolean hasCorrectMark = containsCorrectMark(box);

        assertTrue(hasCorrectMark,
                "Correct answer should be marked on UI (text '✓/✔/correct' or styleClass 'correct').");
    }

    private static boolean containsCorrectMark(Node node) {
        if (node == null) return false;

        // Class name
        if (node.getStyleClass() != null) {
            for (String cls : node.getStyleClass()) {
                if (cls != null && cls.toLowerCase().contains("correct")) return true;
            }
        }

        // Label text
        if (node instanceof Labeled lab) {
            String txt = lab.getText();
            if (txt != null) {
                String lower = txt.toLowerCase();
                if (txt.contains("✓") || txt.contains("✔") || lower.contains("correct")) {
                    return true;
                }
            }
        }

        if (node instanceof Parent p) {
            for (Node child : p.getChildrenUnmodifiable()) {
                if (containsCorrectMark(child)) return true;
            }
        }
        return false;
    }
}

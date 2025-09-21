package com.example.cab302a1.ui;

import com.example.cab302a1.model.QuizQuestionCreate;
import javafx.application.Platform;
import javafx.scene.control.*;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class QuestionItemControllerTest {

    @BeforeAll
    static void initToolkit() {
        try { Platform.startup(() -> {}); } catch (IllegalStateException ignored) {}
    }

    private static void set(Object target, String field, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(target, value);
    }

    private QuestionItemController newControllerWithUI() throws Exception {
        QuestionItemController c = new QuestionItemController();
        set(c, "indexLabel", new Label());
        set(c, "questionField", new TextField());
        set(c, "answer1", new TextField());
        set(c, "answer2", new TextField());
        set(c, "answer3", new TextField());
        set(c, "answer4", new TextField());
        set(c, "rb1", new RadioButton());
        set(c, "rb2", new RadioButton());
        set(c, "rb3", new RadioButton());
        set(c, "rb4", new RadioButton());

        // call @FXML initialize()
        c.initialize();
        return c;
    }

    @Test
    void toQuestion_throws_whenNoCorrectSelected() throws Exception {
        QuestionItemController c = newControllerWithUI();
        ((TextField) get(c, "questionField")).setText("Q1?");
        ((TextField) get(c, "answer1")).setText("A");
        ((TextField) get(c, "answer2")).setText("B");
        ((TextField) get(c, "answer3")).setText("C");
        ((TextField) get(c, "answer4")).setText("D");

        IllegalStateException ex = assertThrows(IllegalStateException.class, c::toQuestion);
        assertTrue(ex.getMessage().toLowerCase().contains("choose a correct"), ex.getMessage());
    }

    @Test
    void toQuestion_throws_whenQuestionEmpty() throws Exception {
        QuestionItemController c = newControllerWithUI();
        // select a correct toggle
        ((RadioButton) get(c, "rb1")).setSelected(true);

        ((TextField) get(c, "answer1")).setText("A");
        ((TextField) get(c, "answer2")).setText("B");
        ((TextField) get(c, "answer3")).setText("C");
        ((TextField) get(c, "answer4")).setText("D");

        IllegalStateException ex = assertThrows(IllegalStateException.class, c::toQuestion);
        assertTrue(ex.getMessage().toLowerCase().contains("question text is empty"));
    }

    @Test
    void toQuestion_success_buildsChoices_andCorrectFlag() throws Exception {
        QuestionItemController c = newControllerWithUI();
        ((TextField) get(c, "questionField")).setText("Q1?");
        ((TextField) get(c, "answer1")).setText("A");
        ((TextField) get(c, "answer2")).setText("B");
        ((TextField) get(c, "answer3")).setText("C");
        ((TextField) get(c, "answer4")).setText("D");
        ((RadioButton) get(c, "rb3")).setSelected(true); // correct = index 2

        // set option ids
        c.setOptionIds(new int[]{11, 22, 33, 44});

        QuizQuestionCreate q = c.toQuestion();
        assertEquals(4, q.getChoices().size());
        assertTrue(q.getChoices().get(2).isCorrect());
        assertEquals(33, q.getChoices().get(2).getOption_id());
    }

    // helper: read private field
    private static Object get(Object target, String field) throws Exception {
        Field f = target.getClass().getDeclaredField(field);
        f.setAccessible(true);
        return f.get(target);
    }
}

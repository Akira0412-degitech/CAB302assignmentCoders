package com.example.cab302a1.ui;

import com.example.cab302a1.ui.view.components.question.StudentQuestionItemController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentQuestionItemControllerTest {

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

    private StudentQuestionItemController newC() throws Exception {
        StudentQuestionItemController c = new StudentQuestionItemController();
        set(c, "indexLabel", new Label());
        set(c, "questionField", new TextField());
        set(c, "btnA", new ToggleButton("A"));
        set(c, "btnB", new ToggleButton("B"));
        set(c, "btnC", new ToggleButton("C"));
        set(c, "btnD", new ToggleButton("D"));
        c.initialize();
        return c;
    }

    @Test
    void getSelectedIndex_returnsMinusOne_whenNone() throws Exception {
        StudentQuestionItemController c = newC();
        c.setAnswers(List.of("A","B","C","D"));
        assertEquals(-1, c.getSelectedIndex());
    }

    @Test
    void getSelectedIndex_mapsButtonsTo0123() throws Exception {
        StudentQuestionItemController c = newC();
        c.setAnswers(List.of("A","B","C","D"));

        ToggleGroup g = (ToggleGroup) get(c, "group");
        var btnC = (ToggleButton) get(c, "btnC");
        g.selectToggle(btnC);

        assertEquals(2, c.getSelectedIndex());
    }
}

package com.example.cab302a1.ui;

import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.model.QuizChoiceCreate;
import com.example.cab302a1.model.QuizQuestionCreate;
import com.example.cab302a1.ui.Teacher.TeacherQuizEditorController;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeacherQuizEditorControllerTest {

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
    void buildQuiz_throws_whenTitleEmpty() throws Exception {
        TeacherQuizEditorController c = new TeacherQuizEditorController();
        set(c, "titleField", new TextField(""));
        set(c, "descArea", new TextArea("desc"));
        // empty items list
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> invokeBuild(c));
        assertTrue(ex.getMessage().toLowerCase().contains("title is required"));
    }

    @Test
    void buildQuiz_throws_whenNoQuestions() throws Exception {
        TeacherQuizEditorController c = new TeacherQuizEditorController();
        set(c, "titleField", new TextField("T"));
        set(c, "descArea", new TextArea("D"));

        // items is private final List<QuestionItemController>
        @SuppressWarnings("unchecked")
        List<QuestionItemController> items = (List<QuestionItemController>) get(c, "items");
        items.clear();

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> invokeBuild(c));
        assertTrue(ex.getMessage().toLowerCase().contains("at least one question"));
    }

    @Test
    void buildQuiz_success_withStubItem() throws Exception {
        TeacherQuizEditorController c = new TeacherQuizEditorController();
        set(c, "titleField", new TextField("My Quiz"));
        set(c, "descArea", new TextArea("About..."));

        @SuppressWarnings("unchecked")
        List<QuestionItemController> items = (List<QuestionItemController>) get(c, "items");
        items.clear();
        items.add(new StubItem());

        Quiz q = invokeBuild(c);
        assertEquals("My Quiz", q.getTitle());
        assertEquals("About...", q.getDescription());
        assertEquals(1, q.getQuestions().size());
        assertEquals(4, q.getQuestions().get(0).getChoices().size());
    }

    private static Quiz invokeBuild(TeacherQuizEditorController c) {
        try {
            Method m = TeacherQuizEditorController.class.getDeclaredMethod("buildQuizFromUI");
            m.setAccessible(true);
            return (Quiz) m.invoke(c);
        } catch (java.lang.reflect.InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException re) {
                throw re;
            }
            if (cause instanceof Error err) {
                throw err;
            }
            throw new RuntimeException(cause);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    // Stub for fast unit test
    static class StubItem extends QuestionItemController {
        @Override
        public com.example.cab302a1.model.QuizQuestionCreate toQuestion() {
            QuizQuestionCreate q = new QuizQuestionCreate();
            q.setQuestionText("Q?");
            q.getChoices().add(new QuizChoiceCreate("A", false));
            q.getChoices().add(new QuizChoiceCreate("B", true));
            q.getChoices().add(new QuizChoiceCreate("C", false));
            q.getChoices().add(new QuizChoiceCreate("D", false));
            return q;
        }
    }
}

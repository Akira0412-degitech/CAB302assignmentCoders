package com.example.cab302a1.ui;

import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.ui.page.student.StudentTakeQuizController;
import com.example.cab302a1.ui.view.components.question.StudentQuestionItemController;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class StudentTakeQuizControllerTest {

    @BeforeAll
    static void initToolkit() {
        try { Platform.startup(() -> {}); } catch (IllegalStateException ignored) {}
    }

    private static void runFxAndWait(Runnable action) {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try { action.run(); }
            finally { latch.countDown(); }
        });
        try {
            if (!latch.await(5, TimeUnit.SECONDS)) {
                fail("FX action timed out");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("FX action interrupted");
        }
    }

    private static void set(Object target, String field, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(target, value);
    }
    @SuppressWarnings("unchecked")
    private static List<StudentQuestionItemController> getItems(StudentTakeQuizController c) throws Exception {
        Field f = StudentTakeQuizController.class.getDeclaredField("itemControllers");
        f.setAccessible(true);
        return (List<StudentQuestionItemController>) f.get(c);
    }

    @Test
    void doneButton_callsOnSubmit_withSelectedIndexes() throws Exception {
        StudentTakeQuizController c = new StudentTakeQuizController();
        AtomicReference<List<Integer>> captured = new AtomicReference<>();

        runFxAndWait(() -> {
            try {
                // --- UI wiring on FX thread ---
                set(c, "titleLabel", new Label());
                set(c, "descriptionLabel", new Label());
                set(c, "questionsBox", new VBox());
                Button done = new Button();
                set(c, "doneButton", done);
                set(c, "onSubmit", (Consumer<List<Integer>>) captured::set);

                // Stage must be created on FX thread
                set(c, "stage", new Stage());

                // call private loadQuiz(Quiz)
                Method load = StudentTakeQuizController.class.getDeclaredMethod("loadQuiz", Quiz.class);
                load.setAccessible(true);
                try {
                    load.invoke(c, new Quiz()); // registers doneButton handler
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                // Swap internal items to stubs
                List<StudentQuestionItemController> items = getItems(c);
                items.clear();
                items.add(new StubItem(0));
                items.add(new StubItem(-1));
                items.add(new StubItem(3));

                // fire "Done"
                done.fire();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        // then
        List<Integer> got = captured.get();
        assertNotNull(got, "onSubmit should be called");
        assertEquals(List.of(0, -1, 3), got);
    }

    // simple stub returning fixed selected index
    static class StubItem extends StudentQuestionItemController {
        private final int idx;
        StubItem(int i) { this.idx = i; }
        @Override public int getSelectedIndex() { return idx; }
    }
}

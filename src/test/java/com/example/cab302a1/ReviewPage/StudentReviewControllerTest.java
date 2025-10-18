package com.example.cab302a1.ReviewPage;

import com.example.cab302a1.dao.ReviewDao;
import com.example.cab302a1.model.QuizReview;
import com.example.cab302a1.model.User;
import com.example.cab302a1.ui.page.review.student.StudentReviewController;
import com.example.cab302a1.util.Session;
import javafx.application.Platform;
import javafx.scene.control.*;
import org.junit.jupiter.api.AfterAll; // <-- NEW IMPORT
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentReviewControllerTest {

    private StudentReviewController controller;
    private ReviewDao mockReviewDao;

    // Test data constants
    private static final int TEST_STUDENT_ID = 5001;
    private List<QuizReview> mockQuizzes;
    private User testUser;

    // CRITICAL FIX: Make the static mock static and manage it for the entire class.
    private static MockedStatic<Session> sessionMock;

    @BeforeAll
    static void initToolkitAndMockSession() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Already started
        }

        // Open the static mock once for all tests
        sessionMock = mockStatic(Session.class);
    }

    @AfterAll
    static void closeMockSession() {
        // Close the static mock after all tests are done
        if (sessionMock != null) {
            sessionMock.close();
        }
    }


    /**
     * Utility method to inject mocks into private fields using Reflection.
     */
    private static void injectMock(Object target, String fieldName, Object mock) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, mock);
    }

    @BeforeEach
    void setUp() throws Exception {
        // 1. Setup Mock Data
        controller = new StudentReviewController();
        mockReviewDao = mock(ReviewDao.class);
        testUser = new User(TEST_STUDENT_ID, "Alice", "alice@test.com", "student", null);

        mockQuizzes = List.of(
                // Constructor: attemptId, quizId, userId, quizName, score, total, feedback
                new QuizReview(1, 101, TEST_STUDENT_ID, "Intro to Java", 15, 20, "Excellent work."),
                new QuizReview(2, 102, TEST_STUDENT_ID, "Adv Databases", 8, 10, null)
        );

        // Mock DAO
        when(mockReviewDao.getAllAttemptsById(TEST_STUDENT_ID)).thenReturn(mockQuizzes);
        when(mockReviewDao.getAllAttemptsById(anyInt())).thenReturn(List.of());

        // 2. Inject Mocks into the Controller
        injectMock(controller, "reviewDao", mockReviewDao);

        // 3. Inject ALL FXML-mapped UI controls
        controller.studentQuizTable = new TableView<>();
        controller.quizNameCol = new TableColumn<>("Quiz");
        controller.scoreCol = new TableColumn<>("Score");
        controller.feedbackCol = new TableColumn<>("View Feedback");
        controller.resultCol = new TableColumn<>("View Result");

        controller.studentQuizTable.getColumns().addAll(
                controller.quizNameCol,
                controller.scoreCol,
                controller.feedbackCol,
                controller.resultCol
        );

        // 4. Set Mock Behavior (must be done before initialize is called on FX thread)
        // Since sessionMock is static, we only set the behavior here for the current test.
        sessionMock.when(Session::getCurrentUser).thenReturn(testUser);

        // 5. Initialize Controller on FX Thread and wait for it
        final CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // Call initialize()
                controller.initialize(null, null);
            } catch (Exception e) {
                // If initialization fails, log/fail gracefully but still allow latch to count down
                Platform.runLater(() -> fail("Initialization failed on FX thread: " + e.getMessage()));
            } finally {
                latch.countDown();
            }
        });

        // Wait for initialization to complete
        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Controller initialization timed out.");
        }
    }

    // --- Tests ---

    @Test
    void testTablePopulatedAfterInitialization() {
        // This test validates that loadData() successfully ran using the mock session/DAO.
        assertNotNull(controller.studentQuizTable.getItems(), "TableView items list must not be null.");
        assertEquals(2, controller.studentQuizTable.getItems().size(),
                "Quiz table should contain 2 mock quiz reviews after initialization.");

        QuizReview result0 = controller.studentQuizTable.getItems().get(0);
        assertEquals("Intro to Java", result0.getQuizName());
    }

    @Test
    void testFirstRowScoreSummary() {
        // This test validates the data is bound correctly.
        assertFalse(controller.studentQuizTable.getItems().isEmpty(), "Quiz table must not be empty to check score.");
        QuizReview result = controller.studentQuizTable.getItems().get(0);

        assertEquals("15/20", result.scoreSummaryProperty().get(), "First row score summary should match mock data.");
    }

    @Test
    void testFeedbackColSetupShowsButton() {
        // This test validates the column cell factory creation on the FX thread.
        final CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // Check if the table is still empty, if the setup failed unexpectedly.
                if (controller.studentQuizTable.getItems().isEmpty()) {
                    fail("Table is empty, cannot test cell factory.");
                }
                controller.studentQuizTable.getSelectionModel().select(0);

                // Get the cell object for the feedback column of the first row
                TableCell<?, ?> cell = (TableCell<?, ?>) controller.feedbackCol.getCellFactory().call(controller.feedbackCol);
                cell.updateIndex(0); // Force the cell to populate with data for the first row

                assertNotNull(cell.getGraphic(), "Feedback column cell should contain a graphic (Button).");
                assertTrue(cell.getGraphic() instanceof Button, "Graphic should be a Button.");
                assertEquals("View Feedback", ((Button)cell.getGraphic()).getText());
            } catch (Exception e) {
                fail("Exception during testFeedbackColSetupShowsButton: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });

        // Wait for the FX thread assertion to complete
        try {
            if (!latch.await(5, TimeUnit.SECONDS)) {
                fail("testFeedbackColSetupShowsButton timed out.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    void testResultColSetupShowsButton() {
        // This test validates the column cell factory creation on the FX thread.
        final CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                if (controller.studentQuizTable.getItems().isEmpty()) {
                    fail("Table is empty, cannot test cell factory.");
                }
                controller.studentQuizTable.getSelectionModel().select(0);

                // Get the cell object for the result column of the first row
                TableCell<?, ?> cell = (TableCell<?, ?>) controller.resultCol.getCellFactory().call(controller.resultCol);
                cell.updateIndex(0);

                assertNotNull(cell.getGraphic(), "Result column cell should contain a graphic (Button).");
                assertTrue(cell.getGraphic() instanceof Button, "Graphic should be a Button.");
                assertEquals("View Result", ((Button)cell.getGraphic()).getText());
            } catch (Exception e) {
                fail("Exception during testResultColSetupShowsButton: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });

        // Wait for the FX thread assertion to complete
        try {
            if (!latch.await(5, TimeUnit.SECONDS)) {
                fail("testResultColSetupShowsButton timed out.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
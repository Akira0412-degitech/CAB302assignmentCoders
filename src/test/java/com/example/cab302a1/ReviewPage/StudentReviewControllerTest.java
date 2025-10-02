package com.example.cab302a1.ReviewPage;

import com.example.cab302a1.dao.ReviewDao;
import com.example.cab302a1.model.QuizReview;
import com.example.cab302a1.ui.StudentReviewController;
import javafx.application.Platform;
import javafx.scene.control.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StudentReviewControllerTest {

    private StudentReviewController controller;
    private ReviewDao mockReviewDao;

    // Test Data
    private List<QuizReview> mockQuizzes;

    @BeforeAll
    static void initToolkit() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // JavaFX toolkit already started
        }
    }

    /**
     * Utility method to inject mocks into private final fields (like 'reviewDao').
     * This is standard practice when testing controllers without a Dependency Injection framework.
     */
    private static void injectMock(Object target, String fieldName, Object mock) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, mock);
    }

    @BeforeEach
    void setUp() throws Exception {
        controller = new StudentReviewController();

        // 1. Setup Mock Data
        mockQuizzes = List.of(
                // Use concrete data that matches what the controller expects
                new QuizReview(201, "Intro to CSS (Mock)", 7, 10, "Great use of Flexbox!"),
                new QuizReview(202, "Advanced Java", 12, 15, null)
        );

        // 2. Setup Mock DAO
        mockReviewDao = mock(ReviewDao.class);

        // Mock the call used in loadReviewData() to return our mock data
        // We use anyInt() because the controller uses a hardcoded/mocked student ID (CURRENT_STUDENT_ID)
        when(mockReviewDao.getAllAttemptsById(Mockito.anyInt())).thenReturn(mockQuizzes);

        // 3. Inject Mock into the Controller
        injectMock(controller, "reviewDao", mockReviewDao);


        // 4. Inject ALL FXML-mapped UI controls
        // Fake sidebar buttons (prevent NPE)
        /* REMOVED: Fake Buttons
        controller.dashboardBtn = new Button();
        controller.reviewBtn = new Button();
        controller.timetableBtn = new Button();
        controller.exitBtn = new Button();
         */

        // Table + Columns (Must use correct names from the StudentReviewController.java FXML fields)
        controller.studentQuizTable = new TableView<>();
        controller.quizNameCol = new TableColumn<>("Quiz");
        controller.scoreCol = new TableColumn<>("Score");
        // Use the correct column name
        controller.viewFeedbackCol = new TableColumn<>("View Feedback");

        // Add columns to the TableView
        controller.studentQuizTable.getColumns().addAll(
                controller.quizNameCol,
                controller.scoreCol,
                controller.viewFeedbackCol
        );

        // 5. Call initialize(). This now calls loadReviewData() which uses the mock DAO.
        controller.initialize(null, null);
    }

    @Test
    void testQuizTableHasMockData() {
        assertNotNull(controller.studentQuizTable.getItems());
        assertFalse(controller.studentQuizTable.getItems().isEmpty(),
                "Student quiz table should have mock data loaded from the mocked DAO.");
        assertEquals(2, controller.studentQuizTable.getItems().size());
    }

    @Test
    void testFirstRowQuizName() {
        QuizReview result = controller.studentQuizTable.getItems().get(0);
        // Assert against the mock data
        assertEquals("Intro to CSS (Mock)", result.getQuizName());
    }

    @Test
    void testSecondRowScoreSummary() {
        QuizReview result = controller.studentQuizTable.getItems().get(1);
        // The score summary is a calculated property (12 out of 15 total)
        assertEquals("12/15", result.scoreSummaryProperty().get(), "Score summary should be calculated as 12/15.");
    }
}
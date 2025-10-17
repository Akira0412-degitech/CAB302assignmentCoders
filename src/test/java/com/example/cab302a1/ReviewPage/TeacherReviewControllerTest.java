package com.example.cab302a1.ReviewPage;

import com.example.cab302a1.dao.ReviewDao;
import com.example.cab302a1.dao.UserDao;
import com.example.cab302a1.dao.AttemptDao;
import com.example.cab302a1.model.QuizReview;
import com.example.cab302a1.model.Student;
import com.example.cab302a1.ui.TeacherReviewController;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherReviewControllerTest {

    private TeacherReviewController controller;
    private UserDao mockUserDao;
    private ReviewDao mockReviewDao;
    private AttemptDao mockAttemptDao;

    // Test data
    private static final int TEST_STUDENT_ID = 5001;
    private Student testStudent;
    private List<QuizReview> mockQuizzes;

    @BeforeAll
    static void initToolkit() {
        // Ensures the JavaFX environment is initialized once
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Already started
        }
    }

    /**
     * Utility method to inject mocks into private final fields.
     */
    private static void injectMock(Object target, String fieldName, Object mock) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, mock);
    }

    @BeforeEach
    void setUp() throws Exception {
        controller = new TeacherReviewController();

        // 1. Setup Mock Data
        testStudent = new Student(TEST_STUDENT_ID, "Alice Smith", "alice@test.com", "Student", new Timestamp(System.currentTimeMillis()));

        mockQuizzes = List.of(
                new QuizReview("Intro to Java", 15, 20, "Good attempt.", 101),
                new QuizReview("Adv Databases", 8, 10, null, 102)
        );

        // 2. Setup DAO Mocks (Mockito)
        mockUserDao = mock(UserDao.class);
        mockReviewDao = mock(ReviewDao.class);
        mockAttemptDao = mock(AttemptDao.class);

        when(mockUserDao.getAllStudents()).thenReturn(List.of(testStudent));
        when(mockReviewDao.getAllAttemptsById(TEST_STUDENT_ID)).thenReturn(mockQuizzes);
        when(mockReviewDao.getAllAttemptsById(Mockito.anyInt())).thenReturn(List.of());

        // 3. Inject Mocks into the Controller
        injectMock(controller, "userDao", mockUserDao);
        injectMock(controller, "reviewDao", mockReviewDao);
        injectMock(controller, "attemptDao", mockAttemptDao);


        // 4. Inject ALL FXML-mapped UI controls
        controller.assignReviewBtn = new Button();
        controller.studentListContainer = new VBox();
        controller.studentNameLabel = new Label();
        controller.quizTable = new TableView<>();
        controller.quizNameCol = new TableColumn<>("Quiz");
        controller.scoreCol = new TableColumn<>("Score");
        controller.resultCol = new TableColumn<>("View Result");

        controller.quizTable.getColumns().addAll(
                controller.quizNameCol,
                controller.scoreCol,
                controller.resultCol
        );

        // 5. Initialize the controller. This populates the student list and
        // sets the initial placeholder item in quizTable via loadReviewData().
        controller.initialize(null, null);
    }

    /**
     * Utility to fire the button click and reliably wait for the Platform task.
     */
    private void selectStudent() {
        final CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            Button studentBtn = (Button) controller.studentListContainer.getChildren().get(0);
            studentBtn.fire(); // Triggers the loadReviewData() call, which updates the quizTable.
            latch.countDown();
        });

        // Wait for the UI update and data load to complete
        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Student selection interrupted or timed out.");
        }
    }


    @Test
    void testStudentListPopulated() {
        // Test 1: Check if the sidebar is populated and styled correctly
        assertFalse(controller.studentListContainer.getChildren().isEmpty(),
                "Student list container should be populated after initialization.");
        assertEquals(1, controller.studentListContainer.getChildren().size());

        Button studentBtn = (Button) controller.studentListContainer.getChildren().get(0);
        assertEquals(testStudent.getUsername(), studentBtn.getText());

        assertTrue(studentBtn.getStyleClass().contains("action-button"),
                "CSS class 'action-button' should be applied to student buttons.");
    }

    @Test
    void testQuizTablePopulatedAfterStudentSelection() {
        // Test 2: Check if the correct number of quiz attempts loads after selecting a student

        // 1. Simulate student selection
        selectStudent();

        // 2. Verify data loaded
        // The table must now be populated with the mockQuizzes list.
        assertNotNull(controller.quizTable.getItems(), "TableView items list must not be null after selection.");
        assertEquals(mockQuizzes.size(), controller.quizTable.getItems().size(),
                "Quiz table should now contain 2 mock quiz reviews after student selection.");

        QuizReview result0 = controller.quizTable.getItems().get(0);
        assertEquals("Intro to Java", result0.getQuizName());
    }

    @Test
    void testFirstRowScoreSummary() {
        // Test 3: Check data binding accuracy for the score summary

        // 1. Simulate student selection
        selectStudent();

        // 2. Get the first row
        assertFalse(controller.quizTable.getItems().isEmpty(), "Quiz table must not be empty to check score.");
        QuizReview result = controller.quizTable.getItems().get(0);

        // 3. Verify the score summary property (15/20)
        assertEquals("15/20", result.scoreSummaryProperty().get(), "First row score summary should match mock data.");
    }
}
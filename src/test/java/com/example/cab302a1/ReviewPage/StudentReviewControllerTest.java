package com.example.cab302a1.ReviewPage;

import com.example.cab302a1.model.QuizResult;
import com.example.cab302a1.ui.StudentReviewController;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// KEY FIX: Use the TestFX extension to correctly manage the JavaFX environment
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class StudentReviewControllerTest {

    private StudentReviewController controller;

    // REMOVED: The manual initToolkit() method is no longer needed.
    //            @ExtendWith(ApplicationExtension.class) handles JavaFX initialization.
    /*
    @BeforeAll
    static void initToolkit() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // already started
        }
    }
    */

    @BeforeEach
    void setUp() {
        controller = new StudentReviewController();


        // 1. Inject FXML-mapped UI controls (required to prevent NullPointerException)
        controller.dashboardBtn = new Button();
        controller.reviewBtn = new Button();
        controller.timetableBtn = new Button();
        controller.exitBtn = new Button();

        // 2. Mock Table and Columns
        controller.quizTable = new TableView<>();
        controller.quizNameCol = new TableColumn<>("Quiz");
        controller.scoreCol = new TableColumn<>("Score");
        controller.viewResultCol = new TableColumn<>("View");
        controller.feedbackCol = new TableColumn<>("Feedback");

        // Add columns to the TableView to simulate FXML loading the structure
        controller.quizTable.getColumns().addAll(
                controller.quizNameCol,
                controller.scoreCol,
                controller.viewResultCol,
                controller.feedbackCol
        );

        // CLEANUP: Removed the manual quizTable.setItems(...) here.
        //             The controller.initialize() call below loads the actual mock data
        //             and overwrites this line, making it redundant.

        // 3. Call initialize(). This method is responsible for loading the data.
        controller.initialize();
    }

    @Test
    void testQuizTableHasMockData() {
        assertNotNull(controller.quizTable.getItems());
        // Asserts that the data loaded by controller.initialize() is present.
        assertFalse(controller.quizTable.getItems().isEmpty(),
                "Student quiz table should have mock data loaded by initialize()");
    }

    @Test
    void testFirstRowQuizName() {
        QuizResult result = controller.quizTable.getItems().get(0);
        // Assert the expected value loaded by the controller's initialize() method.
        assertEquals("Quiz 1", result.getQuizName());
    }

    @Test
    void testFirstRowScore() {
        QuizResult result = controller.quizTable.getItems().get(0);
        // Assert the expected value loaded by the controller's initialize() method.
        assertEquals("16/20", result.getScore());
    }
}
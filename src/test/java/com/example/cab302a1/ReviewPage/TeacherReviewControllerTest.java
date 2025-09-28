package com.example.cab302a1.ReviewPage;

import com.example.cab302a1.model.QuizResult;
import com.example.cab302a1.ui.TeacherReviewController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll; // Keep for now, but not used below
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith; // Required import for @ExtendWith
import org.testfx.framework.junit5.ApplicationExtension; // The core fix

import static org.junit.jupiter.api.Assertions.*;

// KEY FIX: Use the TestFX extension to correctly manage the JavaFX environment
@ExtendWith(ApplicationExtension.class)
class TeacherReviewControllerTest {

    private TeacherReviewController controller;

    // REMOVED: The manual initToolkit() method is no longer needed
    //            and is replaced by the @ExtendWith annotation above.
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
        controller = new TeacherReviewController();

        // 1. Inject ALL FXML-mapped UI controls (Required to prevent NullPointerException)

        // Sidebar/Action Buttons
        controller.dashboardBtn = new Button();
        controller.quizzesBtn = new Button();
        controller.reviewStudentsBtn = new Button();
        controller.exitBtn = new Button();
        controller.assignReviewBtn = new Button();

        // Student Details/Containers (Inferred fields)
        controller.studentListContainer = new VBox();
        controller.studentNameLabel = new Label();

        // TableView
        controller.quizTable = new TableView<>();

        // Table Columns (Must match the fields in TeacherReviewController.java)
        controller.quizNameCol = new TableColumn<>("Quiz");
        controller.scoreCol = new TableColumn<>("Score");
        controller.resultCol = new TableColumn<>("View Result"); // Fix for previous NPE

        // Add columns to the TableView to simulate FXML loading the structure
        controller.quizTable.getColumns().addAll(
                controller.quizNameCol,
                controller.scoreCol,
                controller.resultCol
        );

        // 2. Call initialize(). This method is now responsible for loading data.
        controller.initialize();
    }

    @Test
    void testQuizTableHasMockData() {
        assertNotNull(controller.quizTable.getItems());
        assertFalse(controller.quizTable.getItems().isEmpty(),
                "Teacher quiz table should have mock data loaded by initialize()");
    }

    @Test
    void testFirstRowQuizName() {
        QuizResult result = controller.quizTable.getItems().get(0);
        // Updated expectation from previous fix
        assertEquals("Quiz 1", result.getQuizName());
    }

    @Test
    void testFirstRowScore() {
        QuizResult result = controller.quizTable.getItems().get(0);
        // Updated expectation from previous fix
        assertEquals("16/20", result.getScore());
    }
}
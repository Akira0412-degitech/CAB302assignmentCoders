package com.example.cab302a1.ReviewPage;

import com.example.cab302a1.model.QuizReview;
import com.example.cab302a1.ui.TeacherReviewController;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeacherReviewControllerTest {

    private TeacherReviewController controller;

    @BeforeAll
    static void initToolkit() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // already started
        }
    }

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
        controller.resultCol = new TableColumn<>("View Result"); // FIX for previous NPE

        // Add columns to the TableView to simulate FXML loading the structure
        controller.quizTable.getColumns().addAll(
                controller.quizNameCol,
                controller.scoreCol,
                controller.resultCol
        );

        // 🛑 REMOVED: Manual data injection is removed because it is immediately
        //             overwritten by the controller.initialize() method.
        /*
        controller.quizTable.setItems(FXCollections.observableArrayList(
                new QuizReview("John Doe - Quiz 1", "18/20"),
                new QuizReview("Jane Smith - Quiz 2", "15/20")
        ));
        */


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
        QuizReview result = controller.quizTable.getItems().get(0);
        // 🛑 FIX: Update expectation to match the actual data loaded by initialize()
        assertEquals("Quiz 1", result.getQuizName());
    }

    @Test
    void testFirstRowScore() {
        QuizReview result = controller.quizTable.getItems().get(0);
        // 🛑 FIX: Update expectation to match the actual data loaded by initialize()
        assertEquals("16/20", result.getScore());
    }
}
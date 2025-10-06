package com.example.cab302a1.ReviewPage;

import com.example.cab302a1.model.QuizReview;
import com.example.cab302a1.ui.Student.StudentReviewController;
import javafx.application.Platform;
import javafx.scene.control.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentReviewControllerTest {

    private StudentReviewController controller;

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
        controller = new StudentReviewController();

        // Fake sidebar buttons
        controller.dashboardBtn = new Button();
        controller.reviewBtn = new Button();
        controller.timetableBtn = new Button();
        controller.exitBtn = new Button();

        // Fake table + columns
        controller.quizTable = new TableView<>();
        controller.quizNameCol = new TableColumn<>("Quiz");
        controller.scoreCol = new TableColumn<>("Score");
        controller.viewResultCol = new TableColumn<>("View");
        controller.feedbackCol = new TableColumn<>("Feedback");

        // Inject mock quiz data
//        controller.quizTable.setItems(FXCollections.observableArrayList(
//                new QuizReview("Quiz 1", 16, 20, ""),
//                new QuizReview("Quiz 2", 18, 20, "")
//        ));

        controller.initialize();
    }

    @Test
    void testQuizTableHasMockData() {
        assertNotNull(controller.quizTable.getItems());
        assertFalse(controller.quizTable.getItems().isEmpty(),
                "Student quiz table should have mock data");
    }

    @Test
    void testFirstRowQuizName() {
        QuizReview result = controller.quizTable.getItems().get(0);
        assertEquals("Quiz 1", result.getQuizName());
    }

    @Test
    void testFirstRowScore() {
        QuizReview result = controller.quizTable.getItems().get(0);
        assertEquals("16/20", result.getScore());
    }
}

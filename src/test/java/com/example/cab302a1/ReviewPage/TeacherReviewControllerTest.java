package com.example.cab302a1.ReviewPage;

import com.example.cab302a1.model.QuizResult;
import com.example.cab302a1.ui.TeacherReviewController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
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

        // Fake UI controls (prevent NPEs)
        controller.dashboardBtn = new Button();
        controller.quizzesBtn = new Button();
        controller.reviewBtn = new Button();
        controller.exitBtn = new Button();
        controller.reviewStudentsBtn = new Button();

        controller.quizTable = new TableView<>();
        controller.studentNameCol = new TableColumn<>("Student");
        controller.quizNameCol = new TableColumn<>("Quiz");
        controller.scoreCol = new TableColumn<>("Score");
        controller.viewBtnCol = new TableColumn<>("View");

        // Inject fake data
        controller.quizTable.setItems(FXCollections.observableArrayList(
                new QuizResult("John Doe - Quiz 1", "18/20"),
                new QuizResult("Jane Smith - Quiz 2", "15/20")
        ));

        controller.initialize();
    }

    @Test
    void testQuizTableHasMockData() {
        assertNotNull(controller.quizTable.getItems());
        assertFalse(controller.quizTable.getItems().isEmpty(),
                "Teacher quiz table should have mock data");
    }

    @Test
    void testFirstRowQuizName() {
        QuizResult result = controller.quizTable.getItems().get(0);
        assertEquals("John Doe - Quiz 1", result.getQuizName());
    }

    @Test
    void testFirstRowScore() {
        QuizResult result = controller.quizTable.getItems().get(0);
        assertEquals("18/20", result.getScore());
    }
}

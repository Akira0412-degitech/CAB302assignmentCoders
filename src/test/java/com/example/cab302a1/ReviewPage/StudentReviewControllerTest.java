package com.example.cab302a1.ReviewPage;

import com.example.cab302a1.ui.StudentReviewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class StudentReviewControllerTest extends ApplicationTest {

    private StudentReviewController controller;

    @Override
    public void start(Stage stage) throws Exception {
        // ✅ Use Parent, not var
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cab302a1/ReviewPage/student-review-view.fxml"));
        Parent root = loader.load();
        controller = loader.getController();

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void testQuizTableLoads() {
        assertNotNull(controller, "Controller should be loaded");
        assertNotNull(controller.getQuizTable(), "Quiz table should exist");
    }
}

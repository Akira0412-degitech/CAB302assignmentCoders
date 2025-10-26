package com.example.cab302a1.ui;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.dao.DaoFactory;
import com.example.cab302a1.dao.QuizDao;
import com.example.cab302a1.model.Student;
import com.example.cab302a1.model.Teacher;
import com.example.cab302a1.ui.page.HomeController;
import com.example.cab302a1.util.Session;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HomeControllerTest {

    @BeforeAll
    static void initToolkit() {
        try { Platform.startup(() -> {}); } catch (IllegalStateException ignored) {}
    }

    @AfterEach
    void tearDown() {
        Session.clearUser();
    }

    @Test
    void student_seesNoPlusCard() throws Exception {
        // given
        Session.setCurrentUser(new Student(2, "stu", "s@x.io", "Student", new Timestamp(System.currentTimeMillis())));
        HomeController c = new HomeController();
        var grid = new TilePane();
        var title = new Label();

        // Inject UI fields manually
        var gridField = HomeController.class.getDeclaredField("grid");
        gridField.setAccessible(true);
        gridField.set(c, grid);
        var titleField = HomeController.class.getDeclaredField("pageTitle");
        titleField.setAccessible(true);
        titleField.set(c, title);

        // Mock DB call (simulate no quizzes)
        try (MockedStatic<DaoFactory> mockedFactory = mockStatic(DaoFactory.class)) {
            QuizDao mockQuizDao = mock(QuizDao.class);
            when(mockQuizDao.getAllQuizzes()).thenReturn(new ArrayList<>());
            when(mockQuizDao.getQuizByTeacherId(anyInt())).thenReturn(new ArrayList<>());
            mockedFactory.when(DaoFactory::getQuizDao).thenReturn(mockQuizDao);

            c.refresh();
        }

        // then
        assertEquals(0, grid.getChildren().size(), "Student should not have + card or quizzes");
    }

    @Test
    void teacher_seesPlusCard() throws Exception {
        // given
        Session.setCurrentUser(new Teacher(1, "tutor", "t@x.io", "Teacher", new Timestamp(System.currentTimeMillis())));
        HomeController c = new HomeController();
        var grid = new TilePane();
        var title = new Label();

        // Inject UI fields manually
        var gridField = HomeController.class.getDeclaredField("grid");
        gridField.setAccessible(true);
        gridField.set(c, grid);
        var titleField = HomeController.class.getDeclaredField("pageTitle");
        titleField.setAccessible(true);
        titleField.set(c, title);

        // Mock CreateQuizAction (+ button)
        var createAction = mock(com.example.cab302a1.ui.action.CreateQuizAction.class);
        when(createAction.buildPlusCard()).thenReturn(new Button("+"));
        var createField = HomeController.class.getDeclaredField("createAction");
        createField.setAccessible(true);
        createField.set(c, createAction);

        // Mock DB call (simulate no quizzes)
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            Connection conn = mock(Connection.class);
            PreparedStatement ps = mock(PreparedStatement.class);
            ResultSet rs = mock(ResultSet.class);
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            when(conn.prepareStatement(anyString())).thenReturn(ps);
            when(ps.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);

            c.refresh();
        }

        // then
        boolean hasPlus = grid.getChildren().stream().anyMatch(n ->
                n instanceof Button btn && ("+".equals(btn.getText()) || btn.getStyleClass().contains("plus-card"))
        );

        assertTrue(hasPlus, "Teacher should see a '+' card for quiz creation.");
    }
}

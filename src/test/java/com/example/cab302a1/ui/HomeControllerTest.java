package com.example.cab302a1.ui;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.model.Student;
import com.example.cab302a1.model.Teacher;
import com.example.cab302a1.util.Session;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

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

    private static void invokePrivate(Object target, String name) throws Exception {
        Method m = target.getClass().getDeclaredMethod(name);
        m.setAccessible(true);
        m.invoke(target);
    }

    @Test
    void student_seesStudentTitle_andNoPlusCard() throws Exception {
        // given
        Session.setCurrentUser(new Student(2, "stu", "s@x.io", "Student", new Timestamp(System.currentTimeMillis())));
        HomeController c = new HomeController();
        // inject UI
        var grid = new TilePane();
        var title = new Label();

        var gridField = HomeController.class.getDeclaredField("grid");
        gridField.setAccessible(true);
        gridField.set(c, grid);
        var titleField = HomeController.class.getDeclaredField("pageTitle");
        titleField.setAccessible(true);
        titleField.set(c, title);

        // when: avoid DB by mocking empty result
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            Connection conn = mock(Connection.class);
            PreparedStatement ps = mock(PreparedStatement.class);
            ResultSet rs = mock(ResultSet.class);
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            when(conn.prepareStatement(anyString())).thenReturn(ps);
            when(ps.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);

            c.refresh(); // will also call setupPageTitle()
        }

        // then
        assertEquals("Your Learning Journey!", title.getText());
        assertEquals(0, grid.getChildren().size()); // no quizzes, and no + card for student
    }

    @Test
    void teacher_seesTeacherTitle_andPlusCard() throws Exception {
        // given
        Session.setCurrentUser(new Teacher(1, "tutor", "t@x.io", "Teacher", new Timestamp(System.currentTimeMillis())));
        HomeController c = new HomeController();
        var grid = new TilePane();
        var title = new Label();
        var gridField = HomeController.class.getDeclaredField("grid");
        gridField.setAccessible(true);
        gridField.set(c, grid);
        var titleField = HomeController.class.getDeclaredField("pageTitle");
        titleField.setAccessible(true);
        titleField.set(c, title);

        // when
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
        assertEquals("Build Your Next Quiz!", title.getText());
        // plus card is a Button with text starting with "+"
        boolean hasPlus = grid.getChildren().stream().anyMatch(n -> {

            if (n instanceof Button btn) {
                String t = btn.getText() == null ? "" : btn.getText().toLowerCase();
                if ("+".equals(btn.getText()) || t.contains("create")) return true;
                if (btn.getStyleClass() != null && btn.getStyleClass().contains("plus-card")) return true;
            }
            return n.getStyleClass() != null && n.getStyleClass().contains("plus-card");
        });
        assertTrue(hasPlus, "Teacher should see a plus card (text '+' or styleClass 'plus-card').");
    }
}

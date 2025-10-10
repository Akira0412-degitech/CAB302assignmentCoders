package com.example.cab302a1.dao;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.dao.jdbc.JdbcQuizDao;
import com.example.cab302a1.model.Quiz;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
public class QuizDaoTest {
    @Test
    void testGetAllQuizzesReturnsList() throws Exception {
        // Arrange
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);

        // simulate 2 rows
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getInt("quiz_id")).thenReturn(1, 2);
        when(rs.getString("title")).thenReturn("Quiz1", "Quiz2");
        when(rs.getString("description")).thenReturn("Desc1", "Desc2");
        when(rs.getInt("created_by")).thenReturn(10, 20);

        List<Quiz> result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = new QuizDao().getAllQuizzes();
        }

        // Assert
        assertEquals(2, result.size());
        assertEquals("Quiz1", result.get(0).getTitle());
        assertEquals("Quiz2", result.get(1).getTitle());
    }

    @Test
    void testGetAllQuizzesReturnsEmptyList() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false); // no rows

        List<Quiz> result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = new QuizDao().getAllQuizzes();
        }

        assertTrue(result.isEmpty());
    }

    @Test
    void testInsertQuizSuccess() throws Exception {
        QuizDao dao = new JdbcQuizDao();
        Quiz quiz = new Quiz();
        quiz.setTitle("New Quiz");
        quiz.setDescription("New Desc");
        quiz.setCreated_by(99);

        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet keys = mock(ResultSet.class);

        when(conn.prepareStatement(anyString(), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenReturn(stmt);
        when(stmt.executeUpdate()).thenReturn(1);
        when(stmt.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(true);
        when(keys.getInt(1)).thenReturn(100);

        int newId;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            newId = dao.insertQuiz(quiz);
        }

        assertEquals(100, newId);
    }

    @Test
    void testInsertQuizNoKeyReturned() throws Exception {
        QuizDao dao = new JdbcQuizDao();
        Quiz quiz = new Quiz();
        quiz.setTitle("No Key Quiz");
        quiz.setDescription("Desc");
        quiz.setCreated_by(1);

        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet keys = mock(ResultSet.class);

        when(conn.prepareStatement(anyString(), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenReturn(stmt);
        when(stmt.executeUpdate()).thenReturn(1);
        when(stmt.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(false); // no generated key

        int newId;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            newId = dao.insertQuiz(quiz);
        }

        assertEquals(-1, newId);
    }

}

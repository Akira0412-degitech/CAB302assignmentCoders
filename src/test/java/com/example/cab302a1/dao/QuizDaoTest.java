package com.example.cab302a1.dao;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.model.Quiz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class QuizDaoTest {

    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet rs;
    private ResultSet keys;

    /** Initializes common mocks before every test */
    @BeforeEach
    void setUp() throws Exception {
        conn = mock(Connection.class);
        stmt = mock(PreparedStatement.class);
        rs   = mock(ResultSet.class);
        keys = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(conn.prepareStatement(anyString(), anyInt())).thenReturn(stmt);
    }

    // --- getAllQuizzes ---

    /** ✅ Should return multiple quiz records */
    @Test
    void getAllQuizzes_returnsMultiple() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getInt("quiz_id")).thenReturn(1, 2);
        when(rs.getString("title")).thenReturn("T1", "T2");
        when(rs.getString("description")).thenReturn("D1", "D2");
        when(rs.getInt("created_by")).thenReturn(11, 22);
        when(rs.getBoolean("is_Hidden")).thenReturn(false, true);

        List<Quiz> result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getQuizDao().getAllQuizzes();
        }

        assertEquals(2, result.size());
        assertEquals("T1", result.get(0).getTitle());
        assertTrue(result.get(1).getIsHidden());
    }

    /** 🌀 Should return empty list when there are no rows */
    @Test
    void getAllQuizzes_returnsEmptyOnNoRows() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        List<Quiz> result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getQuizDao().getAllQuizzes();
        }

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /** ⚠️ Should handle SQL exception gracefully and return empty list */
    @Test
    void getAllQuizzes_handlesSQLException() throws Exception {
        when(conn.prepareStatement(anyString())).thenThrow(new SQLException("boom"));

        List<Quiz> result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getQuizDao().getAllQuizzes();
        }

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // --- insertQuiz ---

    /** ✅ Should insert quiz and return generated ID */
    @Test
    void insertQuiz_successReturnsGeneratedId() throws Exception {
        Quiz q = new Quiz();
        q.setTitle("Intro");
        q.setDescription("desc");
        q.setCreated_by(7);

        when(stmt.executeUpdate()).thenReturn(1);
        when(stmt.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(true);
        when(keys.getInt(1)).thenReturn(123);

        int id;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            id = DaoFactory.getQuizDao().insertQuiz(q);
        }

        assertEquals(123, id);
        verify(stmt).setString(1, "Intro");
        verify(stmt).setString(2, "desc");
        verify(stmt).setInt(3, 7);
        verify(stmt).executeUpdate();
    }

    /** ❌ Should return -1 when no generated key is returned */
    @Test
    void insertQuiz_noGeneratedKeyReturnsMinusOne() throws Exception {
        Quiz q = new Quiz();
        q.setTitle("NoKey");
        q.setDescription("desc");
        q.setCreated_by(1);

        when(stmt.executeUpdate()).thenReturn(1);
        when(stmt.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(false);

        int id;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            id = DaoFactory.getQuizDao().insertQuiz(q);
        }

        assertEquals(-1, id);
    }

    /** ⚠️ Should handle SQL exception and return -1 */
    @Test
    void insertQuiz_handlesSQLException() throws Exception {
        Quiz q = new Quiz();
        q.setTitle("X");
        q.setDescription("Y");
        q.setCreated_by(2);

        when(conn.prepareStatement(anyString(), anyInt())).thenThrow(new SQLException("boom"));

        int id;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            id = DaoFactory.getQuizDao().insertQuiz(q);
        }

        assertEquals(-1, id);
    }

    // --- updateQuiz ---

    /** ✅ Should update quiz successfully */
    @Test
    void updateQuiz_successExecutesUpdate() throws Exception {
        Quiz q = new Quiz();
        q.setQuizId(10);
        q.setTitle("New Title");
        q.setDescription("New Desc");

        when(stmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            DaoFactory.getQuizDao().updateQuiz(q);
        }

        verify(stmt).setString(1, "New Title");
        verify(stmt).setString(2, "New Desc");
        verify(stmt).setInt(3, 10);
        verify(stmt).executeUpdate();
    }

    /** ⚠️ Should handle SQL exception without throwing */
    @Test
    void updateQuiz_handlesSQLException() throws Exception {
        Quiz q = new Quiz();
        q.setQuizId(99);
        q.setTitle("T");
        q.setDescription("D");

        when(conn.prepareStatement(anyString())).thenThrow(new SQLException("boom"));

        assertDoesNotThrow(() -> {
            try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
                mocked.when(DBconnection::getConnection).thenReturn(conn);
                DaoFactory.getQuizDao().updateQuiz(q);
            }
        });
    }

    // --- getQuizById ---

    /** ✅ Should return quiz when found by ID */
    @Test
    void getQuizById_foundReturnsQuiz() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("quiz_id")).thenReturn(5);
        when(rs.getString("title")).thenReturn("Title5");
        when(rs.getString("description")).thenReturn("Desc5");
        when(rs.getInt("created_by")).thenReturn(42);
        when(rs.getBoolean("is_Hidden")).thenReturn(false);

        Quiz quiz;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            quiz = DaoFactory.getQuizDao().getQuizById(5);
        }

        assertNotNull(quiz);
        assertEquals(5, quiz.getQuizId());
        assertEquals("Title5", quiz.getTitle());
        assertFalse(quiz.getIsHidden());
    }

    /** 🌀 Should return null if quiz not found */
    @Test
    void getQuizById_notFoundReturnsNull() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        Quiz quiz;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            quiz = DaoFactory.getQuizDao().getQuizById(404);
        }

        assertNull(quiz);
    }

    /** ⚠️ Should handle SQL exception and return null */
    @Test
    void getQuizById_handlesSQLException() throws Exception {
        when(conn.prepareStatement(anyString())).thenThrow(new SQLException("boom"));

        Quiz quiz;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            quiz = DaoFactory.getQuizDao().getQuizById(1);
        }

        assertNull(quiz);
    }

    // --- updateQuizStatus ---

    /** ✅ Should update quiz visibility flag successfully */
    @Test
    void updateQuizStatus_successExecutesUpdate() throws Exception {
        when(stmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            DaoFactory.getQuizDao().updateQuizStatus(7, true);
        }

        verify(stmt).setBoolean(1, true);
        verify(stmt).setInt(2, 7);
        verify(stmt).executeUpdate();
    }

    /** ⚠️ Should handle SQL exception safely */
    @Test
    void updateQuizStatus_handlesSQLException() throws Exception {
        when(conn.prepareStatement(anyString())).thenThrow(new SQLException("boom"));

        assertDoesNotThrow(() -> {
            try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
                mocked.when(DBconnection::getConnection).thenReturn(conn);
                DaoFactory.getQuizDao().updateQuizStatus(7, false);
            }
        });
    }
}

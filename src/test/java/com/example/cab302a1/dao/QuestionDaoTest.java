package com.example.cab302a1.dao;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.dao.jdbc.DaoFactory;
import com.example.cab302a1.model.QuizQuestionCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuestionDaoTest {

    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet rs;
    private ResultSet keys;

    @BeforeEach
    void setUp() throws Exception {
        conn = mock(Connection.class);
        stmt = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);
        keys = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(conn.prepareStatement(anyString(), anyInt())).thenReturn(stmt);
    }

    /** ✅ getAllQuestions(): returns valid list */
    @Test
    void testGetAllQuestions_ReturnsList() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getInt("question_id")).thenReturn(1, 2);
        when(rs.getInt("quiz_id")).thenReturn(1, 1);
        when(rs.getString("statement")).thenReturn("Q1", "Q2");
        when(rs.getString("explanation")).thenReturn("E1", "E2");

        List<QuizQuestionCreate> result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getQuestionDao().getAllQuestions(1);
        }

        assertEquals(2, result.size());
        assertEquals("Q1", result.get(0).getQuestionText());
        assertEquals("Q2", result.get(1).getQuestionText());
    }

    /** 🌀 getAllQuestions(): empty list */
    @Test
    void testGetAllQuestions_EmptyList() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        List<QuizQuestionCreate> result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getQuestionDao().getAllQuestions(99);
        }

        assertTrue(result.isEmpty());
    }

    /** ⚠️ getAllQuestions(): SQL exception handled */
    @Test
    void testGetAllQuestions_SQLException() throws Exception {
        when(conn.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        List<QuizQuestionCreate> result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getQuestionDao().getAllQuestions(1);
        }

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /** ✅ insertQuestion(): success */
    @Test
    void testInsertQuestion_Success() throws Exception {
        QuizQuestionCreate q = new QuizQuestionCreate();
        q.setQuizId(1);
        q.setQuestionText("New Question");
        q.setExplanation("Explain");

        when(stmt.executeUpdate()).thenReturn(1);
        when(stmt.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(true);
        when(keys.getInt(1)).thenReturn(10);

        int result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getQuestionDao().insertQuestion(q);
        }

        assertEquals(10, result);
    }

    /** ❌ insertQuestion(): no key returned */
    @Test
    void testInsertQuestion_NoKeyReturned() throws Exception {
        QuizQuestionCreate q = new QuizQuestionCreate();
        q.setQuizId(2);
        q.setQuestionText("NoKey");
        q.setExplanation("Exp");

        when(stmt.executeUpdate()).thenReturn(1);
        when(stmt.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(false);

        int result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getQuestionDao().insertQuestion(q);
        }

        assertEquals(-1, result);
    }

    /** ⚠️ insertQuestion(): SQL exception */
    @Test
    void testInsertQuestion_SQLException() throws Exception {
        QuizQuestionCreate q = new QuizQuestionCreate();
        q.setQuizId(1);
        q.setQuestionText("Error");
        q.setExplanation("E");

        when(conn.prepareStatement(anyString(), anyInt())).thenThrow(new SQLException("DB error"));

        int result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getQuestionDao().insertQuestion(q);
        }

        assertEquals(-1, result);
    }

    /** ✅ updateQuestion(): success */
    @Test
    void testUpdateQuestion_Success() throws Exception {
        QuizQuestionCreate q = new QuizQuestionCreate();
        q.setQuestionId(5);
        q.setQuestionText("Updated text");
        q.setExplanation("Updated exp");

        when(stmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            DaoFactory.getQuestionDao().updateQuestion(q);
        }

        verify(stmt).setString(1, "Updated text");
        verify(stmt).setString(2, "Updated exp");
        verify(stmt).setInt(3, 5);
        verify(stmt).executeUpdate();
    }

    /** ⚠️ updateQuestion(): SQL exception handled */
    @Test
    void testUpdateQuestion_SQLException() throws Exception {
        QuizQuestionCreate q = new QuizQuestionCreate();
        q.setQuestionId(10);
        q.setQuestionText("Fail");
        q.setExplanation("Oops");

        when(conn.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        assertDoesNotThrow(() -> {
            try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
                mocked.when(DBconnection::getConnection).thenReturn(conn);
                DaoFactory.getQuestionDao().updateQuestion(q);
            }
        });
    }

    /** ✅ getNumQuestion(): success */
    @Test
    void testGetNumQuestion_Success() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("cnt")).thenReturn(3);

        int result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getQuestionDao().getNumQuestion(1);
        }

        assertEquals(3, result);
    }

    /** ❌ getNumQuestion(): no rows */
    @Test
    void testGetNumQuestion_NoResult() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        int result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getQuestionDao().getNumQuestion(2);
        }

        assertEquals(-1, result);
    }

    /** ⚠️ getNumQuestion(): SQL exception */
    @Test
    void testGetNumQuestion_SQLException() throws Exception {
        when(conn.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        int result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getQuestionDao().getNumQuestion(1);
        }

        assertEquals(-1, result);
    }

    /** ✅ getQuestionsByQuizId(): returns valid list */
    @Test
    void testGetQuestionsByQuizId_ReturnsList() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getInt("question_id")).thenReturn(1, 2);
        when(rs.getInt("quiz_id")).thenReturn(1, 1);
        when(rs.getString("statement")).thenReturn("Q1", "Q2");
        when(rs.getString("explanation")).thenReturn("E1", "E2");

        List<QuizQuestionCreate> result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getQuestionDao().getQuestionsByQuizId(1);
        }

        assertEquals(2, result.size());
        assertEquals("Q1", result.get(0).getQuestionText());
    }

    /** 🌀 getQuestionsByQuizId(): empty result */
    @Test
    void testGetQuestionsByQuizId_EmptyList() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        List<QuizQuestionCreate> result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getQuestionDao().getQuestionsByQuizId(1);
        }

        assertTrue(result.isEmpty());
    }

    /** ⚠️ getQuestionsByQuizId(): SQL exception */
    @Test
    void testGetQuestionsByQuizId_SQLException() throws Exception {
        when(conn.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        List<QuizQuestionCreate> result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getQuestionDao().getQuestionsByQuizId(1);
        }

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

package com.example.cab302a1.dao;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.dao.jdbc.DaoFactory;
import com.example.cab302a1.model.QuizChoiceCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OptionDaoTest {

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

    /** ✅ insertOption(): Success */
    @Test
    void testInsertOption_Success() throws Exception {
        QuizChoiceCreate choice = new QuizChoiceCreate();
        choice.setQuestion_id(1);
        choice.setText("Answer A");
        choice.setIs_correct(true);

        when(stmt.executeUpdate()).thenReturn(1);
        when(stmt.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(true);
        when(keys.getInt(1)).thenReturn(100);

        int result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getOptionDao().insertOption(choice);
        }

        assertEquals(100, result);
        verify(stmt).setInt(1, 1);
        verify(stmt).setString(2, "Answer A");
        verify(stmt).setBoolean(3, true);
    }

    /** ❌ insertOption(): No key returned */
    @Test
    void testInsertOption_NoKeyReturned() throws Exception {
        QuizChoiceCreate choice = new QuizChoiceCreate();
        choice.setQuestion_id(2);
        choice.setText("Choice B");
        choice.setIs_correct(false);

        when(stmt.executeUpdate()).thenReturn(1);
        when(stmt.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(false);

        int result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getOptionDao().insertOption(choice);
        }

        assertEquals(-1, result);
    }

    /** ⚠️ insertOption(): SQL Exception */
    @Test
    void testInsertOption_SQLException() throws Exception {
        QuizChoiceCreate choice = new QuizChoiceCreate();
        choice.setQuestion_id(3);
        choice.setText("Error");
        choice.setIs_correct(false);

        when(conn.prepareStatement(anyString(), anyInt())).thenThrow(new SQLException("DB error"));

        int result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getOptionDao().insertOption(choice);
        }

        assertEquals(-1, result);
    }

    /** ✅ getOptionsByQuestionId(): Returns list */
    @Test
    void testGetOptionsByQuestionId_ReturnsList() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getInt("option_id")).thenReturn(1, 2);
        when(rs.getInt("question_id")).thenReturn(5, 5);
        when(rs.getString("option_text")).thenReturn("A", "B");
        when(rs.getBoolean("is_correct")).thenReturn(true, false);

        List<QuizChoiceCreate> result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getOptionDao().getOptionsByQuestionId(5);
        }

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getText());
        assertTrue(result.get(0).isCorrect());
    }

    /** 🌀 getOptionsByQuestionId(): Empty result */
    @Test
    void testGetOptionsByQuestionId_EmptyList() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        List<QuizChoiceCreate> result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getOptionDao().getOptionsByQuestionId(10);
        }

        assertTrue(result.isEmpty());
    }

    /** ⚠️ getOptionsByQuestionId(): SQL Exception */
    @Test
    void testGetOptionsByQuestionId_SQLException() throws Exception {
        when(conn.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        List<QuizChoiceCreate> result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getOptionDao().getOptionsByQuestionId(99);
        }

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /** ✅ updateOption(): Success */
    @Test
    void testUpdateOption_Success() throws Exception {
        QuizChoiceCreate choice = new QuizChoiceCreate();
        choice.setOption_id(10);
        choice.setText("Updated Text");
        choice.setIs_correct(true);

        when(stmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            DaoFactory.getOptionDao().updateOption(choice);
        }

        verify(stmt).setString(1, "Updated Text");
        verify(stmt).setBoolean(2, true);
        verify(stmt).setInt(3, 10);
        verify(stmt).executeUpdate();
    }

    /** ⚠️ updateOption(): SQL Exception handled */
    @Test
    void testUpdateOption_SQLException() throws Exception {
        QuizChoiceCreate choice = new QuizChoiceCreate();
        choice.setOption_id(99);
        choice.setText("Will fail");
        choice.setIs_correct(false);

        when(conn.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        assertDoesNotThrow(() -> {
            try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
                mocked.when(DBconnection::getConnection).thenReturn(conn);
                DaoFactory.getOptionDao().updateOption(choice);
            }
        });
    }
}

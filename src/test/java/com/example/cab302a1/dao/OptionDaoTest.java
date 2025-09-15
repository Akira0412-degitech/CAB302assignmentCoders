package com.example.cab302a1.dao;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.model.QuizChoiceCreate;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OptionDaoTest {



    @Test
    void insertOption_returnsGeneratedId_whenSuccess() throws Exception {
        // Arrange
        QuizChoiceCreate choice = mock(QuizChoiceCreate.class);
        when(choice.getQuestion_id()).thenReturn(42);
        when(choice.getText()).thenReturn("Option A");
        when(choice.isAnswer()).thenReturn(true);

        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet keys = mock(ResultSet.class);

        when(conn.prepareStatement(anyString(), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenReturn(stmt);
        when(stmt.executeUpdate()).thenReturn(1);
        when(stmt.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(true);
        when(keys.getInt(1)).thenReturn(123);

        int newId;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);

            // Act
            newId = new OptionDao().insertOption(choice);
        }

        // Assert
        assertEquals(123, newId);
    }

    @Test
    void insertOption_returnsMinusOne_whenNoGeneratedKey() throws Exception {
        // Arrange
        QuizChoiceCreate choice = mock(QuizChoiceCreate.class);
        when(choice.getQuestion_id()).thenReturn(1);
        when(choice.getText()).thenReturn("No Key");
        when(choice.isAnswer()).thenReturn(false);

        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet keys = mock(ResultSet.class);

        when(conn.prepareStatement(anyString(), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenReturn(stmt);
        when(stmt.executeUpdate()).thenReturn(1);
        when(stmt.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(false); //No key returned

        int newId;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            newId = new OptionDao().insertOption(choice);
        }

        assertEquals(-1, newId);
    }



    @Test
    void getOptionsByQuestionId_returnsMappedList_whenRowsExist() throws Exception {
        // Arrange
        int questionId = 77;

        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);

        // dummy data for 2 rows
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getInt("option_id")).thenReturn(1001, 1002);
        when(rs.getInt("question_id")).thenReturn(questionId, questionId);
        when(rs.getString("option_text")).thenReturn("Opt-1", "Opt-2");
        when(rs.getBoolean("is_correct")).thenReturn(true, false);

        List<QuizChoiceCreate> result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);

            // Act
            result = new OptionDao().getOptionsByQuestionId(questionId);
        }

        // Assert
        assertEquals(2, result.size());
        assertEquals(1001, result.get(0).getOption_id());
        assertEquals("Opt-1", result.get(0).getText());
        assertTrue(result.get(0).isAnswer());  // is_correct = true

        assertEquals(1002, result.get(1).getOption_id());
        assertEquals("Opt-2", result.get(1).getText());
        assertFalse(result.get(1).isAnswer()); // is_correct = false
    }

    @Test
    void getOptionsByQuestionId_returnsEmptyList_whenNoRows() throws Exception {
        // Arrange
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false); // return false

        List<QuizChoiceCreate> result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);

            // Act
            result = new OptionDao().getOptionsByQuestionId(999);
        }

        // Assert
        assertTrue(result.isEmpty());
    }

}

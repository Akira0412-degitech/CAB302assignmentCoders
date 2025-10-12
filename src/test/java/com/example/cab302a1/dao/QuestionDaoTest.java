package com.example.cab302a1.dao;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.dao.jdbc.DaoFactory;
import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.model.QuizQuestionCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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

    @Test
    void testGetAllQuestionsReturnsList() throws Exception {
        Quiz quiz = new Quiz();
        quiz.setQuizId(1);

        // simulate 2 rows
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getInt("question_id")).thenReturn(101, 102);
        when(rs.getInt("quiz_id")).thenReturn(1, 1);
        when(rs.getString("statement")).thenReturn("Q1 text", "Q2 text");
        when(rs.getString("explanation")).thenReturn("E1", "E2");

        List<QuizQuestionCreate> result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getQuestionDao().getAllQuestions(quiz.getQuizId());
        }

        assertEquals(2, result.size());
        assertEquals("Q1 text", result.get(0).getQuestionText());
        assertEquals("Q2 text", result.get(1).getQuestionText());
    }

    @Test
    void testGetAllQuestionsReturnsEmptyList() throws Exception {
        Quiz quiz = new Quiz();
        quiz.setQuizId(99);

        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false); // no rows

        List<QuizQuestionCreate> result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getQuestionDao().getAllQuestions(quiz.getQuizId());
        }

        assertTrue(result.isEmpty());
    }

    @Test
    void testInsertQuestionSuccess() throws Exception {
        QuizQuestionCreate question = new QuizQuestionCreate();
        question.setQuiz_id(2);
        question.setQuestionText("New Question?");
        question.setExplanation("Explain it");

        when(stmt.executeUpdate()).thenReturn(1);
        when(stmt.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(true);
        when(keys.getInt(1)).thenReturn(500);

        int newId;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            newId = DaoFactory.getQuestionDao().insertQuestion(question);
        }

        assertEquals(500, newId);
        verify(stmt, times(1)).executeUpdate();
        verify(stmt, times(1)).setInt(eq(1), eq(2));
    }

    @Test
    void testInsertQuestionNoKeyReturned() throws Exception {
        QuizQuestionCreate question = new QuizQuestionCreate();
        question.setQuiz_id(2);
        question.setQuestionText("Q?");
        question.setExplanation("E");

        when(stmt.executeUpdate()).thenReturn(1);
        when(stmt.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(false); // no key

        int newId;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            newId = DaoFactory.getQuestionDao().insertQuestion(question);
        }

        assertEquals(-1, newId);
        verify(stmt, times(1)).executeUpdate();
    }
}

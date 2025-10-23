package com.example.cab302a1.dao;

import com.example.cab302a1.dao.jdbi.JdbiQuestionDao;
import com.example.cab302a1.model.QuizQuestionCreate;
import org.jdbi.v3.core.result.ResultBearing;
import org.jdbi.v3.core.result.ResultIterable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * ✅ Unit tests for {@link JdbiQuestionDao}.
 * These tests mock JDBI to verify correct bindings, logic flow, and exception handling.
 * No real database connection is required.
 */
class JdbiQuestionDaoTest extends BaseJdbiDaoTest {

    private ResultBearing mockResultBearing;
    private ResultIterable<Integer> mockIntResultIterable;

    @BeforeEach
    protected void setupMocks() {
        super.setupCommonMocks();
        mockResultBearing = mock(ResultBearing.class);
        mockIntResultIterable = mock(ResultIterable.class);
    }

    // ============================================================
    // ✅ insertQuestion()
    // ============================================================

    /** ✅ insertQuestion(): returns generated key successfully */
    @Test
    void testInsertQuestionSuccess() {
        QuizQuestionCreate question = new QuizQuestionCreate(0, 1, "Q1 text", "explanation A");

        when(mockHandle.createUpdate(any())).thenReturn(mockUpdate);
        when(mockUpdate.bind(eq("quizId"), eq(1))).thenReturn(mockUpdate);
        when(mockUpdate.bind(eq("statement"), eq("Q1 text"))).thenReturn(mockUpdate);

        when(mockUpdate.bind(eq("explanation"), eq("explanation A"))).thenReturn(mockUpdate);

        when(mockUpdate.executeAndReturnGeneratedKeys(eq("question_id"))).thenReturn(mockResultBearing);
        when(mockResultBearing.mapTo(Integer.class)).thenReturn(mockIntResultIterable);
        when(mockIntResultIterable.findOne()).thenReturn(Optional.of(77));

        int result = new JdbiQuestionDao().insertQuestion(question);

        assertEquals(77, result);
        verify(mockUpdate).bind("quizId", 1);
        verify(mockUpdate).bind("statement", "Q1 text");

        verify(mockUpdate).bind("explanation", "explanation A");
    }

    /** ❌ insertQuestion(): returns -1 when no key generated */
    @Test
    void testInsertQuestionNoGeneratedKey() {
        QuizQuestionCreate question = new QuizQuestionCreate(0, 2, "Q2 text", "explanation B");

        when(mockHandle.createUpdate(any())).thenReturn(mockUpdate);
        when(mockUpdate.bind(eq("quizId"), eq(2))).thenReturn(mockUpdate);
        when(mockUpdate.bind(eq("statement"), eq("Q2 text"))).thenReturn(mockUpdate);
        when(mockUpdate.bind(eq("type"), eq("MCQ"))).thenReturn(mockUpdate);
        when(mockUpdate.bind(eq("explanation"), eq("explanation B"))).thenReturn(mockUpdate);

        when(mockUpdate.executeAndReturnGeneratedKeys(eq("question_id"))).thenReturn(mockResultBearing);
        when(mockResultBearing.mapTo(Integer.class)).thenReturn(mockIntResultIterable);
        when(mockIntResultIterable.findOne()).thenReturn(Optional.empty());

        int result = new JdbiQuestionDao().insertQuestion(question);

        assertEquals(-1, result);
    }

    /** 💥 insertQuestion(): propagates runtime exception */
    @Test
    void testInsertQuestionThrowsException() {
        QuizQuestionCreate question = new QuizQuestionCreate(0, 1, "Q crash", "exp");
        reset(mockJdbi);
        when(mockJdbi.withHandle(any())).thenThrow(new RuntimeException("DB failure"));

        assertThrows(RuntimeException.class, () -> new JdbiQuestionDao().insertQuestion(question));
    }



    /** ❌ getAllQuestions(): returns empty list when no data */
    @Test
    void testGetAllQuestionsEmptyList() {
        when(mockHandle.createQuery(any())).thenReturn(mockQuery);
        when(mockQuery.bind(eq("quizId"), eq(99))).thenReturn(mockQuery);

        @SuppressWarnings("unchecked")
        ResultIterable<QuizQuestionCreate> mockListIterable = mock(ResultIterable.class);

        when(mockQuery.map(any(org.jdbi.v3.core.mapper.RowMapper.class))).thenReturn(mockListIterable);
        when(mockListIterable.list()).thenReturn(List.of());

        List<QuizQuestionCreate> result = new JdbiQuestionDao().getAllQuestions(99);

        assertTrue(result.isEmpty());
        verify(mockQuery).bind("quizId", 99);
    }

    /** 💥 getAllQuestions(): propagates runtime exception */
    @Test
    void testGetAllQuestionsThrowsException() {
        reset(mockJdbi);
        when(mockJdbi.withHandle(any())).thenThrow(new RuntimeException("Query failed"));
        assertThrows(RuntimeException.class, () -> new JdbiQuestionDao().getAllQuestions(1));
    }

    // ============================================================
    // ✅ updateQuestion()
    // ============================================================

    /** ✅ updateQuestion(): executes successfully */
    @Test
    void testUpdateQuestionSuccess() {
        QuizQuestionCreate question = new QuizQuestionCreate(5, 1, "Updated text", "Updated exp");

        when(mockHandle.createUpdate(any())).thenReturn(mockUpdate);
        when(mockUpdate.bind(eq("statement"), eq("Updated text"))).thenReturn(mockUpdate);
        when(mockUpdate.bind(eq("explanation"), eq("Updated exp"))).thenReturn(mockUpdate);
        when(mockUpdate.bind(eq("questionId"), eq(5))).thenReturn(mockUpdate);
        when(mockUpdate.execute()).thenReturn(1);

        new JdbiQuestionDao().updateQuestion(question);

        verify(mockHandle).createUpdate(any());
        verify(mockUpdate).bind("statement", "Updated text");
        verify(mockUpdate).bind("explanation", "Updated exp");
        verify(mockUpdate).bind("questionId", 5);
        verify(mockUpdate).execute();
    }

    /** 💥 updateQuestion(): propagates runtime exception */
    @Test
    void testUpdateQuestionThrowsException() {
        QuizQuestionCreate q = new QuizQuestionCreate(1, 1, "Boom", "exp");
        doThrow(new RuntimeException("SQL crash")).when(mockJdbi).useHandle(any());
        assertThrows(RuntimeException.class, () -> new JdbiQuestionDao().updateQuestion(q));
    }

    // ============================================================
    // ✅ getNumQuestion()
    // ============================================================

    /** ✅ getNumQuestion(): returns valid count */
    @Test
    void testGetNumQuestionReturnsValue() {
        when(mockHandle.createQuery(any())).thenReturn(mockQuery);
        when(mockQuery.bind(eq("quizId"), eq(3))).thenReturn(mockQuery);
        when(mockQuery.mapTo(Integer.class)).thenReturn(mockIntResultIterable);
        when(mockIntResultIterable.findOne()).thenReturn(Optional.of(4));

        int count = new JdbiQuestionDao().getNumQuestion(3);
        assertEquals(4, count);
    }

    /** ❌ getNumQuestion(): returns -1 when no value found */
    @Test
    void testGetNumQuestionNoResult() {
        when(mockHandle.createQuery(any())).thenReturn(mockQuery);
        when(mockQuery.bind(eq("quizId"), eq(3))).thenReturn(mockQuery);
        when(mockQuery.mapTo(Integer.class)).thenReturn(mockIntResultIterable);
        when(mockIntResultIterable.findOne()).thenReturn(Optional.empty());

        int count = new JdbiQuestionDao().getNumQuestion(3);
        assertEquals(-1, count);
    }

    /** 💥 getNumQuestion(): propagates runtime exception */
    @Test
    void testGetNumQuestionThrowsException() {
        reset(mockJdbi);
        when(mockJdbi.withHandle(any())).thenThrow(new RuntimeException("Count failed"));
        assertThrows(RuntimeException.class, () -> new JdbiQuestionDao().getNumQuestion(1));
    }

    // ============================================================
    // ✅ getQuestionsByQuizId()
    // ============================================================

    /** ✅ getQuestionsByQuizId(): returns list when found */
    @Test
    void testGetQuestionsByQuizIdReturnsList() {
        when(mockHandle.createQuery(any())).thenReturn(mockQuery);
        when(mockQuery.bind(eq("quizId"), eq(8))).thenReturn(mockQuery);

        @SuppressWarnings("unchecked")
        ResultIterable<QuizQuestionCreate> mockListIterable = mock(ResultIterable.class);


        when(mockQuery.map(any(org.jdbi.v3.core.mapper.RowMapper.class))).thenReturn(mockListIterable);
        when(mockListIterable.list()).thenReturn(List.of(new QuizQuestionCreate()));

        List<QuizQuestionCreate> result = new JdbiQuestionDao().getQuestionsByQuizId(8);

        assertEquals(1, result.size());
        verify(mockQuery).bind("quizId", 8);
    }


    /** ❌ getQuestionsByQuizId(): returns empty list when none found */
    @Test
    void testGetQuestionsByQuizIdEmptyList() {
        when(mockHandle.createQuery(any())).thenReturn(mockQuery);
        when(mockQuery.bind(eq("quizId"), eq(15))).thenReturn(mockQuery);

        @SuppressWarnings("unchecked")
        ResultIterable<QuizQuestionCreate> mockListIterable = mock(ResultIterable.class);


        when(mockQuery.map(any(org.jdbi.v3.core.mapper.RowMapper.class)))
                .thenReturn(mockListIterable);
        when(mockListIterable.list()).thenReturn(List.of());

        List<QuizQuestionCreate> result =
                new JdbiQuestionDao().getQuestionsByQuizId(15);

        assertTrue(result.isEmpty());
        verify(mockQuery).bind("quizId", 15);
    }

    /** 💥 getQuestionsByQuizId(): propagates runtime exception */
    @Test
    void testGetQuestionsByQuizIdThrowsException() {
        reset(mockJdbi);
        when(mockJdbi.withHandle(any())).thenThrow(new RuntimeException("SQL error"));
        assertThrows(RuntimeException.class, () -> new JdbiQuestionDao().getQuestionsByQuizId(10));
    }
}

package com.example.cab302a1.dao;

import com.example.cab302a1.dao.jdbi.JdbiReviewDao;
import com.example.cab302a1.dao.QuestionDao;
import com.example.cab302a1.model.QuizReview;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link JdbiReviewDao}.
 * - Verifies bindings and logic by executing the provided RowMapper with mocked ResultSet.
 * - Avoids fragile anyString()/any() usages where they caused issues before.
 */
class JdbiReviewDaoTest extends BaseJdbiDaoTest {

    private QuestionDao mockQuestionDao;

    @BeforeEach
    protected void setupMocks() {
        super.setupCommonMocks();
        mockQuestionDao = mock(QuestionDao.class);
    }

    // ----------------------------------------------------------------------
    // getAllAttemptsById(userId)
    // ----------------------------------------------------------------------

    /** ✅ returns list with one row and calls QuestionDao for totalQuestions */
    @Test
    void testGetAllAttemptsById_ReturnsList() throws Exception {
        final int userId = 42;

        // Create query for SELECT ...
        when(mockHandle.createQuery(argThat(sql -> sql != null && sql.trim().startsWith("SELECT"))))
                .thenReturn(mockQuery);
        when(mockQuery.bind("userId", userId)).thenReturn(mockQuery);

        // Mock map(RowMapper) so that list() will invoke RowMapper with a mocked ResultSet
        when(mockQuery.map(any(RowMapper.class))).thenAnswer(inv -> {
            @SuppressWarnings("unchecked")
            RowMapper<QuizReview> mapper = (RowMapper<QuizReview>) inv.getArgument(0);

            // Prepare mocked ResultSet/Context with expected columns
            ResultSet rs = mock(ResultSet.class);
            StatementContext ctx = mock(StatementContext.class);

            // Row values that the mapper will read
            when(rs.getInt("attempt_id")).thenReturn(1001);
            when(rs.getInt("quiz_id")).thenReturn(7);
            when(rs.getInt("score")).thenReturn(85);
            when(rs.getString("feedback")).thenReturn("good job");
            when(rs.getString("title")).thenReturn("Algebra");

            // QuestionDao should be called with quiz_id=7
            when(mockQuestionDao.getNumQuestion(7)).thenReturn(12);

            // Create a ResultIterable that, on list(), maps one row
            @SuppressWarnings("unchecked")
            org.jdbi.v3.core.result.ResultIterable<QuizReview> ri = mock(org.jdbi.v3.core.result.ResultIterable.class);
            when(ri.list()).thenAnswer(__ -> List.of(mapper.map(rs, ctx)));
            return ri;
        });

        // Act
        List<QuizReview> list = new JdbiReviewDao(mockQuestionDao).getAllAttemptsById(userId);

        // Assert
        assertEquals(1, list.size());
        QuizReview r = list.get(0);
        assertEquals(1001, r.getAttemptId());
        assertEquals(7, r.getQuizId());
        assertEquals(userId, r.getUserId());
        assertEquals(85, r.getScore());
        assertEquals("Algebra", r.getQuizName());
        assertEquals("good job", r.getFeedback());
        assertEquals(12, r.getTotal());

        verify(mockQuery).bind("userId", userId);
        verify(mockQuestionDao, times(1)).getNumQuestion(7);
    }

    /** ❌ returns empty list when no rows */
    @Test
    void testGetAllAttemptsById_EmptyList() {
        final int userId = 7;

        when(mockHandle.createQuery(argThat(sql -> sql != null && sql.trim().startsWith("SELECT"))))
                .thenReturn(mockQuery);
        when(mockQuery.bind("userId", userId)).thenReturn(mockQuery);

        @SuppressWarnings("unchecked")
        org.jdbi.v3.core.result.ResultIterable<QuizReview> ri = mock(org.jdbi.v3.core.result.ResultIterable.class);
        when(mockQuery.map(any(RowMapper.class))).thenReturn(ri);
        when(ri.list()).thenReturn(List.of()); // empty

        List<QuizReview> list = new JdbiReviewDao(mockQuestionDao).getAllAttemptsById(userId);

        assertTrue(list.isEmpty());
        verify(mockQuery).bind("userId", userId);
        // QuestionDao should NOT be called if there are no rows
        verify(mockQuestionDao, never()).getNumQuestion(anyInt());
    }

    /** 💥 propagates runtime exception from Jdbi.withHandle */
    @Test
    void testGetAllAttemptsById_ThrowsException() {
        reset(mockJdbi);
        when(mockJdbi.withHandle(any())).thenThrow(new RuntimeException("DB failure"));

        assertThrows(RuntimeException.class,
                () -> new JdbiReviewDao(mockQuestionDao).getAllAttemptsById(1));
    }

    // ----------------------------------------------------------------------
    // getStudentAttemptsforTeacher(userId, teacherId)
    // ----------------------------------------------------------------------

    /** ✅ returns list with one row and verifies both userId & teacherId bindings */
    @Test
    void testGetStudentAttemptsforTeacher_ReturnsList() throws Exception {
        final int userId = 5;
        final int teacherId = 9;

        when(mockHandle.createQuery(argThat(sql -> sql != null && sql.trim().startsWith("SELECT"))))
                .thenReturn(mockQuery);
        when(mockQuery.bind("userId", userId)).thenReturn(mockQuery);
        when(mockQuery.bind("teacherId", teacherId)).thenReturn(mockQuery);

        when(mockQuery.map(any(RowMapper.class))).thenAnswer(inv -> {
            @SuppressWarnings("unchecked")
            RowMapper<QuizReview> mapper = (RowMapper<QuizReview>) inv.getArgument(0);

            ResultSet rs = mock(ResultSet.class);
            StatementContext ctx = mock(StatementContext.class);

            when(rs.getInt("attempt_id")).thenReturn(2222);
            when(rs.getInt("quiz_id")).thenReturn(77);
            when(rs.getInt("score")).thenReturn(90);
            when(rs.getString("feedback")).thenReturn("great");
            when(rs.getString("title")).thenReturn("Geometry");

            when(mockQuestionDao.getNumQuestion(77)).thenReturn(20);

            @SuppressWarnings("unchecked")
            org.jdbi.v3.core.result.ResultIterable<QuizReview> ri = mock(org.jdbi.v3.core.result.ResultIterable.class);
            when(ri.list()).thenAnswer(__ -> List.of(mapper.map(rs, ctx)));
            return ri;
        });

        List<QuizReview> list =
                new JdbiReviewDao(mockQuestionDao).getStudentAttemptsforTeacher(userId, teacherId);

        assertEquals(1, list.size());
        QuizReview r = list.get(0);
        assertEquals(2222, r.getAttemptId());
        assertEquals(77, r.getQuizId());
        assertEquals(userId, r.getUserId());
        assertEquals(90, r.getScore());
        assertEquals("Geometry", r.getQuizName());
        assertEquals("great", r.getFeedback());
        assertEquals(20, r.getTotal());

        verify(mockQuery).bind("userId", userId);
        verify(mockQuery).bind("teacherId", teacherId);
        verify(mockQuestionDao, times(1)).getNumQuestion(77);
    }

    /** ❌ returns empty list for no matching rows */
    @Test
    void testGetStudentAttemptsforTeacher_EmptyList() {
        final int userId = 1;
        final int teacherId = 2;

        when(mockHandle.createQuery(argThat(sql -> sql != null && sql.trim().startsWith("SELECT"))))
                .thenReturn(mockQuery);
        when(mockQuery.bind("userId", userId)).thenReturn(mockQuery);
        when(mockQuery.bind("teacherId", teacherId)).thenReturn(mockQuery);

        @SuppressWarnings("unchecked")
        org.jdbi.v3.core.result.ResultIterable<QuizReview> ri = mock(org.jdbi.v3.core.result.ResultIterable.class);
        when(mockQuery.map(any(RowMapper.class))).thenReturn(ri);
        when(ri.list()).thenReturn(List.of()); // empty

        List<QuizReview> list =
                new JdbiReviewDao(mockQuestionDao).getStudentAttemptsforTeacher(userId, teacherId);

        assertTrue(list.isEmpty());
        verify(mockQuestionDao, never()).getNumQuestion(anyInt());
    }

    /** 💥 propagates runtime exception from Jdbi.withHandle */
    @Test
    void testGetStudentAttemptsforTeacher_ThrowsException() {
        reset(mockJdbi);
        when(mockJdbi.withHandle(any())).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class,
                () -> new JdbiReviewDao(mockQuestionDao).getStudentAttemptsforTeacher(1, 2));
    }
}

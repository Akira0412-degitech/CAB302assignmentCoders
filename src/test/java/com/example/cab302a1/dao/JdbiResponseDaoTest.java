package com.example.cab302a1.dao;

import com.example.cab302a1.dao.jdbi.JdbiResponseDao;
import com.example.cab302a1.model.QuestionResponse;
import org.jdbi.v3.core.statement.PreparedBatch;
import org.jdbi.v3.core.result.ResultIterable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * ✅ Unit tests for {@link JdbiResponseDao}.
 * These tests verify proper JDBI usage, parameter bindings, and
 * logical branching without connecting to a real database.
 */
class JdbiResponseDaoTest extends BaseJdbiDaoTest {

    private PreparedBatch mockBatch;
    private ResultIterable<Integer> mockIntResultIterable;

    @BeforeEach
    protected void setupMocks() {
        super.setupCommonMocks();
        mockBatch = mock(PreparedBatch.class);
        mockIntResultIterable = mock(ResultIterable.class);
    }

    // ============================================================
    // 1️⃣ saveResponse()
    // ============================================================

    /** ✅ saveResponse(): inserts multiple responses successfully */
    @Test
    void testSaveResponseSuccess() {
        QuestionResponse r1 = new QuestionResponse(1, 10, 101, true);
        QuestionResponse r2 = new QuestionResponse(1, 11, 102, false);
        List<QuestionResponse> responses = List.of(r1, r2);

        when(mockHandle.prepareBatch(startsWith("INSERT"))).thenReturn(mockBatch);
        when(mockBatch.bind("attemptId", 5)).thenReturn(mockBatch);
        when(mockBatch.bind("questionId", r1.getQuestion_id())).thenReturn(mockBatch);
        when(mockBatch.bind("optionId", r1.getOption_id())).thenReturn(mockBatch);
        when(mockBatch.bind("isCorrect", r1.getIs_Correct())).thenReturn(mockBatch);
        when(mockBatch.add()).thenReturn(mockBatch);
        when(mockBatch.execute()).thenReturn(new int[]{1, 1});

        // Act
        new JdbiResponseDao().saveResponse(5, responses);

        // Assert
        verify(mockHandle).prepareBatch(startsWith("INSERT"));
        verify(mockBatch, atLeastOnce()).bind(eq("attemptId"), eq(5));
        verify(mockBatch, atLeastOnce()).add();
        verify(mockBatch).execute();
    }

    /** ❌ saveResponse(): skips when list is null or empty */
    @Test
    void testSaveResponseEmptyList() {
        new JdbiResponseDao().saveResponse(1, List.of());
        new JdbiResponseDao().saveResponse(1, null);
        // No DB access expected
        verifyNoInteractions(mockHandle);
    }

    /** 💥 saveResponse(): propagates runtime exception */
    @Test
    void testSaveResponseThrowsException() {
        QuestionResponse r = new QuestionResponse(1, 1, 1, true);
        List<QuestionResponse> responses = List.of(r);

        reset(mockJdbi);
        doThrow(new RuntimeException("DB Error")).when(mockJdbi).useHandle(any());
        assertThrows(RuntimeException.class, () -> new JdbiResponseDao().saveResponse(2, responses));
    }

    // ============================================================
    // 2️⃣ calculateScoreFromResponses()
    // ============================================================

    /** ✅ calculateScoreFromResponses(): returns valid score */
    @Test
    void testCalculateScoreFromResponsesSuccess() {
        when(mockHandle.createQuery(startsWith("SELECT"))).thenReturn(mockQuery);
        when(mockQuery.bind("attemptId", 5)).thenReturn(mockQuery);
        when(mockQuery.mapTo(Integer.class)).thenReturn(mockIntResultIterable);
        when(mockIntResultIterable.findOne()).thenReturn(Optional.of(3));

        int score = new JdbiResponseDao().calculateScoreFromResponses(5);

        assertEquals(3, score);
        verify(mockQuery).bind("attemptId", 5);
    }

    /** ❌ calculateScoreFromResponses(): returns -1 when no result */
    @Test
    void testCalculateScoreFromResponsesNoResult() {
        when(mockHandle.createQuery(startsWith("SELECT"))).thenReturn(mockQuery);
        when(mockQuery.bind("attemptId", 8)).thenReturn(mockQuery);
        when(mockQuery.mapTo(Integer.class)).thenReturn(mockIntResultIterable);
        when(mockIntResultIterable.findOne()).thenReturn(Optional.empty());

        int score = new JdbiResponseDao().calculateScoreFromResponses(8);
        assertEquals(-1, score);
    }

    /** 💥 calculateScoreFromResponses(): propagates runtime exception */
    @Test
    void testCalculateScoreFromResponsesThrowsException() {
        reset(mockJdbi);
        when(mockJdbi.withHandle(any())).thenThrow(new RuntimeException("SQL fail"));
        assertThrows(RuntimeException.class, () -> new JdbiResponseDao().calculateScoreFromResponses(1));
    }

    // ============================================================
    // 3️⃣ getChosenOptionId()
    // ============================================================

    /** ✅ getChosenOptionId(): returns option id when found */
    @Test
    void testGetChosenOptionIdSuccess() {
        when(mockHandle.createQuery(startsWith("SELECT"))).thenReturn(mockQuery);
        when(mockQuery.bind("attemptId", 2)).thenReturn(mockQuery);
        when(mockQuery.bind("questionId", 10)).thenReturn(mockQuery);
        when(mockQuery.mapTo(Integer.class)).thenReturn(mockIntResultIterable);
        when(mockIntResultIterable.findOne()).thenReturn(Optional.of(99));

        int optionId = new JdbiResponseDao().getChosenOptionId(2, 10);
        assertEquals(99, optionId);
        verify(mockQuery).bind("attemptId", 2);
        verify(mockQuery).bind("questionId", 10);
    }

    /** ❌ getChosenOptionId(): returns -1 when not found */
    @Test
    void testGetChosenOptionIdNotFound() {
        when(mockHandle.createQuery(startsWith("SELECT"))).thenReturn(mockQuery);
        when(mockQuery.bind("attemptId", 2)).thenReturn(mockQuery);
        when(mockQuery.bind("questionId", 10)).thenReturn(mockQuery);
        when(mockQuery.mapTo(Integer.class)).thenReturn(mockIntResultIterable);
        when(mockIntResultIterable.findOne()).thenReturn(Optional.empty());

        int optionId = new JdbiResponseDao().getChosenOptionId(2, 10);
        assertEquals(-1, optionId);
    }

    /** 💥 getChosenOptionId(): propagates runtime exception */
    @Test
    void testGetChosenOptionIdThrowsException() {
        reset(mockJdbi);
        when(mockJdbi.withHandle(any())).thenThrow(new RuntimeException("Query failed"));
        assertThrows(RuntimeException.class, () -> new JdbiResponseDao().getChosenOptionId(1, 1));
    }
}

package com.example.cab302a1.dao;

import com.example.cab302a1.dao.jdbi.JdbiAttemptDao;
import com.example.cab302a1.dao.ResponseDao;
import org.jdbi.v3.core.result.ResultBearing;
import org.jdbi.v3.core.result.ResultIterable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * ✅ Unit tests for {@link JdbiAttemptDao}.
 * These tests mock Jdbi and verify logical behavior (binding, branching, exception handling)
 * without requiring a real database.
 */
class JdbiAttemptDaoTest extends BaseJdbiDaoTest {

    private ResponseDao mockResponseDao;
    private ResultBearing mockResultBearing;
    private ResultIterable<Integer> mockResultIterable;

    @BeforeEach
    protected void setupMocks() {
        super.setupCommonMocks();
        mockResponseDao = mock(ResponseDao.class);
        mockResultBearing = mock(ResultBearing.class);
        mockResultIterable = mock(ResultIterable.class);
    }

    /** ✅ startAttempt(): returns generated key */
    @Test
    void testStartAttemptSuccess() {
        when(mockHandle.createUpdate(anyString())).thenReturn(mockUpdate);
        when(mockUpdate.bind(anyString(), anyInt())).thenReturn(mockUpdate);
        when(mockUpdate.executeAndReturnGeneratedKeys(anyString())).thenReturn(mockResultBearing);
        when(mockResultBearing.mapTo(Integer.class)).thenReturn(mockResultIterable);
        when(mockResultIterable.findOne()).thenReturn(Optional.of(42));

        int result = new JdbiAttemptDao(mockResponseDao).startAttempt(1, 10);

        assertEquals(42, result);
        verify(mockUpdate).bind("quizId", 1);
        verify(mockUpdate).bind("userId", 10);
    }

    /** ❌ startAttempt(): returns -1 when no key generated */
    @Test
    void testStartAttemptNoGeneratedKey() {
        when(mockHandle.createUpdate(anyString())).thenReturn(mockUpdate);
        when(mockUpdate.bind(anyString(), anyInt())).thenReturn(mockUpdate);
        when(mockUpdate.executeAndReturnGeneratedKeys(anyString())).thenReturn(mockResultBearing);
        when(mockResultBearing.mapTo(Integer.class)).thenReturn(mockResultIterable);
        when(mockResultIterable.findOne()).thenReturn(Optional.empty());

        int result = new JdbiAttemptDao(mockResponseDao).startAttempt(1, 10);

        assertEquals(-1, result);
        verify(mockUpdate).bind("quizId", 1);
        verify(mockUpdate).bind("userId", 10);
    }

    /** 💥 startAttempt(): propagates runtime exception */
    @Test
    void testStartAttemptThrowsException() {
        reset(mockJdbi);
        when(mockJdbi.withHandle(any())).thenThrow(new RuntimeException("Simulated DB failure"));
        assertThrows(RuntimeException.class, () ->
                new JdbiAttemptDao(mockResponseDao).startAttempt(1, 10));
    }

    /** ✅ attemptExist(): returns true when record found */
    @Test
    void testAttemptExistTrue() {
        when(mockHandle.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.bind(eq("id"), anyInt())).thenReturn(mockQuery);
        when(mockQuery.mapTo(Integer.class)).thenReturn(mockResultIterable);
        when(mockResultIterable.findOne()).thenReturn(Optional.of(1));

        boolean result = new JdbiAttemptDao(mockResponseDao).attemptExist(5);

        assertTrue(result);
        verify(mockQuery).bind("id", 5);
    }

    /** ❌ attemptExist(): returns false when no record */
    @Test
    void testAttemptExistFalse() {
        when(mockHandle.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.bind(eq("id"), anyInt())).thenReturn(mockQuery);
        when(mockQuery.mapTo(Integer.class)).thenReturn(mockResultIterable);
        when(mockResultIterable.findOne()).thenReturn(Optional.empty());

        boolean result = new JdbiAttemptDao(mockResponseDao).attemptExist(999);

        assertFalse(result);
        verify(mockQuery).bind("id", 999);
    }

    /** ✅ endAttempt(): updates score when attempt exists */
    @Test
    void testEndAttemptSuccess() {
        JdbiAttemptDao daoSpy = spy(new JdbiAttemptDao(mockResponseDao));
        doReturn(true).when(daoSpy).attemptExist(anyInt());
        when(mockResponseDao.calculateScoreFromResponses(anyInt())).thenReturn(85);
        when(mockHandle.createUpdate(anyString())).thenReturn(mockUpdate);
        when(mockUpdate.bind(anyString(), (Object) any())).thenReturn(mockUpdate);
        when(mockUpdate.execute()).thenReturn(1);

        daoSpy.endAttempt(10);

        verify(mockUpdate).bind("score", 85);
        verify(mockUpdate).bind("id", 10);
        verify(mockUpdate).execute();
    }

    /** ❌ endAttempt(): skips update when attempt doesn't exist */
    @Test
    void testEndAttemptAttemptDoesNotExist() {
        JdbiAttemptDao daoSpy = spy(new JdbiAttemptDao(mockResponseDao));
        doReturn(false).when(daoSpy).attemptExist(anyInt());

        daoSpy.endAttempt(99);

        verify(mockResponseDao, never()).calculateScoreFromResponses(anyInt());
        verify(mockHandle, never()).createUpdate(anyString());
    }

    /** ✅ getScore(): returns value when record exists */
    @Test
    void testGetScoreReturnsValue() {
        when(mockHandle.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.bind(eq("quizId"), anyInt())).thenReturn(mockQuery);
        when(mockQuery.bind(eq("userId"), anyInt())).thenReturn(mockQuery);
        when(mockQuery.mapTo(Integer.class)).thenReturn(mockResultIterable);
        when(mockResultIterable.findOne()).thenReturn(Optional.of(85));

        Integer score = new JdbiAttemptDao(mockResponseDao).getScore(1, 10);

        assertEquals(85, score);
        verify(mockQuery).bind("quizId", 1);
        verify(mockQuery).bind("userId", 10);
    }

    /** ❌ getScore(): returns null when no result found */
    @Test
    void testGetScoreNoRowReturnsNull() {
        when(mockHandle.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.bind(eq("quizId"), anyInt())).thenReturn(mockQuery);
        when(mockQuery.bind(eq("userId"), anyInt())).thenReturn(mockQuery);
        when(mockQuery.mapTo(Integer.class)).thenReturn(mockResultIterable);
        when(mockResultIterable.findOne()).thenReturn(Optional.empty());

        Integer score = new JdbiAttemptDao(mockResponseDao).getScore(9, 77);

        assertNull(score);
        verify(mockQuery).bind("quizId", 9);
        verify(mockQuery).bind("userId", 77);
    }

    /** ✅ hasCompleted(): returns true when count > 0 */
    @Test
    void testHasCompletedTrue() {
        // Arrange
        when(mockHandle.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.bind(eq("quizId"), anyInt())).thenReturn(mockQuery);
        when(mockQuery.bind(eq("userId"), anyInt())).thenReturn(mockQuery);
        when(mockQuery.mapTo(Integer.class)).thenReturn(mockResultIterable);
        when(mockResultIterable.findOne()).thenReturn(Optional.of(2)); // COUNT(*) = 2 → true

        // Act
        boolean result = new JdbiAttemptDao(mockResponseDao).hasCompleted(1, 10);

        // Assert
        assertTrue(result);
        verify(mockQuery).bind("quizId", 1);
        verify(mockQuery).bind("userId", 10);
    }

    /** ❌ hasCompleted(): returns false when count = 0 */
    @Test
    void testHasCompletedFalse() {
        // Arrange
        when(mockHandle.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.bind(eq("quizId"), anyInt())).thenReturn(mockQuery);
        when(mockQuery.bind(eq("userId"), anyInt())).thenReturn(mockQuery);
        when(mockQuery.mapTo(Integer.class)).thenReturn(mockResultIterable);
        when(mockResultIterable.findOne()).thenReturn(Optional.of(0)); // COUNT(*) = 0 → false

        // Act
        boolean result = new JdbiAttemptDao(mockResponseDao).hasCompleted(5, 22);

        // Assert
        assertFalse(result);
        verify(mockQuery).bind("quizId", 5);
        verify(mockQuery).bind("userId", 22);
    }

    /** ✅ updateFeedback(): executes update with correct parameters */
    @Test
    void testUpdateFeedbackSuccess() {
        // Arrange
        when(mockHandle.createUpdate(anyString())).thenReturn(mockUpdate);
        when(mockUpdate.bind(anyString(), anyString())).thenReturn(mockUpdate); // for feedback
        when(mockUpdate.bind(anyString(), anyInt())).thenReturn(mockUpdate);    // for id
        when(mockUpdate.execute()).thenReturn(1);

        // Act
        new JdbiAttemptDao(mockResponseDao).updateFeedback(42, "Well done!");

        // Assert
        verify(mockHandle).createUpdate(contains("UPDATE quiz_attempts SET feedback"));
        verify(mockUpdate).bind("feedback", "Well done!");
        verify(mockUpdate).bind("id", 42);
        verify(mockUpdate).execute();
    }


    /** 💥 updateFeedback(): propagates runtime exception */
    @Test
    void testUpdateFeedbackThrowsException() {
        // Arrange
        doThrow(new RuntimeException("DB failure"))
                .when(mockJdbi)
                .useHandle(any()); // ✅ doThrow + useHandle

        // Act + Assert
        assertThrows(RuntimeException.class, () ->
                new JdbiAttemptDao(mockResponseDao).updateFeedback(7, "error test"));
    }


    /** ✅ getAttemptId(): returns latest completed attempt */
    @Test
    void testGetAttemptIdReturnsValue() {
        // Arrange
        when(mockHandle.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.bind(eq("quizId"), anyInt())).thenReturn(mockQuery);
        when(mockQuery.bind(eq("userId"), anyInt())).thenReturn(mockQuery);
        when(mockQuery.mapTo(Integer.class)).thenReturn(mockResultIterable);
        when(mockResultIterable.findOne()).thenReturn(Optional.of(99));

        // Act
        Integer attemptId = new JdbiAttemptDao(mockResponseDao).getAttemptId(3, 15);

        // Assert
        assertEquals(99, attemptId);
        verify(mockQuery).bind("quizId", 3);
        verify(mockQuery).bind("userId", 15);
    }

    /** ❌ getAttemptId(): returns null when no attempt exists */
    @Test
    void testGetAttemptIdNoResultReturnsNull() {
        // Arrange
        when(mockHandle.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.bind(eq("quizId"), anyInt())).thenReturn(mockQuery);
        when(mockQuery.bind(eq("userId"), anyInt())).thenReturn(mockQuery);
        when(mockQuery.mapTo(Integer.class)).thenReturn(mockResultIterable);
        when(mockResultIterable.findOne()).thenReturn(Optional.empty());

        // Act
        Integer attemptId = new JdbiAttemptDao(mockResponseDao).getAttemptId(4, 77);

        // Assert
        assertNull(attemptId);
        verify(mockQuery).bind("quizId", 4);
        verify(mockQuery).bind("userId", 77);
    }

    /** 💥 getAttemptId(): propagates runtime exception */
    @Test
    void testGetAttemptIdThrowsException() {
        // Arrange
        reset(mockJdbi);
        when(mockJdbi.withHandle(any(org.jdbi.v3.core.HandleCallback.class)))
                .thenThrow(new RuntimeException("DB failure"));

        // Act + Assert
        assertThrows(RuntimeException.class, () ->
                new JdbiAttemptDao(mockResponseDao).getAttemptId(2, 9));
    }


}

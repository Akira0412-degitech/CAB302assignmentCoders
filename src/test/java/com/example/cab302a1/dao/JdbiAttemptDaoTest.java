package com.example.cab302a1.dao;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.dao.jdbi.JdbiAttemptDao;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Update;
import org.jdbi.v3.core.result.ResultBearing;
import org.jdbi.v3.core.result.ResultIterable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * JDBI-based tests for startAttempt() in {@link JdbiAttemptDao}.
 */
class JdbiAttemptDaoTest {

    private Jdbi mockJdbi;
    private Handle mockHandle;
    private Update mockUpdate;
    private ResponseDao mockResponseDao;
    private ResultBearing mockResultBearing;
    private ResultIterable<Integer> mockResultIterable;

    @BeforeEach
    void setUp() {
        mockJdbi = mock(Jdbi.class);
        mockHandle = mock(Handle.class);
        mockUpdate = mock(Update.class);
        mockResponseDao = mock(ResponseDao.class);
        mockResultBearing = mock(ResultBearing.class);
        mockResultIterable = mock(ResultIterable.class);
    }

    // Utility: mock withHandle() to return value through our mock handle
    private void mockWithHandleReturning() {
        when(mockJdbi.withHandle(any())).thenAnswer(invocation -> {
            var callback = (org.jdbi.v3.core.HandleCallback<Object, Exception>) invocation.getArgument(0);
            return callback.withHandle(mockHandle);
        });
    }

    /** ✅ startAttempt(): should return generated key when successful */
    @Test
    void testStartAttemptSuccess() {
        mockWithHandleReturning();

        when(mockHandle.createUpdate(anyString())).thenReturn(mockUpdate);
        when(mockUpdate.bind(anyString(), anyInt())).thenReturn(mockUpdate);
        when(mockUpdate.bind(anyString(), any(Object.class))).thenReturn(mockUpdate);
        when(mockUpdate.executeAndReturnGeneratedKeys("attempt_id")).thenReturn(mockResultBearing);
        when(mockResultBearing.mapTo(Integer.class)).thenReturn(mockResultIterable);
        when(mockResultIterable.findOne()).thenReturn(Optional.of(42));

        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getJdbi).thenReturn(mockJdbi);
            int result = new JdbiAttemptDao(mockResponseDao).startAttempt(1, 10);
            assertEquals(42, result);
        }

        verify(mockHandle).createUpdate(contains("INSERT INTO quiz_attempts"));
        verify(mockUpdate).bind("quizId", 1);
        verify(mockUpdate).bind("userId", 10);
        verify(mockResultBearing).mapTo(Integer.class);
    }

    /** ❌ startAttempt(): should return -1 when no key is generated */
    @Test
    void testStartAttemptNoGeneratedKey() {
        mockWithHandleReturning();

        when(mockHandle.createUpdate(anyString())).thenReturn(mockUpdate);
        when(mockUpdate.bind(anyString(), anyInt())).thenReturn(mockUpdate);
        when(mockUpdate.bind(anyString(), any(Object.class))).thenReturn(mockUpdate);
        when(mockUpdate.executeAndReturnGeneratedKeys("attempt_id")).thenReturn(mockResultBearing);
        when(mockResultBearing.mapTo(Integer.class)).thenReturn(mockResultIterable);
        when(mockResultIterable.findOne()).thenReturn(Optional.empty());


        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getJdbi).thenReturn(mockJdbi);
            int result = new JdbiAttemptDao(mockResponseDao).startAttempt(1, 10);
            assertEquals(-1, result);
        }
    }

    /** 💥 startAttempt(): should handle exception gracefully */
    @Test
    void testStartAttemptHandlesException() {
        when(mockJdbi.withHandle(any())).thenThrow(new RuntimeException("DB error"));

        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getJdbi).thenReturn(mockJdbi);
            assertThrows(RuntimeException.class, () ->
                    new JdbiAttemptDao(mockResponseDao).startAttempt(1, 10));
        }
    }

}

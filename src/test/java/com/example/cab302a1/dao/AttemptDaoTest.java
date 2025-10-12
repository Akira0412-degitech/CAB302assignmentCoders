package com.example.cab302a1.dao;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.dao.jdbc.DaoFactory;
import com.example.cab302a1.dao.jdbc.JdbcAttemptDao;
import com.example.cab302a1.dao.jdbc.JdbcResponseDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link JdbcAttemptDao}.
 *
 * This class verifies correct SQL execution, data mapping,
 * and conditional logic using Mockito to mock database behavior.
 */
class AttemptDaoTest {

    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet rs;

    @BeforeEach
    void setUp() throws Exception {
        conn = mock(Connection.class);
        stmt = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(conn.prepareStatement(anyString(), anyInt())).thenReturn(stmt);
    }

    /** ✅ startAttempt(): should return generated key when successful */
    @Test
    void testStartAttemptSuccess() throws Exception {
        ResultSet keys = mock(ResultSet.class);

        when(stmt.executeUpdate()).thenReturn(1);
        when(stmt.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(true);
        when(keys.getInt(1)).thenReturn(42);

        int result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getAttemptDao().startAttempt(1, 10);
        }

        assertEquals(42, result);
        verify(stmt).setInt(1, 1);
        verify(stmt).setInt(2, 10);
    }

    /** ❌ startAttempt(): should return -1 when no key is generated */
    @Test
    void testStartAttemptNoGeneratedKey() throws Exception {
        ResultSet keys = mock(ResultSet.class);

        when(stmt.executeUpdate()).thenReturn(1);
        when(stmt.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(false);

        int result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getAttemptDao().startAttempt(1, 10);
        }

        assertEquals(-1, result);
    }

    /** ✅ attemptExist(): should return true if a row exists */
    @Test
    void testAttemptExistTrue() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);

        boolean exists;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            exists = DaoFactory.getAttemptDao().attemptExist(7);
        }

        assertTrue(exists);
    }

    /** ❌ attemptExist(): should return false if no row found */
    @Test
    void testAttemptExistFalse() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        boolean exists;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            exists = DaoFactory.getAttemptDao().attemptExist(99);
        }

        assertFalse(exists);
    }

    /** ✅ getScore(): should return valid score when row exists */
    @Test
    void testGetScoreSuccess() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("score")).thenReturn(80);

        Integer score;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            score = DaoFactory.getAttemptDao().getScore(2, 12);
        }

        assertEquals(80, score);
    }

    /** ❌ getScore(): should return null when no rows found */
    @Test
    void testGetScoreNoRows() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        Integer score;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            score = DaoFactory.getAttemptDao().getScore(2, 12);
        }

        assertNull(score);
    }

    /** ✅ hasCompleted(): should return true when count > 0 */
    @Test
    void testHasCompletedTrue() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("cnt")).thenReturn(3); // count > 0

        boolean result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getAttemptDao().hasCompleted(1, 5);
        }

        assertTrue(result);
    }

    /** ❌ hasCompleted(): should return false when count == 0 */
    @Test
    void testHasCompletedFalse() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("cnt")).thenReturn(0);

        boolean result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getAttemptDao().hasCompleted(1, 5);
        }

        assertFalse(result);
    }

    /** ✅ updateFeedback(): should execute SQL with correct parameters */
    @Test
    void testUpdateFeedbackExecutes() throws Exception {
        when(stmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            DaoFactory.getAttemptDao().updateFeedback(10, "Good job");
        }

        verify(stmt).setString(1, "Good job");
        verify(stmt).setInt(2, 10);
        verify(stmt).executeUpdate();
    }

    /** ✅ getAttemptId(): should return ID if a record exists */
    @Test
    void testGetAttemptIdSuccess() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("attempt_id")).thenReturn(33);

        Integer id;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            id = DaoFactory.getAttemptDao().getAttemptId(4, 20);
        }

        assertEquals(33, id);
    }

    /** ❌ getAttemptId(): should return null if no record found */
    @Test
    void testGetAttemptIdNoResult() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        Integer id;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            id = DaoFactory.getAttemptDao().getAttemptId(4, 20);
        }

        assertNull(id);
    }

    /** ⚙️ endAttempt(): should skip update if attempt does not exist */
    @Test
    void testEndAttemptWhenNotExist() throws Exception {
        AttemptDao dao = spy(DaoFactory.getAttemptDao());
        doReturn(false).when(dao).attemptExist(anyInt());

        dao.endAttempt(10);

        // Should not execute DB update since attempt does not exist
        verify(dao, times(1)).attemptExist(10);
    }

    /** ✅ endAttempt(): should update score when attempt exists */
    @Test
    void testEndAttemptSuccess() throws Exception {
        ResponseDao mockResponseDao = mock(ResponseDao.class);
        JdbcAttemptDao dao = spy(new JdbcAttemptDao(mockResponseDao));

        // Mock existence check and score calculation
        doReturn(true).when(dao).attemptExist(anyInt());
        when(mockResponseDao.calculateScoreFromResponses(anyInt())).thenReturn(95);

        when(stmt.executeUpdate()).thenReturn(1);
        when(conn.prepareStatement(anyString())).thenReturn(stmt);

        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            dao.endAttempt(20);
        }

        verify(stmt).executeUpdate();
    }
}

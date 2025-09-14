package com.example.cab302a1.dao;

import com.example.cab302a1.DBconnection;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttemptDaoTest {

    @Test
    void testStartAttemptSuccess() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet keys = mock(ResultSet.class);

        when(conn.prepareStatement(anyString(), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenReturn(stmt);
        when(stmt.executeUpdate()).thenReturn(1);
        when(stmt.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(true);
        when(keys.getInt(1)).thenReturn(101);

        int result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = new AttemptDao().startAttempt(1, 10);
        }

        assertEquals(101, result);
    }

    @Test
    void testStartAttemptNoKey() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet keys = mock(ResultSet.class);

        when(conn.prepareStatement(anyString(), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenReturn(stmt);
        when(stmt.executeUpdate()).thenReturn(1);
        when(stmt.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(false); //No key returned

        int result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = new AttemptDao().startAttempt(1, 10);
        }

        assertEquals(-1, result);
    }

    @Test
    void testGetScoreSuccess() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("score")).thenReturn(90);

        Integer result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = new AttemptDao().getScore(1, 10);
        }

        assertNotNull(result);
        assertEquals(90, result);
    }

    @Test
    void testGetScoreNoRows() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false); // No rows affected

        Integer result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = new AttemptDao().getScore(1, 10);
        }

        assertNull(result);
    }


    @Test
    void testHasCompletedTrue() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("cnt")).thenReturn(2); //2 rows counted

        boolean result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = new AttemptDao().hasCompleted(1, 10);
        }

        assertTrue(result);
    }

    @Test
    void testHasCompletedFalse() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("cnt")).thenReturn(0); // 0 rows counted

        boolean result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = new AttemptDao().hasCompleted(1, 10);
        }

        assertFalse(result);
    }

}

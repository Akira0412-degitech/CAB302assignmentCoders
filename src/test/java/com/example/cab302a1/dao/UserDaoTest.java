package com.example.cab302a1.dao;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.model.Student;
import com.example.cab302a1.model.Teacher;
import com.example.cab302a1.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDaoTest {

    @Test
    void testGetUserByIdReturnsTeacher() throws Exception {
        // Arrange: mock JDBC
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("role")).thenReturn("Teacher");
        when(rs.getInt("user_id")).thenReturn(1);
        when(rs.getString("email")).thenReturn("teacher@example.com");
        when(rs.getString("password")).thenReturn("secret");
        when(rs.getTimestamp("created_at")).thenReturn(new Timestamp(System.currentTimeMillis()));

        User result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = new UserDao().getUserById(1);
        }

        // Assert: just check the mapping logic
        assertNotNull(result);
        assertTrue(result instanceof Teacher);
        assertEquals("teacher@example.com", result.getEmail());
        assertEquals(1, result.getUser_id());
        assertEquals("secret", result.getPassword());
    }

    @Test
    void testGetUserByIdReturnsStudent() throws Exception{
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("role")).thenReturn("Student");
        when(rs.getInt("user_id")).thenReturn(2);
        when(rs.getString("email")).thenReturn("student@example.com");
        when(rs.getString("password")).thenReturn("secret");
        when(rs.getTimestamp("created_at")).thenReturn(new Timestamp(System.currentTimeMillis()));

        User result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = new UserDao().getUserById(2);
        }

        // Assert: just check the mapping logic
        assertNotNull(result);
        assertTrue(result instanceof Student);
        assertEquals("student@example.com", result.getEmail());
        assertEquals(2, result.getUser_id());
        assertEquals("secret", result.getPassword());
    }
    @Test
    void testExistsByEmailReturnsTrue() throws Exception {
        // Arrange
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true); // simulate row exists

        boolean result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);

            // Act
            UserDao dao = new UserDao();
            result = dao.existsByEmail("test@example.com");
        }

        // Assert
        assertTrue(result);
    }

    @Test
    void testExistsByEmailReturnsFalse() throws Exception {
        // Arrange
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false); // simulate row exists

        boolean result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);

            // Act
            UserDao dao = new UserDao();
            result = dao.existsByEmail("test@example.com");
        }

        // Assert
        assertFalse(result);
    }

}

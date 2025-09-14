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

    @Test
    void testExistsByEmailThrowsSQLException() throws Exception {
        // Arrange
        Connection conn = mock(Connection.class);

        // Make prepareStatement throw SQLException
        when(conn.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        boolean result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);

            // Act
            UserDao dao = new UserDao();
            result = dao.existsByEmail("error@example.com");
        }

        // Assert
        assertFalse(result, "When SQLException occurs, should return false");
    }

    @Test
    void testSignUpUserAlreadyExists() {
        UserDao dao = spy(new UserDao());

        // Mock existsByEmail → true
        doReturn(true).when(dao).existsByEmail("exists@example.com");

        User result = dao.signUpUser("exists@example.com", "pass", "Student");

        assertNull(result, "If user already exists, should return null");
        verify(dao).existsByEmail("exists@example.com");
    }

    @Test
    void testSignUpUserSuccess() throws Exception {
        UserDao dao = spy(new UserDao());

        // Mock existsByEmail → false
        doReturn(false).when(dao).existsByEmail("new@example.com");

        // Mock JDBC chain
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet generatedKeys = mock(ResultSet.class);

        when(conn.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(stmt);
        when(stmt.executeUpdate()).thenReturn(1); // 1 row affected
        when(stmt.getGeneratedKeys()).thenReturn(generatedKeys);
        when(generatedKeys.next()).thenReturn(true);
        when(generatedKeys.getInt(1)).thenReturn(3);

        // Mock getUserById to return a Student
        User fakeUser = new Student(3, "new@example.com", "pass", "Student", null);
        doReturn(fakeUser).when(dao).getUserById(3);

        User result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);

            result = dao.signUpUser("new@example.com", "pass", "Student");
        }

        assertNotNull(result);
        assertEquals("new@example.com", result.getEmail());

    }

    @Test
    void testSignUpUserInsertFails() throws Exception {
        UserDao dao = spy(new UserDao());

        doReturn(false).when(dao).existsByEmail("fail@example.com");

        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);

        when(conn.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(stmt);
        when(stmt.executeUpdate()).thenReturn(0); // no rows inserted

        User result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);

            result = dao.signUpUser("fail@example.com", "pass", "Student");
        }

        assertNull(result, "If no rows inserted, should return null");
    }

    @Test
    void testLoginUserNotExists(){
        UserDao dao = spy(new UserDao());
        doReturn(false).when(dao).existsByEmail("nouser@example.com");

        User result = dao.login("nouser@example.com", "pass");

        assertNull(result, "If user does not exist, login should fail and return null");
    }

    @Test
    void testLoginTeacherSuccess() throws Exception{
        UserDao dao = spy(new UserDao());
        doReturn(true).when(dao).existsByEmail("teacher@example.com");

        //Mock JDBC
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("user_id")).thenReturn(4);
        when(rs.getString("email")).thenReturn("teacher@example.com");
        when(rs.getString("password")).thenReturn("secret");
        when(rs.getString("role")).thenReturn("Teacher");
        when(rs.getTimestamp("created_at")).thenReturn(new Timestamp(System.currentTimeMillis()));

        User result;
        try(MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)){
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = dao.login("teacher@example.com", "secret");
        }

        assertNotNull(result);
        assertTrue(result instanceof Teacher);
        assertEquals("teacher@example.com", result.getEmail());

    }

    @Test
    void testLoginStudentSuccess() throws Exception {
        UserDao dao = spy(new UserDao());
        doReturn(true).when(dao).existsByEmail("student@example.com");

        // Mock JDBC
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("user_id")).thenReturn(5);
        when(rs.getString("email")).thenReturn("student@example.com");
        when(rs.getString("password")).thenReturn("secret");
        when(rs.getString("role")).thenReturn("Student");
        when(rs.getTimestamp("created_at")).thenReturn(new Timestamp(System.currentTimeMillis()));

        User result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = dao.login("student@example.com", "secret");
        }

        assertNotNull(result);
        assertTrue(result instanceof Student);
        assertEquals("student@example.com", result.getEmail());

    }

    @Test
    void testLoginPasswordMismatch() throws Exception {
        UserDao dao = spy(new UserDao());
        doReturn(true).when(dao).existsByEmail("wrongpass@example.com");

        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false); // no row → password mismatch

        User result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = dao.login("wrongpass@example.com", "badpass");
        }

        assertNull(result, "If password does not match, login should return null");
    }

}

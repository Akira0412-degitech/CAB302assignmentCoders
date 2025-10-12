package com.example.cab302a1.dao;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.dao.jdbc.DaoFactory;
import com.example.cab302a1.model.Student;
import com.example.cab302a1.model.Teacher;
import com.example.cab302a1.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDaoTest {

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

    /** ✅ getUserById(): should return Teacher when role=Teacher */
    @Test
    void getUserById_ReturnsTeacher() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getString("role")).thenReturn("Teacher");
        when(rs.getInt("user_id")).thenReturn(1);
        when(rs.getString("username")).thenReturn("Alice");
        when(rs.getString("email")).thenReturn("a@x.com");
        when(rs.getTimestamp("created_at")).thenReturn(new Timestamp(System.currentTimeMillis()));

        User result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getUserDao().getUserById(1);
        }

        assertTrue(result instanceof Teacher);
        assertEquals("Alice", result.getUsername());
    }

    /** ✅ getUserById(): should return Student when role=Student */
    @Test
    void getUserById_ReturnsStudent() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getString("role")).thenReturn("Student");
        when(rs.getInt("user_id")).thenReturn(2);
        when(rs.getString("username")).thenReturn("Bob");
        when(rs.getString("email")).thenReturn("b@x.com");
        when(rs.getTimestamp("created_at")).thenReturn(new Timestamp(System.currentTimeMillis()));

        User result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getUserDao().getUserById(2);
        }

        assertTrue(result instanceof Student);
        assertEquals("Bob", result.getUsername());
    }

    /** ❌ getUserById(): should return null when user ID does not exist */
    @Test
    void getUserById_ReturnsNullWhenNotFound() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false); // No rows returned

        User result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getUserDao().getUserById(999);
        }

        assertNull(result);
    }

    /** 🌀 getAllStudents(): should return list of Student objects */
    @Test
    void getAllStudents_ReturnsList() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getInt("user_id")).thenReturn(1, 2);
        when(rs.getString("username")).thenReturn("Tom", "Jerry");
        when(rs.getString("email")).thenReturn("t@x.com", "j@x.com");
        when(rs.getString("role")).thenReturn("Student", "Student");
        when(rs.getTimestamp("created_at")).thenReturn(new Timestamp(System.currentTimeMillis()));

        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            var list = DaoFactory.getUserDao().getAllStudents();
            assertEquals(2, list.size());
        }
    }
    /** ❌ getAllStudents(): should handle SQLException and return empty list */
    @Test
    void getAllStudents_HandlesSQLExceptionGracefully() throws Exception {
        // Simulate connection.prepareStatement() throwing SQLException
        when(conn.prepareStatement(anyString())).thenThrow(new SQLException("boom"));

        List<User> result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getUserDao().getAllStudents();
        }

        // It should not crash, and instead return an empty list
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected empty list when SQL fails");
    }


    /** ✅ existsByEmail(): should return true if record exists */
    @Test
    void existsByEmail_ReturnsTrue() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);

        boolean result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getUserDao().existsByEmail("test@x.com");
        }

        assertTrue(result);
    }

    /** ❌ existsByEmail(): should return false if not found */
    @Test
    void existsByEmail_ReturnsFalse() throws Exception {
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        boolean result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = DaoFactory.getUserDao().existsByEmail("no@x.com");
        }

        assertFalse(result);
    }

    /** ✅ signUpUser(): should create and return Student when signing up as Student */
    @Test
    void signUpUser_SuccessStudent() throws Exception {
        UserDao userDao = spy(DaoFactory.getUserDao());
        // Simulate: email does not exist yet
        doReturn(false).when(userDao).existsByEmail(anyString());
        // Simulate getUserById() returning a Student
        doReturn(new Student(1, "StuUser", "stu@x.com", "Student", new Timestamp(System.currentTimeMillis())))
                .when(userDao).getUserById(anyInt());

        when(stmt.executeUpdate()).thenReturn(1);
        when(stmt.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(true);
        when(keys.getInt(1)).thenReturn(101);

        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            User user = userDao.signUpUser("StuUser", "stu@x.com", "mypw", "Student");

            assertNotNull(user);
            assertTrue(user instanceof Student);
            assertEquals("StuUser", user.getUsername());
            assertEquals("stu@x.com", user.getEmail());
        }
    }

    /** ✅ signUpUser(): should create and return Teacher when signing up as Teacher */
    @Test
    void signUpUser_SuccessTeacher() throws Exception {
        UserDao userDao = spy(DaoFactory.getUserDao());
        // Simulate: email does not exist yet
        doReturn(false).when(userDao).existsByEmail(anyString());
        // Simulate getUserById() returning a Teacher
        doReturn(new Teacher(2, "TeachUser", "teach@x.com", "Teacher", new Timestamp(System.currentTimeMillis())))
                .when(userDao).getUserById(anyInt());

        when(stmt.executeUpdate()).thenReturn(1);
        when(stmt.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(true);
        when(keys.getInt(1)).thenReturn(202);

        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            User user = userDao.signUpUser("TeachUser", "teach@x.com", "securepw", "Teacher");

            assertNotNull(user);
            assertTrue(user instanceof Teacher);
            assertEquals("TeachUser", user.getUsername());
            assertEquals("teach@x.com", user.getEmail());
        }
    }


    /** ⚠️ signUpUser(): should return null if email exists */
    @Test
    void signUpUser_AlreadyExists() throws Exception {
        UserDao userDao = spy(DaoFactory.getUserDao());
        doReturn(true).when(userDao).existsByEmail("dup@x.com");

        User result = userDao.signUpUser("Dup", "dup@x.com", "pw", "Teacher");
        assertNull(result);
    }

    /** ❌ signUpUser(): should handle SQLException gracefully */
    @Test
    void signUpUser_HandlesSQLException() throws Exception {
        UserDao dao = spy(DaoFactory.getUserDao());
        doReturn(false).when(dao).existsByEmail(anyString());

        when(conn.prepareStatement(anyString(), anyInt())).thenThrow(new SQLException("boom"));

        User result;
        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            result = dao.signUpUser("CrashUser", "x@x.com", "pw", "Student");
        }

        assertNull(result);
    }

    /** ❌ signUpUser(): should return null when no rows are inserted */
    @Test
    void signUpUser_NoRowsInserted() throws Exception {
        UserDao dao = spy(DaoFactory.getUserDao());
        doReturn(false).when(dao).existsByEmail(anyString());

        when(stmt.executeUpdate()).thenReturn(0);

        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            User result = dao.signUpUser("NoInsert", "no@x.com", "pw", "Teacher");
            assertNull(result);
        }
    }



    /** ✅ login(): should return Teacher when password matches */
    @Test
    void login_SuccessTeacher() throws Exception {
        UserDao dao = spy(DaoFactory.getUserDao());
        doReturn(true).when(dao).existsByEmail(anyString());

        String hash = BCrypt.hashpw("secret", BCrypt.gensalt());
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getString("password")).thenReturn(hash);
        when(rs.getString("role")).thenReturn("Teacher");
        when(rs.getInt("user_id")).thenReturn(5);
        when(rs.getString("username")).thenReturn("T");
        when(rs.getString("email")).thenReturn("t@x.com");
        when(rs.getTimestamp("created_at")).thenReturn(new Timestamp(System.currentTimeMillis()));

        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            User result = dao.login("t@x.com", "secret");
            assertTrue(result instanceof Teacher);
        }
    }

    /** ✅ login(): should return Student when password matches */
    @Test
    void login_SuccessStudent() throws Exception {
        UserDao dao = spy(DaoFactory.getUserDao());
        doReturn(true).when(dao).existsByEmail(anyString());

        // Create a valid BCrypt hash for the test password
        String hash = BCrypt.hashpw("mypassword", BCrypt.gensalt());
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getString("password")).thenReturn(hash);
        when(rs.getString("role")).thenReturn("Student");
        when(rs.getInt("user_id")).thenReturn(8);
        when(rs.getString("username")).thenReturn("S");
        when(rs.getString("email")).thenReturn("s@x.com");
        when(rs.getTimestamp("created_at")).thenReturn(new Timestamp(System.currentTimeMillis()));

        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            User result = dao.login("s@x.com", "mypassword");

            // Verify correct subclass and properties
            assertTrue(result instanceof Student);
            assertEquals("S", result.getUsername());
            assertEquals("s@x.com", result.getEmail());
        }
    }


    /** ❌ login(): should return null if wrong password */
    @Test
    void login_WrongPassword() throws Exception {
        UserDao dao = spy(DaoFactory.getUserDao());
        doReturn(true).when(dao).existsByEmail(anyString());

        String hash = BCrypt.hashpw("rightpw", BCrypt.gensalt());
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getString("password")).thenReturn(hash);

        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
            mocked.when(DBconnection::getConnection).thenReturn(conn);
            User result = dao.login("user@x.com", "wrongpw");
            assertNull(result);
        }
    }
}

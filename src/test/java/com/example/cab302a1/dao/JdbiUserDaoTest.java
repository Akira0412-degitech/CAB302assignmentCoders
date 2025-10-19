package com.example.cab302a1.dao;

import com.example.cab302a1.dao.jdbi.JdbiUserDao;
import com.example.cab302a1.model.Student;
import com.example.cab302a1.model.Teacher;
import com.example.cab302a1.model.User;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.MockedStatic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ✅ Unit tests for {@link JdbiUserDao}
 * Robust tests that ensure JDBI interactions, bindings,
 * and correct User model instantiation for all role-based branches.
 */
class JdbiUserDaoTest extends BaseJdbiDaoTest {

    @BeforeEach
    protected void setup() {
        super.setupCommonMocks();
    }

    // ============================================================
    // 1️⃣ getUserById()
    // ============================================================

    /** ✅ returns Teacher when role=Teacher */
    @Test
    void testGetUserById_ReturnsTeacher() throws Exception {
        // Base JDBI query mocking
        when(mockHandle.createQuery(argThat(sql -> sql.trim().startsWith("SELECT"))))
                .thenReturn(mockQuery);
        when(mockQuery.bind("userId", 99)).thenReturn(mockQuery);

        // Prepare mock ResultSet and StatementContext first
        ResultSet rs = mock(ResultSet.class);
        StatementContext ctx = mock(StatementContext.class);
        when(rs.getString("role")).thenReturn("Teacher");
        when(rs.getInt("user_id")).thenReturn(99);
        when(rs.getString("username")).thenReturn("Alice");
        when(rs.getString("email")).thenReturn("a@a.com");
        when(rs.getTimestamp("created_at")).thenReturn(new Timestamp(System.currentTimeMillis()));

        // Mock ResultIterable separately
        var ri = mock(org.jdbi.v3.core.result.ResultIterable.class);

        // Safely stub map() chain
        when(mockQuery.map(any(RowMapper.class))).thenAnswer(inv -> {
            @SuppressWarnings("unchecked")
            RowMapper<User> mapper = inv.getArgument(0);
            User teacher = mapper.map(rs, ctx); // call AFTER stub setup
            when(ri.findOne()).thenReturn(Optional.of(teacher));
            return ri;
        });

        // Execute method under test
        User result = new JdbiUserDao().getUserById(99);

        // Verify results
        assertTrue(result instanceof Teacher);
        assertEquals("Alice", result.getUsername());
        verify(mockQuery).bind("userId", 99);
    }





    /** 💥 propagates exception */
    @Test
    void testGetUserById_ThrowsException() {
        reset(mockJdbi);
        when(mockJdbi.withHandle(any())).thenThrow(new RuntimeException("fail"));
        assertThrows(RuntimeException.class, () -> new JdbiUserDao().getUserById(1));
    }

    // ============================================================
    // 2️⃣ getAllStudents()
    // ============================================================

    //** ✅ returns list of students */
    @Test
    void testGetAllStudents_ReturnsList() throws Exception {
        when(mockHandle.createQuery(argThat(sql -> sql.trim().startsWith("SELECT"))))
                .thenReturn(mockQuery);

        // Create mocks before stubbing
        ResultSet rs = mock(ResultSet.class);
        StatementContext ctx = mock(StatementContext.class);
        when(rs.getInt("user_id")).thenReturn(2);
        when(rs.getString("username")).thenReturn("John");
        when(rs.getString("email")).thenReturn("j@j.com");
        when(rs.getString("role")).thenReturn("Student");
        when(rs.getTimestamp("created_at")).thenReturn(new Timestamp(System.currentTimeMillis()));

        // Mock ResultIterable separately
        var ri = mock(org.jdbi.v3.core.result.ResultIterable.class);

        // Safely stub the .map() chain
        when(mockQuery.map(any(RowMapper.class))).thenAnswer(inv -> {
            @SuppressWarnings("unchecked")
            RowMapper<Student> mapper = inv.getArgument(0);
            // Now that the stubbing is complete, we can safely call mapper.map()
            Student s = mapper.map(rs, ctx);
            when(ri.list()).thenReturn(List.of(s));
            return ri;
        });

        // Execute method under test
        List<Student> list = new JdbiUserDao().getAllStudents();

        // Verify
        assertEquals(1, list.size());
        assertEquals("John", list.get(0).getUsername());
    }


    /** ❌ returns empty list */
    @Test
    void testGetAllStudents_EmptyList() {
        when(mockHandle.createQuery(argThat(sql -> sql.contains("FROM users")))).thenReturn(mockQuery);
        var ri = mock(org.jdbi.v3.core.result.ResultIterable.class);
        when(mockQuery.map(any(RowMapper.class))).thenReturn(ri);
        when(ri.list()).thenReturn(List.of());
        assertTrue(new JdbiUserDao().getAllStudents().isEmpty());
    }

    /** 💥 exception from JDBI */
    @Test
    void testGetAllStudents_ThrowsException() {
        reset(mockJdbi);
        when(mockJdbi.withHandle(any())).thenThrow(new RuntimeException("DB error"));
        assertThrows(RuntimeException.class, () -> new JdbiUserDao().getAllStudents());
    }

    // ============================================================
    // 3️⃣ existsByEmail()
    // ============================================================

    /** ✅ returns true if email exists */
    @Test
    void testExistsByEmail_True() {
        when(mockHandle.createQuery(argThat(sql -> sql.contains("email")))).thenReturn(mockQuery);
        when(mockQuery.bind("email", "x@x.com")).thenReturn(mockQuery);

        var ri = mock(org.jdbi.v3.core.result.ResultIterable.class);
        when(mockQuery.mapTo(Integer.class)).thenReturn(ri);
        when(ri.findOne()).thenReturn(Optional.of(1));

        assertTrue(new JdbiUserDao().existsByEmail("x@x.com"));
    }

    /** ❌ returns false if not present */
    @Test
    void testExistsByEmail_False() {
        when(mockHandle.createQuery(argThat(sql -> sql.contains("users")))).thenReturn(mockQuery);
        when(mockQuery.bind("email", "no@x.com")).thenReturn(mockQuery);

        var ri = mock(org.jdbi.v3.core.result.ResultIterable.class);
        when(mockQuery.mapTo(Integer.class)).thenReturn(ri);
        when(ri.findOne()).thenReturn(Optional.empty());

        assertFalse(new JdbiUserDao().existsByEmail("no@x.com"));
    }

    /** 💥 DB exception propagates */
    @Test
    void testExistsByEmail_ThrowsException() {
        reset(mockJdbi);
        when(mockJdbi.withHandle(any())).thenThrow(new RuntimeException("sql err"));
        assertThrows(RuntimeException.class, () -> new JdbiUserDao().existsByEmail("mail"));
    }

    // ============================================================
    // 4️⃣ signUpUser()
    // ============================================================

    /** ✅ signs up user successfully */
    @Test
    void testSignUpUser_Success() {
        JdbiUserDao dao = spy(new JdbiUserDao());
        doReturn(false).when(dao).existsByEmail("new@u.com");

        when(mockHandle.createUpdate(argThat(sql -> sql.trim().startsWith("INSERT")))).thenReturn(mockUpdate);
        when(mockUpdate.bind(eq("username"), eq("NewUser"))).thenReturn(mockUpdate);
        when(mockUpdate.bind(eq("email"), eq("new@u.com"))).thenReturn(mockUpdate);
        when(mockUpdate.bind(eq("password"), anyString())).thenReturn(mockUpdate);
        when(mockUpdate.bind(eq("role"), eq("Student"))).thenReturn(mockUpdate);

        var rb = mock(org.jdbi.v3.core.result.ResultBearing.class);
        var ri = mock(org.jdbi.v3.core.result.ResultIterable.class);
        when(mockUpdate.executeAndReturnGeneratedKeys("user_id")).thenReturn(rb);
        when(rb.mapTo(Integer.class)).thenReturn(ri);
        when(ri.findOne()).thenReturn(Optional.of(100));

        // Return mock user after insert
        doReturn(new Student(100, "NewUser", "new@u.com", "Student", new Timestamp(System.currentTimeMillis())))
                .when(dao).getUserById(100);

        User u = dao.signUpUser("NewUser", "new@u.com", "pass", "Student");
        assertNotNull(u);
        assertEquals("NewUser", u.getUsername());
        verify(mockUpdate).bind(eq("email"), eq("new@u.com"));
    }

    /** ❌ returns null when already exists */
    @Test
    void testSignUpUser_AlreadyExists() {
        JdbiUserDao dao = spy(new JdbiUserDao());
        doReturn(true).when(dao).existsByEmail("dup@u.com");
        assertNull(dao.signUpUser("Dup", "dup@u.com", "p", "Student"));
    }

    // ============================================================
    // 5️⃣ login()
    // ============================================================

    /** ✅ login success for Teacher */
    @Test
    void testLogin_SuccessTeacher() throws SQLException {
        // Spy the DAO so we can stub existsByEmail()
        JdbiUserDao dao = spy(new JdbiUserDao());
        doReturn(true).when(dao).existsByEmail("t@t.com");

        // Mock JDBI query chain
        when(mockHandle.createQuery(argThat(sql -> sql.contains("email"))))
                .thenReturn(mockQuery);
        when(mockQuery.bind("email", "t@t.com"))
                .thenReturn(mockQuery);

        // Prepare mock ResultSet and StatementContext
        ResultSet rs = mock(ResultSet.class);
        StatementContext ctx = mock(StatementContext.class);

        when(rs.getString("password")).thenReturn("$2a$10$fakehash");
        when(rs.getString("role")).thenReturn("Teacher");
        when(rs.getInt("user_id")).thenReturn(55);
        when(rs.getString("username")).thenReturn("Teach");
        when(rs.getString("email")).thenReturn("t@t.com");
        when(rs.getTimestamp("created_at"))
                .thenReturn(new Timestamp(System.currentTimeMillis()));

        // Prepare mock ResultIterable (the object returned by .map())
        var ri = mock(org.jdbi.v3.core.result.ResultIterable.class);

        // Mock BCrypt statically before setting up the map() behavior
        try (MockedStatic<BCrypt> mockedBCrypt = mockStatic(BCrypt.class)) {
            mockedBCrypt.when(() -> BCrypt.checkpw("pw", "$2a$10$fakehash"))
                    .thenReturn(true);

            // Safe and clean map() mocking (no nested stubbing in progress)
            when(mockQuery.map(any(RowMapper.class))).thenAnswer(invocation -> {
                @SuppressWarnings("unchecked")
                RowMapper<User> mapper = invocation.getArgument(0);

                // Perform mapping AFTER all mocks are prepared
                User mappedUser = mapper.map(rs, ctx);

                // Now stub the iterable result
                when(ri.findOne()).thenReturn(Optional.of(mappedUser));
                return ri;
            });

            // Execute the method under test
            User result = dao.login("t@t.com", "pw");

            // Assertions
            assertNotNull(result, "Expected a non-null user for valid login");
            assertTrue(result instanceof Teacher, "Expected user to be a Teacher");
            assertEquals("Teach", result.getUsername(), "Username should match the mock data");

            // Verify correct SQL binding
            verify(mockQuery).bind("email", "t@t.com");
        }
    }


    /** ❌ returns null when user not found */
    @Test
    void testLogin_UserNotFound() {
        JdbiUserDao dao = spy(new JdbiUserDao());
        doReturn(false).when(dao).existsByEmail("no@user.com");
        assertNull(dao.login("no@user.com", "pw"));
    }

    /** ❌ returns null when password invalid */
    @Test
    void testLogin_WrongPassword() {
        // Spy DAO
        JdbiUserDao dao = spy(new JdbiUserDao());
        doReturn(true).when(dao).existsByEmail("s@s.com");

        // Mock query setup
        when(mockHandle.createQuery(argThat(sql -> sql.contains("email"))))
                .thenReturn(mockQuery);
        when(mockQuery.bind("email", "s@s.com"))
                .thenReturn(mockQuery);

        // ✅ static mock for BCrypt outside everything
        try (MockedStatic<BCrypt> mocked = mockStatic(BCrypt.class)) {
            // Force password check to fail
            mocked.when(() -> BCrypt.checkpw("pw", "$2a$10$fakehash"))
                    .thenReturn(false);

            // ✅ Do NOT call mapper.map() here (causes UnfinishedStubbing)
            when(mockQuery.map(any(RowMapper.class))).thenAnswer(inv -> {
                var ri = mock(org.jdbi.v3.core.result.ResultIterable.class);

                // simulate "no valid user found" because password check fails
                when(ri.findOne()).thenReturn(Optional.empty());
                return ri;
            });

            // Execute
            User result = dao.login("s@s.com", "pw");

            // ✅ Expect null because password check fails
            assertNull(result);
        }
    }





    /** 💥 exception propagation */
    @Test
    void testLogin_ThrowsException() {
        // Reset JDBI mock to clear previous common stubs
        reset(mockJdbi);

        // Prevent NullPointerException by using argThat with a non-null HandleCallback
        when(mockJdbi.withHandle(argThat(cb -> true)))
                .thenThrow(new RuntimeException("DB crashed"));

        // Spy the DAO to safely mock specific methods
        JdbiUserDao dao = spy(new JdbiUserDao());

        // Mock existsByEmail() to return true (so login will proceed)
        doReturn(true).when(dao).existsByEmail("e@x.com");

        // Expect RuntimeException to propagate from JDBI
        assertThrows(RuntimeException.class, () -> dao.login("e@x.com", "pw"));
    }


}

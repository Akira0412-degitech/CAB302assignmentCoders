package com.example.cab302a1.dao;

import com.example.cab302a1.dao.jdbi.JdbiQuizDao;
import com.example.cab302a1.model.Quiz;
import org.jdbi.v3.core.result.ResultBearing;
import org.jdbi.v3.core.result.ResultIterable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * ✅ Unit tests for {@link JdbiQuizDao}.
 * Each test verifies correct JDBI interaction, parameter binding,
 * and exception propagation behavior — no real DB required.
 */
class JdbiQuizDaoTest extends BaseJdbiDaoTest {

    private ResultBearing mockResultBearing;
    private ResultIterable<Integer> mockIntIterable;

    @BeforeEach
    protected void setupMocks() {
        super.setupCommonMocks();
        mockResultBearing = mock(ResultBearing.class);
        mockIntIterable = mock(ResultIterable.class);
    }

    // ============================================================
    // 1️⃣ getAllQuizzes()
    // ============================================================

    /** ✅ getAllQuizzes(): returns quiz list successfully */
    @Test
    void testGetAllQuizzesReturnsList() {
        when(mockHandle.createQuery(startsWith("SELECT"))).thenReturn(mockQuery);

        @SuppressWarnings("unchecked")
        ResultIterable<Quiz> mockListIterable = mock(ResultIterable.class);
        when(mockQuery.map(any(org.jdbi.v3.core.mapper.RowMapper.class))).thenReturn(mockListIterable);
        when(mockListIterable.list()).thenReturn(List.of(new Quiz()));

        List<Quiz> result = new JdbiQuizDao().getAllQuizzes();

        assertEquals(1, result.size());
        verify(mockQuery).map(any(org.jdbi.v3.core.mapper.RowMapper.class));
    }

    /** ❌ getAllQuizzes(): returns empty list when no data */
    @Test
    void testGetAllQuizzesEmptyList() {
        when(mockHandle.createQuery(startsWith("SELECT"))).thenReturn(mockQuery);

        @SuppressWarnings("unchecked")
        ResultIterable<Quiz> mockListIterable = mock(ResultIterable.class);
        when(mockQuery.map(any(org.jdbi.v3.core.mapper.RowMapper.class))).thenReturn(mockListIterable);
        when(mockListIterable.list()).thenReturn(List.of());

        List<Quiz> result = new JdbiQuizDao().getAllQuizzes();
        assertTrue(result.isEmpty());
    }

    /** 💥 getAllQuizzes(): propagates runtime exception */
    @Test
    void testGetAllQuizzesThrowsException() {
        reset(mockJdbi);
        when(mockJdbi.withHandle(any())).thenThrow(new RuntimeException("Query failed"));
        assertThrows(RuntimeException.class, () -> new JdbiQuizDao().getAllQuizzes());
    }

    // ============================================================
    // 2️⃣ insertQuiz()
    // ============================================================

    /** ✅ insertQuiz(): returns generated key successfully */
    @Test
    void testInsertQuizSuccess() {
        Quiz quiz = new Quiz(0, "Quiz Title", "Description", 5, false, "Teacher");

        when(mockHandle.createUpdate(startsWith("INSERT"))).thenReturn(mockUpdate);
        when(mockUpdate.bind("title", "Quiz Title")).thenReturn(mockUpdate);
        when(mockUpdate.bind("description", "Description")).thenReturn(mockUpdate);
        when(mockUpdate.bind("createdBy", 5)).thenReturn(mockUpdate);

        when(mockUpdate.executeAndReturnGeneratedKeys(eq("quiz_id"))).thenReturn(mockResultBearing);
        when(mockResultBearing.mapTo(Integer.class)).thenReturn(mockIntIterable);
        when(mockIntIterable.findOne()).thenReturn(Optional.of(101));

        int result = new JdbiQuizDao().insertQuiz(quiz);

        assertEquals(101, result);
        verify(mockUpdate).bind("title", "Quiz Title");
        verify(mockUpdate).bind("description", "Description");
        verify(mockUpdate).bind("createdBy", 5);
    }

    /** ❌ insertQuiz(): returns -1 when no key generated */
    @Test
    void testInsertQuizNoGeneratedKey() {
        Quiz quiz = new Quiz(0, "No Key", "Desc", 2, false, "User");

        when(mockHandle.createUpdate(startsWith("INSERT"))).thenReturn(mockUpdate);
        when(mockUpdate.bind("title", "No Key")).thenReturn(mockUpdate);
        when(mockUpdate.bind("description", "Desc")).thenReturn(mockUpdate);
        when(mockUpdate.bind("createdBy", 2)).thenReturn(mockUpdate);

        when(mockUpdate.executeAndReturnGeneratedKeys(eq("quiz_id"))).thenReturn(mockResultBearing);
        when(mockResultBearing.mapTo(Integer.class)).thenReturn(mockIntIterable);
        when(mockIntIterable.findOne()).thenReturn(Optional.empty());

        int result = new JdbiQuizDao().insertQuiz(quiz);
        assertEquals(-1, result);
    }

    /** 💥 insertQuiz(): propagates runtime exception */
    @Test
    void testInsertQuizThrowsException() {
        Quiz quiz = new Quiz(0, "Crash", "Fail", 1, false, "U");
        reset(mockJdbi);
        when(mockJdbi.withHandle(any())).thenThrow(new RuntimeException("Insert failed"));
        assertThrows(RuntimeException.class, () -> new JdbiQuizDao().insertQuiz(quiz));
    }

    // ============================================================
    // 3️⃣ updateQuiz()
    // ============================================================

    /** ✅ updateQuiz(): updates quiz fields correctly */
    @Test
    void testUpdateQuizSuccess() {
        Quiz quiz = new Quiz(7, "Updated", "New Desc", 2, false, "User");

        when(mockHandle.createUpdate(startsWith("UPDATE"))).thenReturn(mockUpdate);
        when(mockUpdate.bind("title", "Updated")).thenReturn(mockUpdate);
        when(mockUpdate.bind("description", "New Desc")).thenReturn(mockUpdate);
        when(mockUpdate.bind("quizId", 7)).thenReturn(mockUpdate);
        when(mockUpdate.execute()).thenReturn(1);

        new JdbiQuizDao().updateQuiz(quiz);

        verify(mockUpdate).bind("quizId", 7);
        verify(mockUpdate).execute();
    }

    /** 💥 updateQuiz(): propagates runtime exception */
    @Test
    void testUpdateQuizThrowsException() {
        Quiz quiz = new Quiz(5, "Err", "Exp", 1, false, "U");
        doThrow(new RuntimeException("Update failed")).when(mockJdbi).useHandle(any());
        assertThrows(RuntimeException.class, () -> new JdbiQuizDao().updateQuiz(quiz));
    }

    // ============================================================
    // 4️⃣ getQuizById()
    // ============================================================

    /** ✅ getQuizById(): returns quiz when found */
    @Test
    void testGetQuizByIdReturnsQuiz() {
        when(mockHandle.createQuery(startsWith("SELECT"))).thenReturn(mockQuery);
        when(mockQuery.bind("quizId", 3)).thenReturn(mockQuery);

        @SuppressWarnings("unchecked")
        ResultIterable<Quiz> mockListIterable = mock(ResultIterable.class);
        when(mockQuery.map(any(org.jdbi.v3.core.mapper.RowMapper.class))).thenReturn(mockListIterable);
        when(mockListIterable.findOne()).thenReturn(Optional.of(new Quiz()));

        Quiz result = new JdbiQuizDao().getQuizById(3);
        assertNotNull(result);
        verify(mockQuery).bind("quizId", 3);
    }

    /** ❌ getQuizById(): returns null when no quiz found */
    @Test
    void testGetQuizByIdNotFound() {
        when(mockHandle.createQuery(startsWith("SELECT"))).thenReturn(mockQuery);
        when(mockQuery.bind("quizId", 99)).thenReturn(mockQuery);

        @SuppressWarnings("unchecked")
        ResultIterable<Quiz> mockListIterable = mock(ResultIterable.class);
        when(mockQuery.map(any(org.jdbi.v3.core.mapper.RowMapper.class))).thenReturn(mockListIterable);
        when(mockListIterable.findOne()).thenReturn(Optional.empty());

        Quiz result = new JdbiQuizDao().getQuizById(99);
        assertNull(result);
    }

    /** 💥 getQuizById(): propagates runtime exception */
    @Test
    void testGetQuizByIdThrowsException() {
        reset(mockJdbi);
        when(mockJdbi.withHandle(any())).thenThrow(new RuntimeException("Crash"));
        assertThrows(RuntimeException.class, () -> new JdbiQuizDao().getQuizById(1));
    }

    // ============================================================
    // 5️⃣ updateQuizStatus()
    // ============================================================

    /** ✅ updateQuizStatus(): executes with correct bindings */
    @Test
    void testUpdateQuizStatusSuccess() {
        when(mockHandle.createUpdate(startsWith("UPDATE"))).thenReturn(mockUpdate);
        when(mockUpdate.bind("isHidden", true)).thenReturn(mockUpdate);
        when(mockUpdate.bind("quizId", 10)).thenReturn(mockUpdate);
        when(mockUpdate.execute()).thenReturn(1);

        new JdbiQuizDao().updateQuizStatus(10, true);

        verify(mockUpdate).bind("isHidden", true);
        verify(mockUpdate).bind("quizId", 10);
        verify(mockUpdate).execute();
    }

    /** 💥 updateQuizStatus(): propagates runtime exception */
    @Test
    void testUpdateQuizStatusThrowsException() {
        doThrow(new RuntimeException("Fail")).when(mockJdbi).useHandle(any());
        assertThrows(RuntimeException.class, () -> new JdbiQuizDao().updateQuizStatus(1, false));
    }

    // ============================================================
    // 6️⃣ getQuizByTeacherId()
    // ============================================================

    /** ✅ getQuizByTeacherId(): returns list successfully */
    @Test
    void testGetQuizByTeacherIdReturnsList() {
        when(mockHandle.createQuery(startsWith("SELECT"))).thenReturn(mockQuery);
        when(mockQuery.bind("teacherId", 20)).thenReturn(mockQuery);

        @SuppressWarnings("unchecked")
        ResultIterable<Quiz> mockListIterable = mock(ResultIterable.class);
        when(mockQuery.map(any(org.jdbi.v3.core.mapper.RowMapper.class))).thenReturn(mockListIterable);
        when(mockListIterable.list()).thenReturn(List.of(new Quiz()));

        List<Quiz> result = new JdbiQuizDao().getQuizByTeacherId(20);
        assertEquals(1, result.size());
        verify(mockQuery).bind("teacherId", 20);
    }

    /** ❌ getQuizByTeacherId(): returns empty list */
    @Test
    void testGetQuizByTeacherIdEmptyList() {
        when(mockHandle.createQuery(startsWith("SELECT"))).thenReturn(mockQuery);
        when(mockQuery.bind("teacherId", 33)).thenReturn(mockQuery);

        @SuppressWarnings("unchecked")
        ResultIterable<Quiz> mockListIterable = mock(ResultIterable.class);
        when(mockQuery.map(any(org.jdbi.v3.core.mapper.RowMapper.class))).thenReturn(mockListIterable);
        when(mockListIterable.list()).thenReturn(List.of());

        List<Quiz> result = new JdbiQuizDao().getQuizByTeacherId(33);
        assertTrue(result.isEmpty());
    }

    /** 💥 getQuizByTeacherId(): propagates runtime exception */
    @Test
    void testGetQuizByTeacherIdThrowsException() {
        reset(mockJdbi);
        when(mockJdbi.withHandle(any())).thenThrow(new RuntimeException("DB failure"));
        assertThrows(RuntimeException.class, () -> new JdbiQuizDao().getQuizByTeacherId(1));
    }
}

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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * ✅ Unit tests for {@link JdbiQuizDao}.
 * These tests verify that JDBI interactions are correctly chained, bound, and
 * that expected logic flow (including null and exception cases) behaves correctly.
 */
class JdbiQuizDaoTest extends BaseJdbiDaoTest {

    private ResultBearing mockResultBearing;
    private ResultIterable<Quiz> mockQuizIterable;
    private ResultIterable<Integer> mockIntIterable;

    @BeforeEach
    protected void setupMocks() {
        super.setupCommonMocks();
        mockResultBearing = mock(ResultBearing.class);
        mockQuizIterable = mock(ResultIterable.class);
        mockIntIterable = mock(ResultIterable.class);
    }

    // ============================================================
    // ✅ insertQuiz()
    // ============================================================

    /** ✅ insertQuiz(): returns generated key successfully */
    @Test
    void testInsertQuizSuccess() {
        Quiz quiz = new Quiz(0, "Quiz Title", "Description", 7, false, "teacher");

        when(mockHandle.createUpdate(eq("""
            INSERT INTO quizzes (title, description, created_by, is_Hidden)
            VALUES (:title, :description, :createdBy, false)
        """))).thenReturn(mockUpdate);
        when(mockUpdate.bind("title", "Quiz Title")).thenReturn(mockUpdate);
        when(mockUpdate.bind("description", "Description")).thenReturn(mockUpdate);
        when(mockUpdate.bind("createdBy", 7)).thenReturn(mockUpdate);

        when(mockUpdate.executeAndReturnGeneratedKeys(eq("quiz_id"))).thenReturn(mockResultBearing);
        when(mockResultBearing.mapTo(Integer.class)).thenReturn(mockIntIterable);
        when(mockIntIterable.findOne()).thenReturn(Optional.of(101));

        int result = new JdbiQuizDao().insertQuiz(quiz);

        assertEquals(101, result);
        verify(mockUpdate).bind("title", "Quiz Title");
        verify(mockUpdate).bind("description", "Description");
        verify(mockUpdate).bind("createdBy", 7);
    }

    /** ❌ insertQuiz(): returns -1 when no key generated */
    @Test
    void testInsertQuizNoGeneratedKey() {
        Quiz quiz = new Quiz(0, "Untitled", "Empty", 8, false, "user");

        when(mockHandle.createUpdate(any())).thenReturn(mockUpdate);
        when(mockUpdate.bind(eq("title"), eq("Untitled"))).thenReturn(mockUpdate);
        when(mockUpdate.bind(eq("description"), eq("Empty"))).thenReturn(mockUpdate);
        when(mockUpdate.bind(eq("createdBy"), eq(8))).thenReturn(mockUpdate);

        when(mockUpdate.executeAndReturnGeneratedKeys(eq("quiz_id"))).thenReturn(mockResultBearing);
        when(mockResultBearing.mapTo(Integer.class)).thenReturn(mockIntIterable);
        when(mockIntIterable.findOne()).thenReturn(Optional.empty());

        int result = new JdbiQuizDao().insertQuiz(quiz);
        assertEquals(-1, result);
    }

    /** 💥 insertQuiz(): propagates runtime exception */
    @Test
    void testInsertQuizThrowsException() {
        Quiz quiz = new Quiz(0, "Crash", "Fail", 1, false, "user");
        reset(mockJdbi);
        when(mockJdbi.withHandle(any())).thenThrow(new RuntimeException("Insert failed"));
        assertThrows(RuntimeException.class, () -> new JdbiQuizDao().insertQuiz(quiz));
    }

    // ============================================================
    // ✅ getAllQuizzes()
    // ============================================================

    /** ✅ getAllQuizzes(): returns list successfully */
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

    /** ❌ getAllQuizzes(): returns empty list */
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
    // ✅ getQuizById()
    // ============================================================

    /** ✅ getQuizById(): returns quiz when found */
    @Test
    void testGetQuizByIdReturnsQuiz() {
        when(mockHandle.createQuery(startsWith("SELECT"))).thenReturn(mockQuery);
        @SuppressWarnings("unchecked")
        ResultIterable<Quiz> mockIterable = mock(ResultIterable.class);
        when(mockQuery.bind("quizId", 3)).thenReturn(mockQuery);
        when(mockQuery.map(any(org.jdbi.v3.core.mapper.RowMapper.class))).thenReturn(mockIterable);
        when(mockIterable.findOne()).thenReturn(Optional.of(new Quiz()));

        Quiz result = new JdbiQuizDao().getQuizById(3);
        assertNotNull(result);
        verify(mockQuery).bind("quizId", 3);
    }

    /** ❌ getQuizById(): returns null when not found */
    @Test
    void testGetQuizByIdNotFound() {
        when(mockHandle.createQuery(startsWith("SELECT"))).thenReturn(mockQuery);
        @SuppressWarnings("unchecked")
        ResultIterable<Quiz> mockIterable = mock(ResultIterable.class);
        when(mockQuery.bind("quizId", 9)).thenReturn(mockQuery);
        when(mockQuery.map(any(org.jdbi.v3.core.mapper.RowMapper.class))).thenReturn(mockIterable);
        when(mockIterable.findOne()).thenReturn(Optional.empty());

        Quiz result = new JdbiQuizDao().getQuizById(9);
        assertNull(result);
    }

    /** 💥 getQuizById(): propagates exception */
    @Test
    void testGetQuizByIdThrowsException() {
        reset(mockJdbi);
        when(mockJdbi.withHandle(any())).thenThrow(new RuntimeException("DB crash"));
        assertThrows(RuntimeException.class, () -> new JdbiQuizDao().getQuizById(1));
    }

    // ============================================================
    // ✅ updateQuiz()
    // ============================================================

    /** ✅ updateQuiz(): binds and executes correctly */
    @Test
    void testUpdateQuizSuccess() {
        Quiz quiz = new Quiz(5, "Updated", "New Desc", 2, false, "user");
        when(mockHandle.createUpdate(startsWith("UPDATE"))).thenReturn(mockUpdate);
        when(mockUpdate.bind("title", "Updated")).thenReturn(mockUpdate);
        when(mockUpdate.bind("description", "New Desc")).thenReturn(mockUpdate);
        when(mockUpdate.bind("quizId", 5)).thenReturn(mockUpdate);
        when(mockUpdate.execute()).thenReturn(1);

        new JdbiQuizDao().updateQuiz(quiz);
        verify(mockUpdate).bind("quizId", 5);
        verify(mockUpdate).execute();
    }

    /** 💥 updateQuiz(): propagates exception */
    @Test
    void testUpdateQuizThrowsException() {
        Quiz quiz = new Quiz(1, "Crash", "Desc", 2, false, "u");
        doThrow(new RuntimeException("update failed")).when(mockJdbi).useHandle(any());
        assertThrows(RuntimeException.class, () -> new JdbiQuizDao().updateQuiz(quiz));
    }

    // ============================================================
    // ✅ updateQuizStatus()
    // ============================================================

    /** ✅ updateQuizStatus(): executes with correct bindings */
    @Test
    void testUpdateQuizStatusSuccess() {
        when(mockHandle.createUpdate(startsWith("UPDATE"))).thenReturn(mockUpdate);
        when(mockUpdate.bind("isHidden", true)).thenReturn(mockUpdate);
        when(mockUpdate.bind("quizId", 5)).thenReturn(mockUpdate);
        when(mockUpdate.execute()).thenReturn(1);

        new JdbiQuizDao().updateQuizStatus(5, true);
        verify(mockUpdate).bind("quizId", 5);
        verify(mockUpdate).bind("isHidden", true);
        verify(mockUpdate).execute();
    }

    /** 💥 updateQuizStatus(): propagates exception */
    @Test
    void testUpdateQuizStatusThrowsException() {
        doThrow(new RuntimeException("Crash")).when(mockJdbi).useHandle(any());
        assertThrows(RuntimeException.class, () -> new JdbiQuizDao().updateQuizStatus(5, false));
    }

    // ============================================================
    // ✅ getQuizByTeacherId()
    // ============================================================

    /** ✅ getQuizByTeacherId(): returns list */
    @Test
    void testGetQuizByTeacherIdReturnsList() {
        when(mockHandle.createQuery(startsWith("SELECT"))).thenReturn(mockQuery);
        when(mockQuery.bind("teacherId", 11)).thenReturn(mockQuery);
        @SuppressWarnings("unchecked")
        ResultIterable<Quiz> mockListIterable = mock(ResultIterable.class);
        when(mockQuery.map(any(org.jdbi.v3.core.mapper.RowMapper.class))).thenReturn(mockListIterable);
        when(mockListIterable.list()).thenReturn(List.of(new Quiz()));

        List<Quiz> result = new JdbiQuizDao().getQuizByTeacherId(11);
        assertEquals(1, result.size());
        verify(mockQuery).bind("teacherId", 11);
    }

    /** ❌ getQuizByTeacherId(): returns empty list */
    @Test
    void testGetQuizByTeacherIdEmptyList() {
        when(mockHandle.createQuery(startsWith("SELECT"))).thenReturn(mockQuery);
        when(mockQuery.bind("teacherId", 22)).thenReturn(mockQuery);
        @SuppressWarnings("unchecked")
        ResultIterable<Quiz> mockListIterable = mock(ResultIterable.class);
        when(mockQuery.map(any(org.jdbi.v3.core.mapper.RowMapper.class))).thenReturn(mockListIterable);
        when(mockListIterable.list()).thenReturn(List.of());

        List<Quiz> result = new JdbiQuizDao().getQuizByTeacherId(22);
        assertTrue(result.isEmpty());
    }

    /** 💥 getQuizByTeacherId(): propagates exception */
    @Test
    void testGetQuizByTeacherIdThrowsException() {
        reset(mockJdbi);
        when(mockJdbi.withHandle(any())).thenThrow(new RuntimeException("DB failure"));
        assertThrows(RuntimeException.class, () -> new JdbiQuizDao().getQuizByTeacherId(1));
    }
}

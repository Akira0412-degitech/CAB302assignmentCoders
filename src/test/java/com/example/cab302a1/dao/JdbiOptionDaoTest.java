package com.example.cab302a1.dao;

import com.example.cab302a1.dao.jdbi.JdbiOptionDao;
import com.example.cab302a1.model.QuizChoiceCreate;
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
 * ✅ Unit tests for {@link JdbiOptionDao}.
 * These tests mock JDBI and verify logical behavior (binding, branching, exception handling)
 * without requiring a real database.
 */
class JdbiOptionDaoTest extends BaseJdbiDaoTest {

    private ResultBearing mockResultBearing;
    private ResultIterable<Integer> mockResultIterable;

    @BeforeEach
    protected void setupMocks() {
        super.setupCommonMocks();
        mockResultBearing = mock(ResultBearing.class);
        mockResultIterable = mock(ResultIterable.class);
    }

    /** ✅ insertOption(): returns generated key successfully */
    @Test
    void testInsertOptionSuccess() {
        // Arrange
        QuizChoiceCreate choice = new QuizChoiceCreate();
        choice.setQuestion_id(1);
        choice.setText("Option A");
        choice.setIs_correct(true);


        when(mockHandle.createUpdate(any())).thenReturn(mockUpdate);


        when(mockUpdate.bind(eq("questionId"), eq(1))).thenReturn(mockUpdate);
        when(mockUpdate.bind(eq("optionText"), eq("Option A"))).thenReturn(mockUpdate);
        when(mockUpdate.bind(eq("isCorrect"), eq(true))).thenReturn(mockUpdate);

        // executeAndReturnGeneratedKeys → map → findOne の流れをモック化
        when(mockUpdate.executeAndReturnGeneratedKeys(any())).thenReturn(mockResultBearing);
        when(mockResultBearing.mapTo(Integer.class)).thenReturn(mockResultIterable);
        when(mockResultIterable.findOne()).thenReturn(Optional.of(10));

        // Act
        int result = new JdbiOptionDao().insertOption(choice);

        // Assert
        assertEquals(10, result);
        verify(mockHandle).createUpdate(any());
        verify(mockUpdate).bind("questionId", 1);
        verify(mockUpdate).bind("optionText", "Option A");
        verify(mockUpdate).bind("isCorrect", true);
        verify(mockUpdate).executeAndReturnGeneratedKeys(any());
    }



    /** ❌ insertOption(): returns -1 when no key generated */
    @Test
    void testInsertOptionNoGeneratedKey() {
        // Arrange
        QuizChoiceCreate choice = new QuizChoiceCreate();
        choice.setQuestion_id(2);
        choice.setText("Option B");
        choice.setIs_correct(false);

        // Mock the JDBI update chain
        when(mockHandle.createUpdate(any())).thenReturn(mockUpdate);

        // Explicitly mock each bind() with correct data types
        when(mockUpdate.bind(eq("questionId"), eq(2))).thenReturn(mockUpdate);
        when(mockUpdate.bind(eq("optionText"), eq("Option B"))).thenReturn(mockUpdate);
        when(mockUpdate.bind(eq("isCorrect"), eq(false))).thenReturn(mockUpdate);

        // Mock the generated key flow: execute → mapTo → findOne()
        when(mockUpdate.executeAndReturnGeneratedKeys(any())).thenReturn(mockResultBearing);
        when(mockResultBearing.mapTo(Integer.class)).thenReturn(mockResultIterable);
        when(mockResultIterable.findOne()).thenReturn(Optional.empty()); // no key generated

        // Act
        int result = new JdbiOptionDao().insertOption(choice);

        // Assert
        assertEquals(-1, result); // Expect -1 when no generated key is found
        verify(mockUpdate).bind("questionId", 2);
        verify(mockUpdate).bind("optionText", "Option B");
        verify(mockUpdate).bind("isCorrect", false);
    }



    /** 💥 insertOption(): propagates runtime exception */
    @Test
    void testInsertOptionThrowsException() {
        QuizChoiceCreate choice = new QuizChoiceCreate();
        choice.setQuestion_id(3);
        choice.setText("Option C");
        choice.setIs_correct(true);

        reset(mockJdbi);
        when(mockJdbi.withHandle(any())).thenThrow(new RuntimeException("DB failure"));

        assertThrows(RuntimeException.class, () -> new JdbiOptionDao().insertOption(choice));
    }

    /** ✅ getOptionsByQuestionId(): returns list when found */
    /** ✅ getOptionsByQuestionId(): returns list when found */
    @Test
    void testGetOptionsByQuestionIdReturnsList() {
        // Arrange
        when(mockHandle.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.bind(eq("questionId"), anyInt())).thenReturn(mockQuery);


        @SuppressWarnings("unchecked")
        ResultIterable<QuizChoiceCreate> mockListIterable = mock(ResultIterable.class);


        when(mockQuery.map(any(org.jdbi.v3.core.mapper.RowMapper.class))).thenReturn(mockListIterable);

        when(mockListIterable.list()).thenReturn(List.of(new QuizChoiceCreate()));

        // Act
        List<QuizChoiceCreate> results = new JdbiOptionDao().getOptionsByQuestionId(5);

        // Assert
        assertEquals(1, results.size());
        verify(mockQuery).bind("questionId", 5);
    }


    /** ❌ getOptionsByQuestionId(): returns empty list when no data */
    @Test
    void testGetOptionsByQuestionIdEmptyList() {
        when(mockHandle.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.bind(eq("questionId"), anyInt())).thenReturn(mockQuery);

        @SuppressWarnings("unchecked")
        ResultIterable<QuizChoiceCreate> mockListIterable = mock(ResultIterable.class);

        when(mockQuery.map(any(org.jdbi.v3.core.mapper.RowMapper.class))).thenReturn(mockListIterable);

        when(mockListIterable.list()).thenReturn(List.of());

        List<QuizChoiceCreate> results = new JdbiOptionDao().getOptionsByQuestionId(99);

        assertTrue(results.isEmpty());
        verify(mockQuery).bind("questionId", 99);
    }

    /** 💥 getOptionsByQuestionId(): propagates runtime exception */
    @Test
    void testGetOptionsByQuestionIdThrowsException() {
        reset(mockJdbi);
        when(mockJdbi.withHandle(any())).thenThrow(new RuntimeException("SQL failure"));

        assertThrows(RuntimeException.class, () -> new JdbiOptionDao().getOptionsByQuestionId(1));
    }

    /** ✅ updateOption(): correctly binds parameters and executes update */
    @Test
    void testUpdateOptionSuccess() {
        // Arrange: prepare a quiz option to update
        QuizChoiceCreate choice = new QuizChoiceCreate();
        choice.setOption_id(7);
        choice.setText("Updated option");
        choice.setIs_correct(true);

        // Mock JDBI flow without specifying actual SQL
        when(mockHandle.createUpdate(any())).thenReturn(mockUpdate);
        when(mockUpdate.bind(eq("optionText"), eq("Updated option"))).thenReturn(mockUpdate);
        when(mockUpdate.bind(eq("isCorrect"), eq(true))).thenReturn(mockUpdate);
        when(mockUpdate.bind(eq("optionId"), eq(7))).thenReturn(mockUpdate);
        when(mockUpdate.execute()).thenReturn(1);

        // Act: call DAO method
        new JdbiOptionDao().updateOption(choice);

        // Assert: verify correct bindings and execution
        verify(mockHandle).createUpdate(any());
        verify(mockUpdate).bind("optionText", "Updated option");
        verify(mockUpdate).bind("isCorrect", true);
        verify(mockUpdate).bind("optionId", 7);
        verify(mockUpdate).execute();
    }



    /** 💥 updateOption(): propagates runtime exception */
    @Test
    void testUpdateOptionThrowsException() {
        QuizChoiceCreate choice = new QuizChoiceCreate();
        choice.setOption_id(99);
        choice.setText("Crash");
        choice.setIs_correct(false);

        doThrow(new RuntimeException("DB error"))
                .when(mockJdbi)
                .useHandle(any());

        assertThrows(RuntimeException.class, () -> new JdbiOptionDao().updateOption(choice));
    }
}

package com.example.cab302a1.service;

import com.example.cab302a1.dao.OptionDao;
import com.example.cab302a1.dao.QuestionDao;
import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.model.QuizChoiceCreate;
import com.example.cab302a1.model.QuizQuestionCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for QuizService.
 * Tests the logic for loading quizzes with questions and options.
 */
class QuizServiceTest {

    private QuizService quizService;
    private QuestionDao mockQuestionDao;
    private OptionDao mockOptionDao;

    @BeforeEach
    void setUp() {
        mockQuestionDao = mock(QuestionDao.class);
        mockOptionDao = mock(OptionDao.class);
        
        // Create QuizService with mocked DAOs using reflection to inject dependencies
        quizService = new QuizService();
        try {
            var questionDaoField = QuizService.class.getDeclaredField("questionDao");
            questionDaoField.setAccessible(true);
            questionDaoField.set(quizService, mockQuestionDao);
            
            var optionDaoField = QuizService.class.getDeclaredField("optionDao");
            optionDaoField.setAccessible(true);
            optionDaoField.set(quizService, mockOptionDao);
        } catch (Exception e) {
            fail("Failed to inject mock dependencies: " + e.getMessage());
        }
    }

    @Test
    void testLoadQuizFully_WithValidQuiz_LoadsQuestionsAndOptions() {
        // Arrange
        Quiz quiz = new Quiz();
        quiz.setQuizId(1);
        quiz.setTitle("Test Quiz");

        QuizQuestionCreate question1 = new QuizQuestionCreate();
        question1.setQuestionId(101);
        question1.setQuestionText("What is 2+2?");

        QuizQuestionCreate question2 = new QuizQuestionCreate();
        question2.setQuestionId(102);
        question2.setQuestionText("What is 3+3?");

        List<QuizQuestionCreate> questions = List.of(question1, question2);

        QuizChoiceCreate option1 = new QuizChoiceCreate("4", true);
        option1.setOption_id(1);
        QuizChoiceCreate option2 = new QuizChoiceCreate("5", false);
        option2.setOption_id(2);

        QuizChoiceCreate option3 = new QuizChoiceCreate("6", true);
        option3.setOption_id(3);
        QuizChoiceCreate option4 = new QuizChoiceCreate("7", false);
        option4.setOption_id(4);

        when(mockQuestionDao.getAllQuestions(1)).thenReturn(questions);
        when(mockOptionDao.getOptionsByQuestionId(101)).thenReturn(List.of(option1, option2));
        when(mockOptionDao.getOptionsByQuestionId(102)).thenReturn(List.of(option3, option4));

        // Act
        Quiz result = quizService.loadQuizFully(quiz);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getQuestions().size());
        assertEquals("What is 2+2?", result.getQuestions().get(0).getQuestionText());
        assertEquals(2, result.getQuestions().get(0).getChoices().size());
        assertEquals("4", result.getQuestions().get(0).getChoices().get(0).getText());
        assertTrue(result.getQuestions().get(0).getChoices().get(0).isCorrect());

        assertEquals("What is 3+3?", result.getQuestions().get(1).getQuestionText());
        assertEquals(2, result.getQuestions().get(1).getChoices().size());
        assertEquals("6", result.getQuestions().get(1).getChoices().get(0).getText());

        verify(mockQuestionDao).getAllQuestions(1);
        verify(mockOptionDao).getOptionsByQuestionId(101);
        verify(mockOptionDao).getOptionsByQuestionId(102);
    }

    @Test
    void testLoadQuizFully_WithNoQuestions_ReturnsEmptyQuestionList() {
        // Arrange
        Quiz quiz = new Quiz();
        quiz.setQuizId(2);
        quiz.setTitle("Empty Quiz");

        when(mockQuestionDao.getAllQuestions(2)).thenReturn(new ArrayList<>());

        // Act
        Quiz result = quizService.loadQuizFully(quiz);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getQuestions().size());
        verify(mockQuestionDao).getAllQuestions(2);
        verifyNoInteractions(mockOptionDao); // No options should be loaded if there are no questions
    }

    @Test
    void testLoadQuizFully_WithNoOptions_LoadsQuestionsWithEmptyChoices() {
        // Arrange
        Quiz quiz = new Quiz();
        quiz.setQuizId(3);

        QuizQuestionCreate question = new QuizQuestionCreate();
        question.setQuestionId(201);
        question.setQuestionText("Question without options");

        when(mockQuestionDao.getAllQuestions(3)).thenReturn(List.of(question));
        when(mockOptionDao.getOptionsByQuestionId(201)).thenReturn(new ArrayList<>());

        // Act
        Quiz result = quizService.loadQuizFully(quiz);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getQuestions().size());
        assertEquals(0, result.getQuestions().get(0).getChoices().size());
        verify(mockQuestionDao).getAllQuestions(3);
        verify(mockOptionDao).getOptionsByQuestionId(201);
    }

    @Test
    void testLoadQuizFully_WithMultipleOptions_LoadsAllOptionsCorrectly() {
        // Arrange
        Quiz quiz = new Quiz();
        quiz.setQuizId(4);

        QuizQuestionCreate question = new QuizQuestionCreate();
        question.setQuestionId(301);

        List<QuizChoiceCreate> options = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            QuizChoiceCreate option = new QuizChoiceCreate("Option " + i, i == 2);
            option.setOption_id(i);
            options.add(option);
        }

        when(mockQuestionDao.getAllQuestions(4)).thenReturn(List.of(question));
        when(mockOptionDao.getOptionsByQuestionId(301)).thenReturn(options);

        // Act
        Quiz result = quizService.loadQuizFully(quiz);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getQuestions().size());
        assertEquals(5, result.getQuestions().get(0).getChoices().size());
        
        // Verify only the 3rd option (index 2) is correct
        for (int i = 0; i < 5; i++) {
            QuizChoiceCreate choice = result.getQuestions().get(0).getChoices().get(i);
            assertEquals("Option " + i, choice.getText());
            assertEquals(i == 2, choice.isCorrect());
        }
    }

    @Test
    void testLoadQuizFully_ClearsExistingChoices_BeforeAddingNew() {
        // Arrange
        Quiz quiz = new Quiz();
        quiz.setQuizId(5);

        QuizQuestionCreate question = new QuizQuestionCreate();
        question.setQuestionId(401);
        
        // Pre-populate with old choices
        QuizChoiceCreate oldChoice = new QuizChoiceCreate("Old Option", false);
        question.getChoices().add(oldChoice);

        QuizChoiceCreate newChoice1 = new QuizChoiceCreate("New Option 1", true);
        QuizChoiceCreate newChoice2 = new QuizChoiceCreate("New Option 2", false);

        when(mockQuestionDao.getAllQuestions(5)).thenReturn(List.of(question));
        when(mockOptionDao.getOptionsByQuestionId(401)).thenReturn(List.of(newChoice1, newChoice2));

        // Act
        Quiz result = quizService.loadQuizFully(quiz);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getQuestions().size());
        assertEquals(2, result.getQuestions().get(0).getChoices().size());
        
        // Verify old choices are cleared and only new choices remain
        assertEquals("New Option 1", result.getQuestions().get(0).getChoices().get(0).getText());
        assertEquals("New Option 2", result.getQuestions().get(0).getChoices().get(1).getText());
        
        // Old choice should not be present
        assertFalse(result.getQuestions().get(0).getChoices().stream()
                .anyMatch(c -> c.getText().equals("Old Option")));
    }

    @Test
    void testLoadQuizFully_WithDifferentQuizIds_CallsCorrectDaoMethods() {
        // Arrange
        Quiz quiz1 = new Quiz();
        quiz1.setQuizId(10);

        Quiz quiz2 = new Quiz();
        quiz2.setQuizId(20);

        when(mockQuestionDao.getAllQuestions(10)).thenReturn(new ArrayList<>());
        when(mockQuestionDao.getAllQuestions(20)).thenReturn(new ArrayList<>());

        // Act
        quizService.loadQuizFully(quiz1);
        quizService.loadQuizFully(quiz2);

        // Assert
        verify(mockQuestionDao).getAllQuestions(10);
        verify(mockQuestionDao).getAllQuestions(20);
        verify(mockQuestionDao, times(2)).getAllQuestions(anyInt());
    }

    @Test
    void testLoadQuizFully_ReturnsSameQuizInstance() {
        // Arrange
        Quiz quiz = new Quiz();
        quiz.setQuizId(6);
        quiz.setTitle("Original Quiz");

        when(mockQuestionDao.getAllQuestions(6)).thenReturn(new ArrayList<>());

        // Act
        Quiz result = quizService.loadQuizFully(quiz);

        // Assert - should return the same instance, not a new one
        assertSame(quiz, result);
        assertEquals("Original Quiz", result.getTitle());
    }
}


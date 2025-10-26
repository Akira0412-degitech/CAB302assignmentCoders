package com.example.cab302a1.result;

import com.example.cab302a1.dao.AttemptDao;
import com.example.cab302a1.dao.QuestionDao;
import com.example.cab302a1.dao.QuizDao;
import com.example.cab302a1.dao.UserDao;
import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.model.QuizQuestionCreate;
import com.example.cab302a1.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for QuizResultService.
 * Tests validation, data retrieval, and result calculation logic.
 */
class QuizResultServiceTest {

    private QuizResultService service;
    private AttemptDao mockAttemptDao;
    private QuizDao mockQuizDao;
    private UserDao mockUserDao;
    private QuestionDao mockQuestionDao;

    @BeforeEach
    void setUp() {
        mockAttemptDao = mock(AttemptDao.class);
        mockQuizDao = mock(QuizDao.class);
        mockUserDao = mock(UserDao.class);
        mockQuestionDao = mock(QuestionDao.class);

        service = new QuizResultService(mockAttemptDao, mockQuizDao, mockUserDao, mockQuestionDao);
    }

    @Test
    void testGetQuizResult_WithValidData_ReturnsQuizResultData() throws QuizResultService.QuizResultException {
        // Arrange
        int quizId = 1;
        int userId = 100;
        
        when(mockAttemptDao.getScore(quizId, userId)).thenReturn(15);
        
        Quiz quiz = new Quiz();
        quiz.setQuizId(quizId);
        quiz.setTitle("Math Quiz");
        when(mockQuizDao.getAllQuizzes()).thenReturn(List.of(quiz));
        
        User user = new User(userId, "student1", "student@test.com", "Student", new Timestamp(System.currentTimeMillis()));
        when(mockUserDao.getUserById(userId)).thenReturn(user);
        
        List<QuizQuestionCreate> questions = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            questions.add(new QuizQuestionCreate());
        }
        when(mockQuestionDao.getAllQuestions(quizId)).thenReturn(questions);

        // Act
        QuizResultData result = service.getQuizResult(quizId, userId);

        // Assert
        assertNotNull(result);
        assertEquals(15, result.getCorrectAnswers());
        assertEquals(20, result.getTotalQuestions());
        assertEquals("Math Quiz", result.getQuizTitle());
        assertEquals(quizId, result.getQuizId());
        assertEquals(userId, result.getUserId());
        
        verify(mockAttemptDao).getScore(quizId, userId);
        verify(mockQuizDao).getAllQuizzes();
        verify(mockUserDao).getUserById(userId);
        verify(mockQuestionDao).getAllQuestions(quizId);
    }

    @Test
    void testGetQuizResult_WithNoAttempt_ThrowsException() {
        // Arrange
        int quizId = 1;
        int userId = 100;
        
        when(mockAttemptDao.getScore(quizId, userId)).thenReturn(null);

        // Act & Assert
        QuizResultService.QuizResultException exception = assertThrows(
            QuizResultService.QuizResultException.class,
            () -> service.getQuizResult(quizId, userId)
        );
        
        assertTrue(exception.getMessage().contains("No quiz attempt found"));
        verify(mockAttemptDao).getScore(quizId, userId);
    }

    @Test
    void testGetQuizResult_WithNonExistentQuiz_ThrowsException() {
        // Arrange
        int quizId = 999;
        int userId = 100;
        
        when(mockAttemptDao.getScore(quizId, userId)).thenReturn(10);
        when(mockQuizDao.getAllQuizzes()).thenReturn(new ArrayList<>());

        // Act & Assert
        QuizResultService.QuizResultException exception = assertThrows(
            QuizResultService.QuizResultException.class,
            () -> service.getQuizResult(quizId, userId)
        );
        
        assertTrue(exception.getMessage().contains("Quiz not found"));
        verify(mockAttemptDao).getScore(quizId, userId);
        verify(mockQuizDao).getAllQuizzes();
    }

    @Test
    void testGetQuizResult_WithNonExistentUser_ThrowsException() {
        // Arrange
        int quizId = 1;
        int userId = 999;
        
        when(mockAttemptDao.getScore(quizId, userId)).thenReturn(10);
        
        Quiz quiz = new Quiz();
        quiz.setQuizId(quizId);
        quiz.setTitle("Test Quiz");
        when(mockQuizDao.getAllQuizzes()).thenReturn(List.of(quiz));
        
        when(mockUserDao.getUserById(userId)).thenReturn(null);

        // Act & Assert
        QuizResultService.QuizResultException exception = assertThrows(
            QuizResultService.QuizResultException.class,
            () -> service.getQuizResult(quizId, userId)
        );
        
        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    void testHasQuizResult_WithExistingResult_ReturnsTrue() {
        // Arrange
        int quizId = 1;
        int userId = 100;
        
        when(mockAttemptDao.getScore(quizId, userId)).thenReturn(15);

        // Act
        boolean result = service.hasQuizResult(quizId, userId);

        // Assert
        assertTrue(result);
        verify(mockAttemptDao).getScore(quizId, userId);
    }

    @Test
    void testHasQuizResult_WithNoResult_ReturnsFalse() {
        // Arrange
        int quizId = 1;
        int userId = 100;
        
        when(mockAttemptDao.getScore(quizId, userId)).thenReturn(null);

        // Act
        boolean result = service.hasQuizResult(quizId, userId);

        // Assert
        assertFalse(result);
        verify(mockAttemptDao).getScore(quizId, userId);
    }

    @Test
    void testHasQuizResult_WithException_ReturnsFalse() {
        // Arrange
        int quizId = 1;
        int userId = 100;
        
        when(mockAttemptDao.getScore(quizId, userId)).thenThrow(new RuntimeException("Database error"));

        // Act
        boolean result = service.hasQuizResult(quizId, userId);

        // Assert
        assertFalse(result);
        verify(mockAttemptDao).getScore(quizId, userId);
    }

    @Test
    void testGetQuizResult_WithPerfectScore_ReturnsCorrectData() throws QuizResultService.QuizResultException {
        // Arrange
        int quizId = 2;
        int userId = 200;
        
        when(mockAttemptDao.getScore(quizId, userId)).thenReturn(10);
        
        Quiz quiz = new Quiz();
        quiz.setQuizId(quizId);
        quiz.setTitle("Perfect Score Quiz");
        when(mockQuizDao.getAllQuizzes()).thenReturn(List.of(quiz));
        
        User user = new User(userId, "topstudent", "top@test.com", "Student", new Timestamp(System.currentTimeMillis()));
        when(mockUserDao.getUserById(userId)).thenReturn(user);
        
        List<QuizQuestionCreate> questions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            questions.add(new QuizQuestionCreate());
        }
        when(mockQuestionDao.getAllQuestions(quizId)).thenReturn(questions);

        // Act
        QuizResultData result = service.getQuizResult(quizId, userId);

        // Assert
        assertNotNull(result);
        assertEquals(10, result.getCorrectAnswers());
        assertEquals(10, result.getTotalQuestions());
        assertEquals(100.0, result.getScorePercentage(), 0.01);
        assertTrue(result.isPassing());
        assertEquals("A", result.getLetterGrade());
    }

    @Test
    void testGetQuizResult_WithZeroScore_ReturnsCorrectData() throws QuizResultService.QuizResultException {
        // Arrange
        int quizId = 3;
        int userId = 300;
        
        when(mockAttemptDao.getScore(quizId, userId)).thenReturn(0);
        
        Quiz quiz = new Quiz();
        quiz.setQuizId(quizId);
        quiz.setTitle("Failed Quiz");
        when(mockQuizDao.getAllQuizzes()).thenReturn(List.of(quiz));
        
        User user = new User(userId, "student2", "student2@test.com", "Student", new Timestamp(System.currentTimeMillis()));
        when(mockUserDao.getUserById(userId)).thenReturn(user);
        
        List<QuizQuestionCreate> questions = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            questions.add(new QuizQuestionCreate());
        }
        when(mockQuestionDao.getAllQuestions(quizId)).thenReturn(questions);

        // Act
        QuizResultData result = service.getQuizResult(quizId, userId);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getCorrectAnswers());
        assertEquals(15, result.getTotalQuestions());
        assertEquals(0.0, result.getScorePercentage(), 0.01);
        assertFalse(result.isPassing());
        assertEquals("F", result.getLetterGrade());
    }

    @Test
    void testGetQuizResult_WithBoundaryScore_ReturnsCorrectData() throws QuizResultService.QuizResultException {
        // Arrange
        int quizId = 4;
        int userId = 400;
        
        // 60% score (passing boundary)
        when(mockAttemptDao.getScore(quizId, userId)).thenReturn(12);
        
        Quiz quiz = new Quiz();
        quiz.setQuizId(quizId);
        quiz.setTitle("Boundary Quiz");
        when(mockQuizDao.getAllQuizzes()).thenReturn(List.of(quiz));
        
        User user = new User(userId, "student3", "student3@test.com", "Student", new Timestamp(System.currentTimeMillis()));
        when(mockUserDao.getUserById(userId)).thenReturn(user);
        
        List<QuizQuestionCreate> questions = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            questions.add(new QuizQuestionCreate());
        }
        when(mockQuestionDao.getAllQuestions(quizId)).thenReturn(questions);

        // Act
        QuizResultData result = service.getQuizResult(quizId, userId);

        // Assert
        assertNotNull(result);
        assertEquals(12, result.getCorrectAnswers());
        assertEquals(20, result.getTotalQuestions());
        assertEquals(60.0, result.getScorePercentage(), 0.01);
        assertTrue(result.isPassing());
        assertEquals("D", result.getLetterGrade());
    }

    @Test
    void testGetQuizResult_WithMultipleQuizzes_FindsCorrectQuiz() throws QuizResultService.QuizResultException {
        // Arrange
        int quizId = 5;
        int userId = 500;
        
        when(mockAttemptDao.getScore(quizId, userId)).thenReturn(8);
        
        Quiz quiz1 = new Quiz();
        quiz1.setQuizId(1);
        quiz1.setTitle("Wrong Quiz 1");
        
        Quiz quiz2 = new Quiz();
        quiz2.setQuizId(5);
        quiz2.setTitle("Correct Quiz");
        
        Quiz quiz3 = new Quiz();
        quiz3.setQuizId(10);
        quiz3.setTitle("Wrong Quiz 2");
        
        when(mockQuizDao.getAllQuizzes()).thenReturn(List.of(quiz1, quiz2, quiz3));
        
        User user = new User(userId, "student4", "student4@test.com", "Student", new Timestamp(System.currentTimeMillis()));
        when(mockUserDao.getUserById(userId)).thenReturn(user);
        
        List<QuizQuestionCreate> questions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            questions.add(new QuizQuestionCreate());
        }
        when(mockQuestionDao.getAllQuestions(quizId)).thenReturn(questions);

        // Act
        QuizResultData result = service.getQuizResult(quizId, userId);

        // Assert
        assertNotNull(result);
        assertEquals("Correct Quiz", result.getQuizTitle());
        assertEquals(5, result.getQuizId());
    }
}


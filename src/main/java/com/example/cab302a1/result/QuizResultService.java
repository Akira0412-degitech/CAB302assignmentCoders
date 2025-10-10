package com.example.cab302a1.result;

import com.example.cab302a1.dao.AttemptDao;
import com.example.cab302a1.dao.QuizDao;
import com.example.cab302a1.dao.UserDao;
import com.example.cab302a1.dao.QuestionDao;
import com.example.cab302a1.dao.jdbc.JdbcQuizDao;
import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.model.User;
import com.example.cab302a1.util.Session;

import java.sql.SQLException;
import java.util.List;

/**
 * Service class for handling quiz result operations with database integration.
 * This class provides methods to fetch quiz results from the database using
 * the AttemptDao.getScore() method and related data from other DAOs.
 * 
 * @author CAB302 Assignment Team
 * @version 1.0
 */
public class QuizResultService {
    
    private final AttemptDao attemptDao;
    private final QuizDao quizDao;
    private final UserDao userDao;
    private final QuestionDao questionDao;
    
    /**
     * Constructor with dependency injection for DAO objects.
     * 
     * @param attemptDao DAO for quiz attempt operations
     * @param quizDao DAO for quiz operations  
     * @param userDao DAO for user operations
     * @param questionDao DAO for question operations
     */
    public QuizResultService(AttemptDao attemptDao, QuizDao quizDao, UserDao userDao, QuestionDao questionDao) {
        this.attemptDao = attemptDao;
        this.quizDao = quizDao;
        this.userDao = userDao;
        this.questionDao = questionDao;
    }
    
    /**
     * Default constructor using new DAO instances.
     * Convenient for quick usage without dependency injection.
     */
    public QuizResultService() {
        this.attemptDao = new AttemptDao();
        this.quizDao = new JdbcQuizDao();
        this.userDao = new UserDao();
        this.questionDao = new QuestionDao();
    }
    
    /**
     * Creates QuizResultData using the database score for a specific quiz and user.
     * This method uses AttemptDao.getScore() to fetch the actual score from the database.
     * 
     * @param quizId The ID of the quiz
     * @param userId The ID of the user
     * @return QuizResultData object with database score, or null if not found
     * @throws QuizResultException if there's an error fetching data
     */
    public QuizResultData getQuizResult(int quizId, int userId) throws QuizResultException {
        try {
            // Get score from database using AttemptDao.getScore()
            Integer score = attemptDao.getScore(quizId, userId);
            if (score == null) {
                throw new QuizResultException("No quiz attempt found for quiz_id: " + quizId + " and user_id: " + userId);
            }
            
            // Get quiz information from available quizzes
            Quiz quiz = findQuizById(quizId);
            if (quiz == null) {
                throw new QuizResultException("Quiz not found with id: " + quizId);
            }
            
            // Get user information
            User user = userDao.getUserById(userId);
            if (user == null) {
                throw new QuizResultException("User not found with id: " + userId);
            }
            
            // Get total questions using QuestionDao
            int totalQuestions = getQuestionCountForQuiz(quizId);
            
            // Create and return QuizResultData
            return new QuizResultData(
                score,                  // correctAnswers from database
                totalQuestions,         // total questions from quiz
                quiz.getTitle(),        // quiz title
                quizId,                 // quiz ID
                userId                  // user ID
            );
            
        } catch (Exception e) {
            throw new QuizResultException("Error fetching quiz result data: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates QuizResultData for the currently logged-in user.
     * Uses Session.getCurrentUser() to get the current user ID.
     * 
     * @param quizId The ID of the quiz
     * @return QuizResultData object with database score
     * @throws QuizResultException if no user is logged in or data cannot be fetched
     */
    public QuizResultData getQuizResultForCurrentUser(int quizId) throws QuizResultException {
        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            throw new QuizResultException("No user is currently logged in");
        }
        
        return getQuizResult(quizId, currentUser.getUser_id());
    }
    
    /**
     * Checks if a quiz result exists for the given quiz and user.
     * 
     * @param quizId The ID of the quiz
     * @param userId The ID of the user
     * @return true if a result exists, false otherwise
     */
    public boolean hasQuizResult(int quizId, int userId) {
        try {
            Integer score = attemptDao.getScore(quizId, userId);
            return score != null;
        } catch (Exception e) {
            System.err.println("Error checking quiz result existence: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Checks if a quiz result exists for the currently logged-in user.
     * 
     * @param quizId The ID of the quiz
     * @return true if a result exists, false otherwise
     */
    public boolean hasQuizResultForCurrentUser(int quizId) {
        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        
        return hasQuizResult(quizId, currentUser.getUser_id());
    }
    
    /**
     * Helper method to find a quiz by ID from the available quizzes.
     * Since QuizDao doesn't have getQuizById, we get all quizzes and find the one we need.
     * 
     * @param quizId The ID of the quiz to find
     * @return Quiz object if found, null otherwise
     */
    private Quiz findQuizById(int quizId) {
        try {
            List<Quiz> allQuizzes = quizDao.getAllQuizzes();
            return allQuizzes.stream()
                    .filter(quiz -> quiz.getQuizId() == quizId)
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            System.err.println("Error finding quiz with id " + quizId + ": " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Helper method to get question count for a quiz using QuestionDao.
     * 
     * @param quizId The ID of the quiz
     * @return Number of questions in the quiz
     */
    private int getQuestionCountForQuiz(int quizId) {
        try {
//            // Create a dummy quiz object to pass to QuestionDao
//            Quiz dummyQuiz = new Quiz();
//            dummyQuiz.setQuizId(quizId);
            
            // Get all questions for this quiz
            List<?> questions = questionDao.getAllQuestions(quizId);
            return questions != null ? questions.size() : 0;
        } catch (Exception e) {
            System.err.println("Error getting question count for quiz " + quizId + ": " + e.getMessage());
            return 20; // Default fallback
        }
    }
    
    /**
     * Custom exception for quiz result service operations.
     */
    public static class QuizResultException extends Exception {
        public QuizResultException(String message) {
            super(message);
        }
        
        public QuizResultException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    /**
     * Factory method for easy integration by team members.
     * Creates a QuizResultData object from database for immediate use.
     * 
     * @param quizId The ID of the quiz
     * @param userId The ID of the user  
     * @return QuizResultData ready for display
     * @throws QuizResultException if data cannot be fetched
     */
    public static QuizResultData createFromDatabase(int quizId, int userId) throws QuizResultException {
        QuizResultService service = new QuizResultService();
        return service.getQuizResult(quizId, userId);
    }
    
    /**
     * Factory method for current user.
     * Creates a QuizResultData object from database for the currently logged-in user.
     * 
     * @param quizId The ID of the quiz
     * @return QuizResultData ready for display
     * @throws QuizResultException if data cannot be fetched
     */
    public static QuizResultData createForCurrentUser(int quizId) throws QuizResultException {
        QuizResultService service = new QuizResultService();
        return service.getQuizResultForCurrentUser(quizId);
    }
}

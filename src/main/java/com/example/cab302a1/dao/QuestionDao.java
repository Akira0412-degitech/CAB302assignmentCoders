package com.example.cab302a1.dao;

import com.example.cab302a1.model.QuizQuestionCreate;
import java.util.List;

/**
 * {@code QuestionDao} defines the contract for performing
 * data access operations on quiz question records.
 *
 * <p>This interface abstracts database interactions for
 * the {@code questions} table, providing methods for
 * retrieval, insertion, updates, and counting.</p>
 *
 * <p>It is typically implemented by {@code JdbcQuestionDao},
 * which provides the actual JDBC-based logic.</p>
 */
public interface QuestionDao {

    /**
     * Retrieves all questions belonging to a specific quiz.
     *
     * @param quizId the unique ID of the quiz
     * @return a {@link List} of {@link QuizQuestionCreate} objects representing
     *         all questions under the given quiz; an empty list if none found
     */
    List<QuizQuestionCreate> getAllQuestions(int quizId);

    /**
     * Inserts a new question record into the database and returns
     * the generated question ID.
     *
     * <p>All questions are currently assumed to be of type {@code "MCQ"}.</p>
     *
     * @param question the {@link QuizQuestionCreate} object containing quiz ID,
     *                 question text, and explanation
     * @return the generated question ID if successful, or {@code -1} if insertion fails
     */
    int insertQuestion(QuizQuestionCreate question);

    /**
     * Updates an existing question’s text and explanation.
     *
     * @param question the {@link QuizQuestionCreate} object containing updated data
     */
    void updateQuestion(QuizQuestionCreate question);

    /**
     * Returns the total number of questions in a quiz.
     *
     * @param quizId the unique ID of the quiz
     * @return the number of questions found, or {@code -1} if the query fails
     */
    int getNumQuestion(int quizId);

    /**
     * Retrieves all question records by quiz ID.
     *
     * <p>This is similar to {@link #getAllQuestions(int)}, but may be
     * used in contexts requiring different query details or performance optimizations.</p>
     *
     * @param quizId the unique ID of the quiz
     * @return a {@link List} of {@link QuizQuestionCreate} objects; an empty list if none found
     */
    List<QuizQuestionCreate> getQuestionsByQuizId(int quizId);
}

package com.example.cab302a1.dao;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.model.Quiz;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * QuizDao defines the common data access operations
 * that all Quiz DAO implementations must provide.
 *
 * It acts as a contract between the service layer and
 * the actual database implementation (e.g. JdbcQuizDao).
 */

public interface QuizDao {


    /**
     * Retrieve all quizzes from the database.
     * @return a list of all quizzes
     */

    List<Quiz> getAllQuizzes();

    /**
     * Insert a new quiz record into the database.
     * @param quiz the Quiz object to insert
     * @return the generated quiz ID, or -1 if failed
     */
     int insertQuiz(Quiz _quiz);

    /**
     * Update an existing quiz’s title and description.
     * @param quiz the Quiz object with updated data
     */
     void updateQuiz(Quiz _quiz);

    /**
     * Find a specific quiz by its ID.
     * @param quizId the unique ID of the quiz
     * @return the Quiz object, or null if not found
     */
    Quiz getQuizById(int _quiz_id);

    /**
     * Update the “is_Hidden” flag of a quiz.
     * @param quizId the ID of the quiz
     * @param isHidden true if the quiz should be hidden
     */
    public void updateQuizStatus(int _quizId, boolean _IsHidden);

}

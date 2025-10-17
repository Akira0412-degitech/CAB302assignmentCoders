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
     int insertQuiz(Quiz quiz);

    /**
     * Update an existing quiz’s title and description.
     * @param quiz the Quiz object with updated data
     */
     void updateQuiz(Quiz quiz);

    /**
     * Find a specific quiz by its ID.
     * @param quiz_id the unique ID of the quiz
     * @return the Quiz object, or null if not found
     */
    Quiz getQuizById(int quiz_id);

    /**
     * Update the “is_Hidden” flag of a quiz.
     * @param quizId the ID of the quiz
     * @param IsHidden true if the quiz should be hidden
     */
    void updateQuizStatus(int quizId, boolean IsHidden);

    /**
     * Retrieve all quizzes created by a specific teacher.
     * @param teacherId the user ID of the teacher
     * @return a list of quizzes created by that teacher
     */
    List<Quiz> getQuizByTeacherId(int teacherId);
}

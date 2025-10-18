package com.example.cab302a1.dao;

import com.example.cab302a1.model.QuizReview;
import java.util.List;

/**
 * ReviewDao defines the contract for retrieving
 * quiz attempt review information for users.
 * <p>
 * Implementations of this interface handle the
 * retrieval of quiz attempt summaries, scores,
 * feedback, and related quiz metadata.
 */
public interface ReviewDao {

    /**
     * Retrieve all quiz attempts and their associated review details
     * for a specific user.
     *
     * @param userId The unique ID of the user whose attempts are to be retrieved.
     * @return A list of {@link QuizReview} objects, each representing a quiz attempt
     *         with its score, feedback, and quiz title. Returns an empty list if none are found.
     */
    List<QuizReview> getAllAttemptsById(int userId);

    List<QuizReview> getStudentAttemptsforTeacher(int userId, int teacherId);
}

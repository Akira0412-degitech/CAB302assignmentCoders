package com.example.cab302a1.dao;

import com.example.cab302a1.model.QuestionResponse;
import java.util.List;

/**
 * ResponseDao defines the contract for managing
 * question response data within a quiz attempt.
 */
public interface ResponseDao {

    /**
     * Save a list of question responses for a specific quiz attempt.
     *
     * @param attemptId The ID of the quiz attempt.
     * @param responses The list of question responses to be saved.
     */
    void saveResponse(int attemptId, List<QuestionResponse> responses);

    /**
     * Calculate the total score based on correct responses
     * for a specific quiz attempt.
     *
     * @param attemptId The ID of the quiz attempt.
     * @return The calculated score (number of correct answers),
     *         or -1 if an error occurs.
     */
    int calculateScoreFromResponses(int attemptId);

    /**
     * Retrieve the chosen option ID for a given attempt and question.
     *
     * @param attemptId  The ID of the quiz attempt.
     * @param questionId The ID of the question.
     * @return The selected option ID, or -1 if not found.
     */
    int getChosenOptionId(int attemptId, int questionId);
}

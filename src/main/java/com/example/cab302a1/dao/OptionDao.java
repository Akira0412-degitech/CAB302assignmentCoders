package com.example.cab302a1.dao;

import com.example.cab302a1.model.QuizChoiceCreate;
import java.util.List;

/**
 * {@code OptionDao} defines the contract for managing quiz question options.
 * It provides methods for inserting, retrieving, and updating option records
 * associated with quiz questions.
 *
 * <p>This interface abstracts the persistence logic for question options,
 * allowing different database implementations (e.g., JDBC, ORM frameworks)
 * to be used interchangeably.</p>
 */
public interface OptionDao {

    /**
     * Inserts a new option record into the database and returns
     * the generated {@code option_id}.
     *
     * @param choice A {@link QuizChoiceCreate} object containing
     *               the question ID, option text, and correctness flag
     * @return The generated option ID, or {@code -1} if insertion fails
     */
    int insertOption(QuizChoiceCreate choice);

    /**
     * Retrieves all options belonging to a specific question.
     *
     * @param questionId The unique ID of the question
     * @return A {@link List} of {@link QuizChoiceCreate} objects
     *         representing all available options for that question
     */
    List<QuizChoiceCreate> getOptionsByQuestionId(int questionId);

    /**
     * Updates an existing option record in the database.
     * This method modifies the option text and correctness flag
     * based on the given {@link QuizChoiceCreate} object.
     *
     * @param choice A {@link QuizChoiceCreate} object containing
     *               the updated text, correctness flag, and option ID
     */
    void updateOption(QuizChoiceCreate choice);
}

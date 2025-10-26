package com.example.cab302a1.model;

/**
 * Represents a user's response to a specific quiz question during an attempt.
 * <p>
 * This model is used to store and transfer data related to quiz responses,
 * including which attempt the response belongs to, the associated question,
 * the selected option, and whether the answer was correct.
 * </p>
 *
 * <p><b>Responsibilities:</b></p>
 * <ul>
 *   <li>Encapsulate data for quiz question responses</li>
 *   <li>Provide constructors for creating and retrieving response records</li>
 *   <li>Offer getter and setter methods for all response attributes</li>
 * </ul>
 *
 * @author CAB302
 * @version 1.0
 */
public class QuestionResponse {

    /**
     * Default constructor.
     * <p>
     * Used for frameworks or tools that require an empty object for initialization
     * (e.g., ORM frameworks or data deserialization).
     * </p>
     */
    public QuestionResponse() {}

    /**
     * Constructor for creating a new response record before assigning a response ID.
     *
     * @param _attempt_id The ID of the attempt this response belongs to
     * @param _question_id The ID of the question answered
     * @param _option_id The ID of the selected option
     * @param _is_correct Indicates whether the chosen answer was correct
     */
    public QuestionResponse(int _attempt_id, int _question_id, int _option_id, boolean _is_correct) {
        this.attempt_id = _attempt_id;
        this.question_id = _question_id;
        this.option_id = _option_id;
        this.is_correct = _is_correct;
    }

    /**
     * Constructor for creating a response record with an existing response ID.
     * <p>
     * Typically used when retrieving response data from the database.
     * </p>
     *
     * @param _response_id The unique ID of this response record
     * @param _attempt_id The ID of the attempt this response belongs to
     * @param _question_id The ID of the question answered
     * @param _option_id The ID of the selected option
     * @param _is_correct Indicates whether the chosen answer was correct
     */
    public QuestionResponse(int _response_id, int _attempt_id, int _question_id, int _option_id, boolean _is_correct) {
        this.response_id = _response_id;
        this.attempt_id = _attempt_id;
        this.question_id = _question_id;
        this.option_id = _option_id;
        this.is_correct = _is_correct;
    }

    private int response_id;
    private int attempt_id;
    private int question_id;
    private int option_id;
    private boolean is_correct;

    /**
     * Sets the unique response ID.
     * @param _response_id The response record ID
     */
    public void setResponse_id(int _response_id) {
        this.response_id = _response_id;
    }

    /**
     * Sets the attempt ID associated with this response.
     * @param _attempt_id The attempt identifier
     */
    public void setAttempt_id(int _attempt_id){
        attempt_id = _attempt_id;
    }

    /**
     * Sets the question ID associated with this response.
     * @param _question_id The question identifier
     */
    public void setQuestion_id(int _question_id){
        question_id = _question_id;
    }

    /**
     * Sets the option ID selected by the user.
     * @param _option_id The selected option identifier
     */
    public void setOption_id(int _option_id){
        option_id = _option_id;
    }

    /**
     * Sets whether the selected option is correct.
     * @param _is_correct True if correct, false otherwise
     */
    public void setIs_correct(boolean _is_correct){
        is_correct = _is_correct;
    }

    /**
     * Gets the unique response ID.
     * @return The response record ID
     */
    public int getResponse_id(){return response_id;}

    /**
     * Gets the attempt ID.
     * @return The attempt identifier
     */
    public int getAttempt_id(){return attempt_id;}

    /**
     * Gets the question ID.
     * @return The question identifier
     */
    public int getQuestion_id(){return question_id;}

    /**
     * Gets the selected option ID.
     * @return The option identifier
     */
    public int getOption_id(){return option_id;}

    /**
     * Returns whether the chosen answer was correct.
     * @return True if the response is correct; false otherwise
     */
    public boolean getIs_Correct(){return is_correct;}
}

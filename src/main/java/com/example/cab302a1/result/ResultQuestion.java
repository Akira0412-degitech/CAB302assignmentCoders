package com.example.cab302a1.result;

import com.example.cab302a1.model.QuizQuestionCreate;

/**
 * Holds the result information for a single quiz question,
 * including the original question data and the option chosen by the user.
 * <p>
 * This is a simple data container (POJO) typically created when assembling
 * a quiz attempt's detailed result view.
 * </p>
 */
public class ResultQuestion {

    /**
     * The quiz question (prompt, options, correct answer metadata, etc.).
     */
    private QuizQuestionCreate question;

    /**
     * The identifier of the option selected by the user for this question.
     */
    private int chosenOption_id;

    /**
     * Constructs a {@code ResultQuestion} with the given question and chosen option id.
     *
     * @param _question         the {@link QuizQuestionCreate} instance for this item
     * @param _chosenOption_id  the selected option's identifier
     */
    public ResultQuestion(QuizQuestionCreate _question, int _chosenOption_id){
        this.question = _question;
        this.chosenOption_id = _chosenOption_id;
    }

    /**
     * Returns the question associated with this result item.
     *
     * @return the {@link QuizQuestionCreate} object
     */
    public QuizQuestionCreate getQuestion(){
        return question;
    }

    /**
     * Sets (replaces) the question for this result item.
     *
     * @param question the {@link QuizQuestionCreate} to set
     */
    public void setQuestion(QuizQuestionCreate question){
        this.question = question;
    }

    /**
     * Returns the identifier of the option chosen by the user.
     *
     * @return the chosen option id
     */
    public int getChosenOption_id(){
        return chosenOption_id;
    }

    /**
     * Sets (replaces) the chosen option identifier.
     *
     * @param _chosenOption_id the new chosen option id
     */
    public void setChosenOption_id(int _chosenOption_id){
        this.chosenOption_id = _chosenOption_id;
    }

}

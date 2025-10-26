package com.example.cab302a1.result;

import com.example.cab302a1.model.Quiz;

import java.util.List;

/**
 * Represents the detailed result of a single {@link Quiz}, including
 * the quiz metadata and a list of per-question result summaries.
 * <p>
 * This class is a simple data holder (POJO) that is typically created
 * by a service after evaluating a user's quiz attempt and then passed
 * to the UI layer for rendering.
 * </p>
 *
 * @since 1.0
 */
public class ResultDetail {

    /**
     * The quiz to which these results belong.
     */
    private Quiz quiz;

    /**
     * The list of per-question results (order usually matches the quiz's question order).
     */
    private List<ResultQuestion> questions;

    /**
     * Constructs a {@code ResultDetail} with the provided quiz and per-question results.
     *
     * @param _quiz            the quiz metadata and identity for these results
     * @param _resultQuestions the list of per-question result details
     */
    public ResultDetail(Quiz _quiz, List<ResultQuestion> _resultQuestions){
        this.quiz = _quiz;

        this.questions = _resultQuestions;
    }

    /**
     * Returns the quiz associated with these results.
     *
     * @return the {@link Quiz} for this result detail
     */
    public Quiz getQuiz(){
        return quiz;
    }

    /**
     * Sets (replaces) the quiz associated with these results.
     *
     * @param _quiz the {@link Quiz} to associate
     */
    public void setQuiz(Quiz _quiz){
        this.quiz = _quiz;
    }

    /**
     * Returns the list of per-question results.
     *
     * @return a list of {@link ResultQuestion} items
     */
    public List<ResultQuestion> getResultQuestions(){
        return questions;
    }

    /**
     * Sets (replaces) the list of per-question results.
     *
     * @param _resultQuestions the new list of {@link ResultQuestion} items
     */
    public void setResultQuestion(List<ResultQuestion> _resultQuestions){
        this.questions = _resultQuestions;
    }
}

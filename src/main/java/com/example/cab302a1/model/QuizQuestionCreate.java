package com.example.cab302a1.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a quiz question and its associated choices.
 * <p>
 * This model stores the question text, explanation, and all possible answer
 * choices linked to a specific quiz. It is mainly used when creating or editing
 * quizzes to manage question content and available options.
 * </p>
 */
public class QuizQuestionCreate {
    private int question_id;
    private int quiz_id;
    private String questionText;
    private List<QuizChoiceCreate> choices = new ArrayList<>();
    private String explanation;

    /**
     * Default constructor for creating an empty quiz question.
     */
    public QuizQuestionCreate() {
    }

    /**
     * Constructs a quiz question with full details.
     *
     * @param _question_id  the unique ID of the question
     * @param _quiz_id      the ID of the quiz this question belongs to
     * @param _questionText the text content of the question
     * @param _explanation  an optional explanation or hint for the question
     */
    public QuizQuestionCreate(int _question_id, int _quiz_id, String _questionText, String _explanation) {
        this.question_id = _question_id;
        this.quiz_id = _quiz_id;
        this.questionText = _questionText;
        this.explanation = _explanation;
    }

    /**
     * Sets the ID of the quiz this question belongs to.
     * @param _quizId the quiz ID
     */
    public void setQuizId(int _quizId) {
        quiz_id = _quizId;
    }

    /**
     * Sets the text content of the question.
     * @param questionText the question text
     */
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    /**
     * Sets the unique ID of the question.
     * @param _questionId the question ID
     */
    public void setQuestionId(int _questionId) {
        question_id = _questionId;
    }

    /**
     * Sets the explanation or additional information for the question.
     * @param _explanation the explanation text
     */
    public void setExplanation(String _explanation) {
        explanation = _explanation;
    }

    /**
     * Sets the list of available choices for this question.
     * @param _choices a list of {@link QuizChoiceCreate} objects
     */
    public void setOptions(List<QuizChoiceCreate> _choices) {
        this.choices = _choices;
    }

    /**
     * Returns the list of answer choices for this question.
     * @return a list of {@link QuizChoiceCreate} objects
     */
    public List<QuizChoiceCreate> getChoices() { return choices; }

    /**
     * Returns the question text.
     * @return the question text
     */
    public String getQuestionText() { return questionText; }

    /**
     * Returns the question ID.
     * @return the question ID
     */
    public int getQuestionId() { return question_id; }

    /**
     * Returns the quiz ID associated with this question.
     * @return the quiz ID
     */
    public int getQuizId() { return quiz_id; }

    /**
     * Returns the explanation or additional information for the question.
     * @return the explanation text
     */
    public String getExplanation() { return explanation; }
}

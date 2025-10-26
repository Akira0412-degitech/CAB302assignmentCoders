package com.example.cab302a1.model;

/**
 * Represents a possible answer choice for a quiz question.
 * <p>
 * Each choice belongs to a specific question and contains the option text
 * and an indicator showing whether it is the correct answer.
 * This model is mainly used when creating or managing quiz questions.
 * </p>
 */
public class QuizChoiceCreate {
    private int option_id;
    private int question_id;
    private String text;
    private boolean is_correct;

    /**
     * Default constructor.
     * <p>
     * Used when creating an empty choice instance or when data
     * will be set later using setter methods.
     * </p>
     */
    public QuizChoiceCreate() {
    }

    /**
     * Creates a new quiz choice with text and correctness flag.
     *
     * @param text       the text content of the choice
     * @param _is_correct true if this choice is the correct answer, false otherwise
     */
    public QuizChoiceCreate(String text, boolean _is_correct) {
        this.text = text;
        is_correct = _is_correct;
    }

    /**
     * Sets the unique option ID for this choice.
     * @param _optionId the option ID
     */
    public void setOption_id(int _optionId) {
        option_id = _optionId;
    }

    /**
     * Sets the ID of the question this choice belongs to.
     * @param _question_id the question ID
     */
    public void setQuestion_id(int _question_id) {
        question_id = _question_id;
    }

    /**
     * Sets the text content of the choice.
     * @param text the choice text
     */
    public void setText(String text) { this.text = text; }

    /**
     * Returns the option ID of this choice.
     * @return the option ID
     */
    public int getOption_id() { return option_id; }

    /**
     * Returns the ID of the question this choice belongs to.
     * @return the question ID
     */
    public int getQuestion_id() { return question_id; }

    /**
     * Returns the text content of this choice.
     * @return the choice text
     */
    public String getText() { return text; }

    /**
     * Returns whether this choice is the correct answer.
     * @return true if correct, false otherwise
     */
    public boolean isCorrect() { return is_correct; }

    /**
     * Sets whether this choice is correct.
     * @param is_correct true if correct, false otherwise
     */
    public void setIs_correct(boolean is_correct) { this.is_correct = is_correct; }
}

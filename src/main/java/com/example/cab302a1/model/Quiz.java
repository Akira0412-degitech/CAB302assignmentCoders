package com.example.cab302a1.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a quiz, containing metadata such as title, description, author, and visibility.
 * <p>
 * Each quiz can contain multiple questions represented by {@link QuizQuestionCreate}.
 * This class provides getters and setters to manage quiz details and the associated questions.
 * </p>
 */
public class Quiz {
    private int quiz_id;
    private String title;
    private String description;
    private int created_by;
    private boolean is_Hidden;
    private String authorUsername;
    private final List<QuizQuestionCreate> questions = new ArrayList<>();

    /**
     * Creates a new quiz with full details.
     *
     * @param quiz_id        the unique identifier for the quiz
     * @param title          the title of the quiz
     * @param description    a description of the quiz
     * @param created_by     the user ID of the quiz creator
     * @param is_Hidden      whether the quiz is hidden from students
     * @param authorUsername the username of the quiz author
     */
    public Quiz(int quiz_id, String title, String description, int created_by, boolean is_Hidden, String authorUsername) {
        this.quiz_id = quiz_id;
        this.title = title;
        this.description = description;
        this.created_by = created_by;
        this.is_Hidden = is_Hidden;
        this.authorUsername = authorUsername;
    }

    /**
     * Default constructor for creating an empty quiz.
     */
    public Quiz() {
    }

    /**
     * Returns the unique quiz ID.
     * @return the quiz ID
     */
    public int getQuizId() { return quiz_id; }

    /**
     * Returns the quiz title.
     * @return the title
     */
    public String getTitle() { return title; }

    /**
     * Returns the quiz description.
     * @return the description
     */
    public String getDescription() { return description; }

    /**
     * Returns the user ID of the creator.
     * @return the creator ID
     */
    public int getCreated_by() { return created_by; }

    /**
     * Indicates whether the quiz is hidden.
     * @return true if hidden, otherwise false
     */
    public boolean getIsHidden() { return is_Hidden; }

    /**
     * Returns the list of quiz questions.
     * @return a list of {@link QuizQuestionCreate} objects
     */
    public List<QuizQuestionCreate> getQuestions() { return questions; }

    /**
     * Sets the quiz ID.
     * @param _quizId the quiz ID
     */
    public void setQuizId(Integer _quizId) { this.quiz_id = _quizId; }

    /**
     * Sets the quiz title.
     * @param title the title
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Sets the quiz description.
     * @param description the description
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Sets the creator ID.
     * @param _created_by the creator ID
     */
    public void setCreated_by(Integer _created_by) { this.created_by = _created_by; }

    /**
     * Returns the username of the quiz author.
     * @return the author's username
     */
    public String getAuthorUsername() { return authorUsername; }

    /**
     * Sets the username of the quiz author.
     * @param authorUsername the author's username
     */
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }

    /**
     * Replaces the current list of questions with a new list.
     * @param qs a list of {@link QuizQuestionCreate} objects
     */
    public void setQuestions(List<QuizQuestionCreate> qs) {
        this.questions.clear();
        if (qs != null) {
            this.questions.addAll(qs);
        }
    }
}

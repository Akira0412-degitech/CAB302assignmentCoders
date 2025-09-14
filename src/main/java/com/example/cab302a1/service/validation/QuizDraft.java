package com.example.cab302a1.service.validation;

import java.util.ArrayList;
import java.util.List;

public class QuizDraft {
    private String title;
    private String description; // optional
    private List<QuestionDraft> questions = new ArrayList<>();

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<QuestionDraft> getQuestions() { return questions; }
    public void setQuestions(List<QuestionDraft> questions) { this.questions = questions; }

    public static class QuestionDraft {
        private String text;
        private List<String> choices = new ArrayList<>(); // size == 4
        private Integer correctIndex; // 0..3
        private String explanation; // optional

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        public List<String> getChoices() { return choices; }
        public void setChoices(List<String> choices) { this.choices = choices; }
        public Integer getCorrectIndex() { return correctIndex; }
        public void setCorrectIndex(Integer correctIndex) { this.correctIndex = correctIndex; }
        public String getExplanation() { return explanation; }
        public void setExplanation(String explanation) { this.explanation = explanation; }
    }
}

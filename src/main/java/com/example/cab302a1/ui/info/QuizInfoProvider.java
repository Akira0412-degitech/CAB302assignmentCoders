package com.example.cab302a1.ui.info;
import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.service.QuizService;



public class QuizInfoProvider {
    private final QuizService quizService;
    public QuizInfoProvider(QuizService quizService) { this.quizService = quizService; }

    public String build(Quiz quiz) {
        String teacher = (quiz.getAuthorUsername() != null && !quiz.getAuthorUsername().isBlank())
                ? quiz.getAuthorUsername() : "Unknown";
        int count = 0;
        try {
            Quiz full = quizService.loadQuizFully(quiz);
            if (full.getQuestions() != null) count = full.getQuestions().size();
        } catch (Exception ignored) {}
        String desc = (quiz.getDescription() == null || quiz.getDescription().isBlank())
                ? "(No description)" : quiz.getDescription();
        return "Tutor: " + teacher + "\nQuestions: " + count + "\nDescription: " + desc;
    }
}

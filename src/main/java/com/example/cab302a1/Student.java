package com.example.cab302a1;

import java.util.List;

/**
 * A simple class to model a student and their data.
 */
public class Student {
    private String name;
    private String studentNumber;
    private List<QuizResult> quizHistory;
    private List<String> bestSubjects;
    private List<String> worstSubjects;

    public Student(String name, String studentNumber, List<QuizResult> quizHistory, List<String> bestSubjects, List<String> worstSubjects) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.quizHistory = quizHistory;
        this.bestSubjects = bestSubjects;
        this.worstSubjects = worstSubjects;
    }

    public String getName() {
        return name;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public List<QuizResult> getQuizHistory() {
        return quizHistory;
    }

    public List<String> getBestSubjects() {
        return bestSubjects;
    }

    public List<String> getWorstSubjects() {
        return worstSubjects;
    }
}

package com.example.cab302a1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

/**
 * Controller class for the Student Main View UI.
 * This class handles interactions with the UI elements defined in student-main-view.fxml.
 */
public class StudentMainController {

    @FXML
    private Button dashboardBtn;
    @FXML
    private Button quizzesBtn;
    @FXML
    private Button reviewBtn;
    @FXML
    private Button timetableBtn;

    @FXML
    private Label studentNameLabel;
    @FXML
    private Label studentNumberLabel;
    @FXML
    private VBox quizHistoryList;
    @FXML
    private VBox bestSubjectsList;
    @FXML
    private VBox worstSubjectsList;

    private Student loggedInStudent;

    /**
     * Initializes the controller. This method is called automatically
     * after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        // Create a single mock student to represent the logged-in user
        this.loggedInStudent = createMockStudent();

        // Populate the UI with the student's details
        displayStudentDetails(this.loggedInStudent);

        // Set up button actions (currently just print to console)
        dashboardBtn.setOnAction(event -> System.out.println("Dashboard button clicked!"));
        quizzesBtn.setOnAction(event -> System.out.println("Quizzes button clicked!"));
        reviewBtn.setOnAction(event -> System.out.println("Review button clicked!"));
        timetableBtn.setOnAction(event -> System.out.println("Timetable button clicked!"));
    }

    /**
     * Creates a single mock student with sample data.
     * In a real application, this would be retrieved from a database.
     * @return A Student object.
     */
    private Student createMockStudent() {
        return new Student("John Smith", "n12345678",
                Arrays.asList(
                        new QuizResult("Quiz 1", "13/20"),
                        new QuizResult("Quiz 2", "18/20"),
                        new QuizResult("Quiz 3", "16/20"),
                        new QuizResult("Quiz 4", "19/20"),
                        new QuizResult("Quiz 5", "14/20")
                ),
                Arrays.asList("Mathematics", "Physics", "Software Engineering"),
                Arrays.asList("English", "History")
        );
    }

    /**
     * Displays the details for the logged-in student.
     * @param student The student to display details for.
     */
    private void displayStudentDetails(Student student) {
        studentNameLabel.setText("Student Name: " + student.getName());
        studentNumberLabel.setText("Student Number: " + student.getStudentNumber());

        // Update Quiz History
        quizHistoryList.getChildren().clear();
        for (QuizResult result : student.getQuizHistory()) {
            Label quizLabel = new Label(result.getQuizName() + ": " + result.getScore());
            quizLabel.getStyleClass().add("details-item");
            quizHistoryList.getChildren().add(quizLabel);
        }

        // Update Best Subjects
        bestSubjectsList.getChildren().clear();
        for (String subject : student.getBestSubjects()) {
            Label subjectLabel = new Label(subject);
            subjectLabel.getStyleClass().add("details-item");
            bestSubjectsList.getChildren().add(subjectLabel);
        }

        // Update Worst Subjects
        worstSubjectsList.getChildren().clear();
        for (String subject : student.getWorstSubjects()) {
            Label subjectLabel = new Label(subject);
            subjectLabel.getStyleClass().add("details-item");
            worstSubjectsList.getChildren().add(subjectLabel);
        }
    }
}

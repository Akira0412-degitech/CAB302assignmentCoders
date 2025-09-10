package com.example.cab302a1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Controller class for the Review Page UI (Tutor View).
 * This class handles interactions with the UI elements defined in review-page-view.fxml.
 */
public class ReviewPageController {

    @FXML
    private BorderPane mainPane;

    // Sidebar Buttons
    @FXML
    private Button dashboardBtn;
    @FXML
    private Button manageQuizzesBtn;
    @FXML
    private Button calendarBtn;
    @FXML
    private Button reviewBtn;

    // Main content area elements (for Tutor View)
    @FXML
    private VBox studentListContainer; // Container for the list of student names
    @FXML
    private Button assignReviewBtn; // Button to assign a review to the selected student
    @FXML
    private Button moreDetailsBtn; // New 'More Details' button

    // Labels and VBoxes within the student details section
    @FXML
    private Label studentNameLabel;
    @FXML
    private Label studentNumberLabel;
    @FXML
    private VBox quizHistoryContainer;
    @FXML
    private VBox bestSubjectsContainer;
    @FXML
    private VBox worstSubjectsContainer;

    private Student selectedStudent;

    /**
     * Initializes the controller. This method is automatically called after the FXML
     * file has been loaded and all @FXML annotated members have been injected.
     */
    @FXML
    public void initialize() {
        // --- Event Handlers for UI interactions (currently just print to console) ---
        if (dashboardBtn != null) {
            dashboardBtn.setOnAction(event -> System.out.println("Dashboard button clicked!"));
        }
        if (manageQuizzesBtn != null) {
            manageQuizzesBtn.setOnAction(event -> System.out.println("Manage Quizzes button clicked!"));
        }
        if (calendarBtn != null) {
            calendarBtn.setOnAction(event -> System.out.println("Calendar button clicked!"));
        }
        if (reviewBtn != null) {
            reviewBtn.setOnAction(event -> System.out.println("Review button clicked!"));
        }

        // Action for the "Assign Review" button
        if (assignReviewBtn != null) {
            assignReviewBtn.setOnAction(event -> onAssignReviewClicked());
        }

        // Action for the new "More Details" button - now opens the self-review form
        if (moreDetailsBtn != null) {
            moreDetailsBtn.setOnAction(event -> onMoreDetailsClicked());
        }

        // Create mock student data
        List<Student> students = createMockStudents();
        populateStudentList(students);

        // Display the first student's details by default
        if (!students.isEmpty()) {
            displayStudentDetails(students.get(0));
            selectedStudent = students.get(0);
        }
    }

    /**
     * Handles the "Assign Review" button click. This method will open a new dialog window
     * to allow the tutor to write a review for the selected student.
     */
    private void onAssignReviewClicked() {
        if (selectedStudent == null) {
            System.out.println("Please select a student to assign a review.");
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("review-form.fxml"));
            VBox root = fxmlLoader.load();
            ReviewFormController controller = fxmlLoader.getController();
            controller.setStudent(selectedStudent);

            Stage stage = new Stage();
            stage.setTitle("Assign Review to " + selectedStudent.getName());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the "More Details" button click. This method now opens the student's
     * self-review form in a new dialog.
     */
    private void onMoreDetailsClicked() {
        if (selectedStudent == null) {
            System.out.println("Please select a student to view more details.");
            return;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("student-self-review.fxml"));
            VBox root = fxmlLoader.load();
            StudentSelfReviewController controller = fxmlLoader.getController();
            controller.setStudent(selectedStudent);

            Stage stage = new Stage();
            stage.setTitle(selectedStudent.getName() + "'s Self-Review");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Creates a list of mock students with sample data.
     * @return A list of Student objects.
     */
    private List<Student> createMockStudents() {
        return Arrays.asList(
                new Student("Rick Owens", "n12345678",
                        Arrays.asList(
                                new QuizResult("Quiz 1", "13/20"),
                                new QuizResult("Quiz 2", "20/20")
                        ),
                        Arrays.asList("Mathematics", "Physics"),
                        Arrays.asList("English", "Liberal Arts")
                ),
                new Student("Bruce Wayne", "n102711911",
                        Arrays.asList(
                                new QuizResult("Quiz 1", "13/20"),
                                new QuizResult("Quiz 2", "20/20"),
                                new QuizResult("Quiz 3", "17/20"),
                                new QuizResult("Quiz 4", "9/20"),
                                new QuizResult("Quiz 5", "15/20")
                        ),
                        Arrays.asList("Mathematics", "Physics", "Quantum Mechanics"),
                        Arrays.asList("English", "Liberal Arts")
                ),
                new Student("Kim Chaewon", "n24681357",
                        Arrays.asList(
                                new QuizResult("Quiz 1", "18/20"),
                                new QuizResult("Quiz 2", "19/20"),
                                new QuizResult("Quiz 3", "20/20")
                        ),
                        Arrays.asList("Mathematics", "Physics"),
                        Arrays.asList("English", "Liberal Arts")
                )
        );
    }

    /**
     * Populates the student list container with buttons for each student.
     * @param students The list of students to display.
     */
    private void populateStudentList(List<Student> students) {
        studentListContainer.getChildren().clear(); // Clear existing mock buttons
        for (Student student : students) {
            Button studentBtn = new Button(student.getName());
            studentBtn.setMaxWidth(Double.MAX_VALUE);
            studentBtn.getStyleClass().add("student-list-item");
            studentBtn.setOnAction(event -> {
                displayStudentDetails(student);
                selectedStudent = student;
            });
            studentListContainer.getChildren().add(studentBtn);
        }
    }

    /**
     * Displays the details for a selected student.
     * @param student The student to display details for.
     */
    private void displayStudentDetails(Student student) {
        studentNameLabel.setText("Student Name: " + student.getName());
        studentNumberLabel.setText("Student Number: " + student.getStudentNumber());

        // Update Quiz History
        quizHistoryContainer.getChildren().clear();
        quizHistoryContainer.getChildren().add(new Label("Quiz History"));
        for (QuizResult result : student.getQuizHistory()) {
            Label quizLabel = new Label(result.getQuizName() + ": " + result.getScore());
            quizLabel.getStyleClass().add("details-item");
            quizHistoryContainer.getChildren().add(quizLabel);
        }

        // Update Best Subjects
        bestSubjectsContainer.getChildren().clear();
        bestSubjectsContainer.getChildren().add(new Label("Best Subjects:"));
        for (String subject : student.getBestSubjects()) {
            Label subjectLabel = new Label(subject);
            subjectLabel.getStyleClass().add("details-item");
            bestSubjectsContainer.getChildren().add(subjectLabel);
        }

        // Update Worst Subjects
        worstSubjectsContainer.getChildren().clear();
        worstSubjectsContainer.getChildren().add(new Label("Worst Subjects:"));
        for (String subject : student.getWorstSubjects()) {
            Label subjectLabel = new Label(subject);
            subjectLabel.getStyleClass().add("details-item");
            worstSubjectsContainer.getChildren().add(subjectLabel);
        }
    }
}

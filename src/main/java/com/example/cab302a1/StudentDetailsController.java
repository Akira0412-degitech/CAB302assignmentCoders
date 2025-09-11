package com.example.cab302a1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

/**
 * Controller for the detailed student view dialog.
 * Handles the logic for displaying a student's full details.
 */
public class StudentDetailsController {

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
    @FXML
    private Button closeButton;

    /**
     * Sets the student for this details view and populates the UI.
     * @param student The student object to display details for.
     */
    public void setStudent(Student student) {
        studentNameLabel.setText("Student Name: " + student.getName());
        studentNumberLabel.setText("Student Number: " + student.getStudentNumber());

        // Update Quiz History
        populateList(quizHistoryContainer, "Quiz History", student.getQuizHistory().stream().map(result -> result.getQuizName() + ": " + result.getScore()).toList());

        // Update Best Subjects
        populateList(bestSubjectsContainer, "Best Subjects", student.getBestSubjects());

        // Update Worst Subjects
        populateList(worstSubjectsContainer, "Worst Subjects", student.getWorstSubjects());
    }

    /**
     * Initializes the controller. Sets up button actions.
     */
    @FXML
    public void initialize() {
        closeButton.setOnAction(event -> handleClose());
    }

    /**
     * Handles the close button click.
     * Closes the details window.
     */
    private void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Helper method to populate a VBox with a list of labels.
     * @param container The VBox to add labels to.
     * @param title The title for the section.
     * @param items The list of strings to display.
     */
    private void populateList(VBox container, String title, List<String> items) {
        container.getChildren().clear();
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("details-title-smaller");
        container.getChildren().add(titleLabel);
        for (String item : items) {
            Label itemLabel = new Label(item);
            itemLabel.getStyleClass().add("details-item");
            container.getChildren().add(itemLabel);
        }
    }
}

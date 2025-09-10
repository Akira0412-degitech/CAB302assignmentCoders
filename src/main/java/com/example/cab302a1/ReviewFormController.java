package com.example.cab302a1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * Controller for the review form dialog.
 * Handles the logic for a tutor to submit a review for a student.
 */
public class ReviewFormController {

    @FXML
    private Label studentNameLabel;
    @FXML
    private TextArea reviewTextArea;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;

    private Student student;

    /**
     * Sets the student for this review form and updates the UI.
     * @param student The student object to be reviewed.
     */
    public void setStudent(Student student) {
        this.student = student;
        studentNameLabel.setText("Review for " + student.getName());
    }

    /**
     * Initializes the controller. Sets up button actions.
     */
    @FXML
    public void initialize() {
        submitButton.setOnAction(event -> handleSubmit());
        cancelButton.setOnAction(event -> handleCancel());
    }

    /**
     * Handles the submit button click.
     * For now, this will just print the review to the console.
     */
    private void handleSubmit() {
        if (student == null) {
            System.out.println("Error: No student selected for review.");
            return;
        }

        String reviewText = reviewTextArea.getText();
        System.out.println("Submitting review for " + student.getName() + ":");
        System.out.println(reviewText);

        // Close the window after submission
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles the cancel button click.
     * Closes the review form window without submitting.
     */
    private void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}

package com.example.cab302a1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;

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
    private Button createQuizBtn;
    @FXML
    private Button manageQuizzesBtn;
    @FXML
    private Button studentsBtn;
    @FXML
    private Button calendarBtn;
    @FXML
    private Button reviewBtn;
    @FXML
    private Button settingsBtn; // New button
    @FXML
    private Button exitBtn;     // New button

    // Header elements (updated for Tutor View)
    // No specific element for quiz title here, but you can add it if needed later
    // The "Retry Incorrect Answers" button is removed from Tutor View FXML

    // Main content area elements (for Tutor View)
    @FXML
    private VBox studentListContainer; // Container for the list of student names
    @FXML
    private Button assignReviewBtn; // Button to assign a review to the selected student

    // Labels within the student details section
    @FXML
    private Label studentNameLabel; // You would dynamically set this
    @FXML
    private Label studentNumberLabel; // You would dynamically set this

    // You can add more labels/containers for quiz history, best/worst subjects if you intend to update them dynamically
    // For now, they are static in FXML

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
        if (createQuizBtn != null) {
            createQuizBtn.setOnAction(event -> System.out.println("Create Quiz button clicked!"));
        }
        if (manageQuizzesBtn != null) {
            manageQuizzesBtn.setOnAction(event -> System.out.println("Manage Quizzes button clicked!"));
        }
        if (studentsBtn != null) {
            studentsBtn.setOnAction(event -> System.out.println("Students button clicked!"));
        }
        if (calendarBtn != null) {
            calendarBtn.setOnAction(event -> System.out.println("Calendar button clicked!"));
        }
        if (reviewBtn != null) {
            reviewBtn.setOnAction(event -> System.out.println("Review button clicked!"));
        }
        if (settingsBtn != null) {
            settingsBtn.setOnAction(event -> System.out.println("Settings button clicked!"));
        }
        if (exitBtn != null) {
            exitBtn.setOnAction(event -> System.out.println("Exit button clicked!"));
        }

        // Action for the "Assign Review" button
        if (assignReviewBtn != null) {
            assignReviewBtn.setOnAction(event -> System.out.println("Assign Review button clicked for selected student!"));
        }

        // You would typically populate the studentListContainer dynamically here
        // or add logic for clicking on student names to update the details section.
    }
}

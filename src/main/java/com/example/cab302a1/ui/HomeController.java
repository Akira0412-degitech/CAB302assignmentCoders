package com.example.cab302a1.ui;

import com.example.cab302a1.dao.QuizDao;
import com.example.cab302a1.dao.AttemptDao;
import com.example.cab302a1.dao.ResponseDao;
import com.example.cab302a1.model.QuestionResponse;
import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.model.QuizChoiceCreate;
import com.example.cab302a1.result.QuizResultController;
import com.example.cab302a1.result.QuizResultData;
import com.example.cab302a1.service.QuizService;
import com.example.cab302a1.util.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.example.cab302a1.components.NavigationManager;


import com.example.cab302a1.DBconnection;

/**
 * Shared controller for both Student and Teacher home.
 * Behavior (read-only vs. editable) is toggled by UserRole.
 */
public class HomeController implements Initializable {

    @FXML
    private TilePane grid;

    @FXML
    private Label pageTitle;

    // Store full Quiz objects instead of just titles
    private final List<Quiz> quizzes = new ArrayList<>();

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        // Register this page with NavigationManager for proper navigation history
        NavigationManager.getInstance().setCurrentPage(NavigationManager.Pages.HOME);
        
        // Set role-based page title
        setupPageTitle();
        
        // Update navbar to show Home as active
        com.example.cab302a1.components.NavbarController.updateNavbarState("home");
        
        refresh();
        
        System.out.println("Home page initialized and registered with NavigationManager");
    }

    /**
     * Set the page title based on user role
     */
    private void setupPageTitle() {
        if (pageTitle != null) {
            if (Session.isTeacher()) {
                pageTitle.setText("Build Your Next Quiz!");
            } else {
                pageTitle.setText("Your Learning Journey!");
            }
        }
    }


    /** Rebuild the grid according to the current role and quiz list. */
    public void refresh() {
        grid.getChildren().clear();
        quizzes.clear();
        QuizDao quizDao = new QuizDao();

        // Load all quizzes
        quizzes.addAll(quizDao.getAllQuizzes());

        // Create quiz cards
        for (Quiz q : quizzes) {
            grid.getChildren().add(createQuizCard(q));
        }

        // Add plus card for teachers
        if (Session.isTeacher()) {
            grid.getChildren().add(createPlusCard());
        }

        // Update page title in case role changed
        setupPageTitle();
    }

/**
     * Create a quiz card with basic styling and functionality
     */
    private Button createQuizCard(Quiz quiz) {
        Button card = new Button(quiz.getTitle());
        card.getStyleClass().add("quiz-card");
        card.setPrefSize(200, 150);  // Updated to prototype dimensions
        card.setWrapText(true);

        // Check if this quiz is completed by the current student
        if (Session.isStudent()) {
            int userId = (Session.getCurrentUser() != null) ? Session.getCurrentUser().getUser_id() : 0;
            AttemptDao attemptDao = new AttemptDao();
            boolean isCompleted = attemptDao.hasCompleted(quiz.getQuizId(), userId);
            
            if (isCompleted) {
                // Add a style class for completed quizzes
                card.getStyleClass().add("quiz-card-completed");
            }
        }

        card.setOnAction(e -> {
            Stage owner = (Stage) grid.getScene().getWindow();
            handleQuizCardClick(owner, quiz);
        });

        return card;
    }




    //When click - S or T using session
    private void handleQuizCardClick(Stage owner, Quiz quiz) {
        if (Session.isStudent()) {
            int userId = (Session.getCurrentUser() != null) ? Session.getCurrentUser().getUser_id() : 0;
            int quizId = quiz.getQuizId();
            AttemptDao attemptDao = new AttemptDao();

            // Check if the student has already completed this quiz
            if (attemptDao.hasCompleted(quizId, userId)) {
                // Show the quiz result summary page instead of allowing retake
                try {
                    Integer score = attemptDao.getScore(quizId, userId);
                    QuizService quizService = new QuizService();
                    Quiz fullQuiz = quizService.loadQuizFully(quiz);
                    int total = fullQuiz.getQuestions().size();

                    FXMLLoader fxml = new FXMLLoader(getClass().getResource(
                            "/com/example/cab302a1/result/QuizResult.fxml"));
                    Parent root = fxml.load();
                    QuizResultController rc = fxml.getController();

                    rc.setQuizResult(new QuizResultData(
                            score != null ? score : 0, total, fullQuiz.getTitle(), quizId, userId
                    ));

                    Scene resultScene = new Scene(root, 1000, 650);
                    
                    // Load CSS stylesheet
                    java.net.URL cssUrl = getClass().getResource("/com/example/cab302a1/result/QuizResult.css");
                    if (cssUrl != null) {
                        resultScene.getStylesheets().add(cssUrl.toExternalForm());
                    }
                    
                    owner.setScene(resultScene);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Error loading quiz results: " + ex.getMessage(), ButtonType.OK).showAndWait();
                }
                return;
            }

            // Quiz load
            QuizService quizService = new QuizService();
            Quiz fullQuiz = quizService.loadQuizFully(quiz);

            // Start attempt
            int attemptId = attemptDao.startAttempt(quizId, userId);
            if (attemptId <= 0 /*|| !attemptDao.attemptExist(attemptId)*/) {
                new Alert(Alert.AlertType.ERROR, "Failed to start attempt.", ButtonType.OK).showAndWait();
                return;
            }


            // Quiz open
            StudentTakeQuizController.open(owner, fullQuiz, selections -> {
                try {
                    java.util.List<QuestionResponse> responses = new java.util.ArrayList<>();

                    java.util.List<com.example.cab302a1.model.QuizQuestionCreate> questions = fullQuiz.getQuestions();
                    for (int i = 0; i < questions.size(); i++) {
                        int selectedIndex = (i < selections.size() ? selections.get(i) : -1);
                        if (selectedIndex < 0) {
                            continue; // Not selected answer ignore
                        }

                        com.example.cab302a1.model.QuizQuestionCreate q = questions.get(i);
                        java.util.List<com.example.cab302a1.model.QuizChoiceCreate> choices = q.getChoices();
                        if (selectedIndex >= choices.size()) continue;

                        com.example.cab302a1.model.QuizChoiceCreate chosen = choices.get(selectedIndex);


                        QuestionResponse r = new QuestionResponse();
                        r.setAttempt_id(attemptId);
                        r.setQuestion_id(q.getQuestionId());
                        r.setOption_id(chosen.getOption_id());
                        r.setIs_correct(chosen.isCorrect());

                        responses.add(r);
                    }

                    // not selected anything message -> need to imporve
                    if (responses.isEmpty()) {
                        new javafx.scene.control.Alert(
                                javafx.scene.control.Alert.AlertType.WARNING,
                                "Please select at least one answer before submitting."
                        ).showAndWait();
                        return;
                    }

                    //using DAO
                    new ResponseDao().saveResponse(attemptId, responses);

                    // end attempt
                    attemptDao.endAttempt(attemptId);

                    //result page prep
                    Integer score = attemptDao.getScore(quizId, userId);
                    int total = fullQuiz.getQuestions().size();

                    FXMLLoader fxml = new FXMLLoader(getClass().getResource(
                            "/com/example/cab302a1/result/QuizResult.fxml"));
                    Parent root = fxml.load();
                    QuizResultController rc = fxml.getController();

                    rc.setQuizResult(new com.example.cab302a1.result.QuizResultData(
                            score != null ? score : 0, total, fullQuiz.getTitle(), quizId, userId
                    ));

                    Scene resultScene = new Scene(root, 1000, 650);
                    
                    // Load CSS stylesheet - this was missing and causing the styling issue
                    java.net.URL cssUrl = getClass().getResource("/com/example/cab302a1/result/QuizResult.css");
                    if (cssUrl != null) {
                        resultScene.getStylesheets().add(cssUrl.toExternalForm());
                        System.out.println("Quiz result CSS loaded successfully after quiz completion");
                    } else {
                        System.err.println("Warning: Quiz result CSS not found");
                    }
                    
                    owner.setScene(resultScene);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    new javafx.scene.control.Alert(
                            javafx.scene.control.Alert.AlertType.ERROR,
                            "Failed to submit quiz. " + ex.getMessage()
                    ).showAndWait();
                }
            });


        } else {
            TeacherQuizDetailController.open(owner, quiz, updated -> refresh());
        }
    }


    /** Teacher-only '+' card to open the quiz editor modal. */
    private Button createPlusCard() {
        Button plus = new Button("+");
        plus.getStyleClass().add("plus-card");
        plus.setPrefSize(200, 150);  // Updated to prototype dimensions
        plus.setOnAction(e -> openCreateQuizEditor());
        return plus;
    }

    /** Open QuizEditor as a modal dialog; when saved, append to list and refresh UI. */
    private void openCreateQuizEditor() {
        Stage owner = (Stage) grid.getScene().getWindow();
        QuizEditorController.open(owner, quiz -> {
            // Receive the created Quiz from the editor via callback
            quizzes.add(quiz);
            refresh(); // re-render grid with the new card
        });
    }


    /** Convenience info dialog. */
    private void info(String header, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(header);
        a.showAndWait();
    }

    /** Convenience warning dialog. */
    private void warn(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }

}

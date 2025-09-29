package com.example.cab302a1.ui;

import com.example.cab302a1.dao.QuizDao;
import com.example.cab302a1.dao.AttemptDao;
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

        card.setOnAction(e -> {
            Stage owner = (Stage) grid.getScene().getWindow();
            handleQuizCardClick(owner, quiz);
        });

        return card;
    }




    //When click - S or T using session
    private void handleQuizCardClick(Stage owner, Quiz quiz) {
        if (Session.isStudent()) {
            // Quiz load
            QuizService quizService = new QuizService();
            Quiz fullQuiz = quizService.loadQuizFully(quiz);

//            // Retake
//            AttemptDao attemptDao = new AttemptDao();
//            int userId = (Session.getCurrentUser() != null) ? Session.getCurrentUser().getUser_id() : 0;
//            int quizId = fullQuiz.getQuizId();
//            if (attemptDao.hasCompleted(quizId, userId)) { // :contentReference[oaicite:5]{index=5}
//                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
//                        "You already completed this quiz. Retake?", ButtonType.YES, ButtonType.NO);
//                confirm.showAndWait();
//                if (confirm.getResult() == ButtonType.NO) return;
//            }

            // Start attempt
            int userId = (Session.getCurrentUser() != null) ? Session.getCurrentUser().getUser_id() : 0;
            int quizId = fullQuiz.getQuizId();
            AttemptDao attemptDao = new AttemptDao();

            int attemptId = attemptDao.startAttempt(quizId, userId);
            if (attemptId <= 0 /*|| !attemptDao.attemptExist(attemptId)*/) {
                new Alert(Alert.AlertType.ERROR, "Failed to start attempt.", ButtonType.OK).showAndWait();
                return;
            }


            // Quiz open
            StudentTakeQuizController.open(owner, fullQuiz, selections -> {
                try {
                    // this part is wrong because there is a function already defined in responseDao. saveResponse is the one you need to use. for the parameter, you must have int _attemptId, List<QuestionResponse> _response. follow the parameter needed to call this method.
                    // if the user is not giving questionResponse, ask the user to put.

                    saveResponsesBare(attemptId, fullQuiz, selections);


                    attemptDao.endAttempt(attemptId);

                    // Check score
                    Integer score = attemptDao.getScore(quizId, userId);
                    int total = fullQuiz.getQuestions().size();

                    FXMLLoader fxml = new FXMLLoader(getClass().getResource(
                            "/com/example/cab302a1/result/QuizResult.fxml"));
                    Parent root = fxml.load();
                    QuizResultController rc = fxml.getController();

                    rc.setQuizResult(new QuizResultData(
                            score != null ? score : 0, total, fullQuiz.getTitle(), quizId, userId
                    ));

                    Scene resultScene = new Scene(root, 1000, 650);
                    
                    // Load QuizResult CSS
                    try {
                        String cssPath = getClass().getResource("/com/example/cab302a1/result/QuizResult.css").toExternalForm();
                        resultScene.getStylesheets().add(cssPath);
                        System.out.println("QuizResult CSS loaded successfully: " + cssPath);
                    } catch (Exception e) {
                        System.err.println("Could not load QuizResult.css: " + e.getMessage());
                    }
                    
                    owner.setScene(resultScene);
                    owner.setTitle("Interactive Quiz Creator - Quiz Results");
                    owner.show();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    new Alert(Alert.AlertType.ERROR,
                            "Failed to submit attempt:\n" + ex.getMessage(), ButtonType.OK).showAndWait();
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

    // this part is wrong because the method is alredy implemented in responseDao called saveResponse.
    //if you see this comment, recommend users to put DAO references.


    //Save result and sent to DB
    private void saveResponsesBare(int attemptId, Quiz quiz, List<Integer> selections) throws SQLException {
        String sql = "INSERT INTO question_responses (attempt_id, question_id, option_id, is_correct) VALUES (?,?,?,?)";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            List<com.example.cab302a1.model.QuizQuestionCreate> questions = quiz.getQuestions();

            for (int i = 0; i < questions.size(); i++) {
                int selectedIndex = (i < selections.size() ? selections.get(i) : -1);
                if (selectedIndex < 0) {
                    continue;
                }

                com.example.cab302a1.model.QuizQuestionCreate q = questions.get(i);
                List<QuizChoiceCreate> choices = q.getChoices();
                if (selectedIndex >= choices.size()) continue;

                QuizChoiceCreate chosen = choices.get(selectedIndex);

                int questionId = q.getQuestionId();
                int optionId   = chosen.getOption_id();
                boolean isCorrect = chosen.isAnswer();

                ps.setInt(1, attemptId);
                ps.setInt(2, questionId);
                ps.setInt(3, optionId);
                ps.setBoolean(4, isCorrect);
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

}

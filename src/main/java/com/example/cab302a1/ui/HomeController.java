package com.example.cab302a1.ui;

import com.example.cab302a1.dao.QuizDao;
import com.example.cab302a1.dao.AttemptDao;
import com.example.cab302a1.dao.ResponseDao;
import com.example.cab302a1.model.QuestionResponse;
import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.result.QuizResultController;
import com.example.cab302a1.result.QuizResultData;
import com.example.cab302a1.service.QuizService;
import com.example.cab302a1.shared.HiddenQuizRegistry;
import com.example.cab302a1.shared.ServiceRegistry;
import com.example.cab302a1.ui.Student.StudentTakeQuizController;
import com.example.cab302a1.ui.Teacher.TeacherQuizDetailController;
import com.example.cab302a1.ui.Teacher.TeacherQuizEditorController;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import java.util.Optional;

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
            if (HiddenQuizRegistry.isHidden(q.getQuizId())) continue; //Hidden quiz not appear
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
    private javafx.scene.Node createQuizCard(Quiz quiz) {
        Button cardBtn = new Button(quiz.getTitle());
        cardBtn.getStyleClass().add("quiz-card");
        cardBtn.setPrefSize(200, 150);  // Updated to prototype dimensions
        cardBtn.setWrapText(true);

        // Check if this quiz is completed by the current student
        if (Session.isStudent()) {
            int userId = (Session.getCurrentUser() != null) ? Session.getCurrentUser().getUser_id() : 0;
            AttemptDao attemptDao = new AttemptDao();
            boolean isCompleted = attemptDao.hasCompleted(quiz.getQuizId(), userId);

            if (isCompleted) {
                // Add a style class for completed quizzes
                cardBtn.getStyleClass().add("quiz-card-completed");
            }
        }

        cardBtn.setOnAction(e -> {
            Stage owner = (Stage) grid.getScene().getWindow();
            handleQuizCardClick(owner, quiz);
        });

        // StackPane - X button overlay
        StackPane cardPane = new StackPane(cardBtn);
        StackPane.setAlignment(cardBtn, Pos.CENTER);

        if (Session.isTeacher()) {
            Button closeBtn = new Button("×");
            closeBtn.getStyleClass().add("delete-btn");
            StackPane.setAlignment(closeBtn, Pos.TOP_LEFT);
            StackPane.setMargin(closeBtn, new Insets(6, 0, 0, 6));
            closeBtn.setOnAction(ev -> {
                ev.consume();
                onHideClicked(quiz, cardPane);
            });
            cardPane.getChildren().add(closeBtn);
        }

        return cardPane;
    }

    private void onHideClicked(Quiz quiz, Node cardNode) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete this quiz?");
        alert.setHeaderText("This quiz will be delete.");
        ButtonType ok = new ButtonType("Delete", ButtonType.OK.getButtonData());
        ButtonType cancel = ButtonType.CANCEL;
        alert.getButtonTypes().setAll(ok, cancel);

        Optional<ButtonType> res = alert.showAndWait();
        if (res.isPresent() && res.get() == ok) {
            HiddenQuizRegistry.hide(quiz.getQuizId());
            grid.getChildren().remove(cardNode); // delete only in UI
        }
    }


    //When click - S or T using session
    private void handleQuizCardClick(Stage owner, Quiz quiz) {
        if (HiddenQuizRegistry.isHidden(quiz.getQuizId())) {
            new Alert(Alert.AlertType.INFORMATION,
                    "This quiz is not available. Please choose another quiz.",
                    ButtonType.OK).showAndWait();
            return;
        }

        if (Session.isStudent()) {
            int userId = (Session.getCurrentUser() != null) ? Session.getCurrentUser().getUser_id() : 0;
            int quizId = quiz.getQuizId();
            AttemptDao attemptDao = new AttemptDao();

            // If already completed the quiz, go to the results page
            if (attemptDao.hasCompleted(quizId, userId)) {
                Integer score = attemptDao.getScore(quizId, userId);
                QuizService quizService = new QuizService();
                Quiz fullQuiz = quizService.loadQuizFully(quiz);
                int total = fullQuiz.getQuestions().size();

                ServiceRegistry.nav().toQuizResult(owner,
                        new com.example.cab302a1.result.QuizResultData(
                                score != null ? score : 0, total, fullQuiz.getTitle(), quizId, userId
                        ));
                return;
            }

            // If incomplete, start attempt → Open quiz screen
            int attemptId = attemptDao.startAttempt(quizId, userId);
            if (attemptId <= 0) {
                new Alert(Alert.AlertType.ERROR, "Failed to start attempt.", ButtonType.OK).showAndWait();
                return;
            }

            QuizService quizService = new QuizService();
            Quiz fullQuiz = quizService.loadQuizFully(quiz);

            ServiceRegistry.nav().toStudentTakeQuiz(owner, fullQuiz, selections -> {
                try {
                    List<QuestionResponse> responses = new ArrayList<>();
                    var questions = fullQuiz.getQuestions();
                    for (int i = 0; i < questions.size(); i++) {
                        int idx = (i < selections.size()) ? selections.get(i) : -1;
                        if (idx < 0) continue;

                        var q = questions.get(i);
                        var choices = q.getChoices();
                        if (idx >= choices.size()) continue;

                        var chosen = choices.get(idx);
                        QuestionResponse r = new QuestionResponse();
                        r.setAttempt_id(attemptId);
                        r.setQuestion_id(q.getQuestionId());
                        r.setOption_id(chosen.getOption_id());
                        r.setIs_correct(chosen.isCorrect());
                        responses.add(r);
                    }
                    if (responses.isEmpty()) {
                        new Alert(Alert.AlertType.WARNING, "Please select at least one answer.").showAndWait();
                        return;
                    }

                    new ResponseDao().saveResponse(attemptId, responses);
                    attemptDao.endAttempt(attemptId);

                    Integer score = attemptDao.getScore(quizId, userId);
                    int total = fullQuiz.getQuestions().size();

                    ServiceRegistry.nav().toQuizResult(owner,
                            new com.example.cab302a1.result.QuizResultData(
                                    score != null ? score : 0, total, fullQuiz.getTitle(), quizId, userId
                            ));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Failed to submit quiz. " + ex.getMessage(), ButtonType.OK).showAndWait();
                }
            });
            return;
        }

        TeacherQuizDetailController.open(owner, quiz, updated -> refresh());
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
        TeacherQuizEditorController.open(owner, quiz -> {
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

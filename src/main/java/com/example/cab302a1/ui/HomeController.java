package com.example.cab302a1.ui;
import com.example.cab302a1.ui.action.HideQuizAction;
import com.example.cab302a1.ui.info.QuizInfoProvider;
import com.example.cab302a1.ui.view.components.HoverInfoButton;
import javafx.scene.control.Tooltip;
import com.example.cab302a1.dao.QuizDao;
import com.example.cab302a1.dao.AttemptDao;
import com.example.cab302a1.dao.ResponseDao;
import com.example.cab302a1.model.QuestionResponse;
import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.result.QuizResultController;
import com.example.cab302a1.result.QuizResultData;
import com.example.cab302a1.service.QuizService;
import com.example.cab302a1.ui.view.card.QuizCardFactory;



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
import javafx.application.Platform;
import javafx.scene.Node;


import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import java.util.Optional;
import com.example.cab302a1.components.NavigationManager;

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

    private final QuizCardFactory cardFactory = new QuizCardFactory();

    private final HideQuizAction hideAction = new HideQuizAction(new QuizDao());
    private final QuizInfoProvider infoProvider = new QuizInfoProvider(new QuizService());


    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        // Register this page with NavigationManager for proper navigation history
        NavigationManager.getInstance().setCurrentPage(NavigationManager.Pages.HOME);

        // Set role-based page title
        setupPageTitle();
        
        // Update navbar to show Home as active
        com.example.cab302a1.components.NavbarController.updateNavbarState("home");

        //after scene
        Platform.runLater(this::refresh);


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

        // Load all quizzes (only Visible)
        List<Quiz> all = quizDao.getAllQuizzes();
        for (Quiz q : all) {
            if (!q.getIsHidden()) {           // IsHidden filtering
                quizzes.add(q);
                grid.getChildren().add(buildQuizCardForRole(q));
            }
        }

        if (Session.isTeacher()) {
            grid.getChildren().add(createPlusCard());
        }
        setupPageTitle();
    }


    //Refactoring needs? cuz Code title weired
    @Deprecated
    private javafx.scene.Node createQuizCard(Quiz quiz) {
        return buildQuizCardForRole(quiz);
    }

    // 1) Assemble cards according to the role (student/teacher branch core)
    private javafx.scene.Node buildQuizCardForRole(Quiz quiz) {
        if (Session.isStudent()) {
            return cardFactory.buildStudentCard(
                    quiz,
                    () -> infoProvider.build(quiz), // ← Supplier로 지연 제공
                    e -> {
                        Stage owner = (Stage) ((Node) e.getSource()).getScene().getWindow();
                        handleQuizCardClick(owner, quiz);
                    }
            );
        } else {
            return cardFactory.buildTeacherCard(
                    quiz,
                    e -> {
                        Stage owner = (Stage) ((Node) e.getSource()).getScene().getWindow();
                        handleQuizCardClick(owner, quiz);
                    },
                    cardNode -> hideAction.confirmAndHide(quiz, cardNode, grid)
            );
        }
    }


    // 2) Common skeleton: Create only the central title button, bind the handler in the role deco
    private StackPane buildBaseCard(Quiz quiz) {
        StackPane cardPane = new StackPane();
        Button titleBtn = new Button(quiz.getTitle() != null ? quiz.getTitle() : "Untitled");
        titleBtn.getStyleClass().add("quiz-card");     // 기존 카드 스타일 유지
        titleBtn.setPrefSize(200, 150);                // 기존 프로토타입 사이즈 유지
        titleBtn.setWrapText(true);
        StackPane.setAlignment(titleBtn, Pos.CENTER);
        cardPane.getChildren().add(titleBtn);
        return cardPane;
    }

    // 3) Student Deco: '!' (tooltip) + click to display student flow
    private void decorateForStudent(StackPane card, Quiz quiz) {
        Button titleBtn = (Button) card.getChildren().get(0);

        // 기존 buildQuizInfoText(quiz) 재사용 (지금 단계에서는 그대로)
        Tooltip tip = new Tooltip(buildQuizInfoText(quiz));
        Tooltip.install(titleBtn, tip);

        // 기존 handleQuizCardClick을 그대로 사용(학생/교사 분기를 내부에서 처리)
        titleBtn.setOnAction(e -> {
            Stage owner = (Stage) grid.getScene().getWindow();
            handleQuizCardClick(owner, quiz);
        });
    }

    // 4) Teacher Deco: Click on the top left '×' (hidden) + details/edit
    private void decorateForTeacher(StackPane card, Quiz quiz) {
        Button titleBtn = (Button) card.getChildren().get(0);

        // '×' 버튼(기존 코드 로직 그대로)
        Button closeBtn = new Button("×");
        closeBtn.getStyleClass().add("delete-btn");
        StackPane.setAlignment(closeBtn, Pos.TOP_LEFT);
        StackPane.setMargin(closeBtn, new Insets(6, 0, 0, 6));
        closeBtn.setOnAction(ev -> {
            ev.consume();
            onHideClicked(quiz, card);  // 기존 onHideClicked 재사용
        });
        card.getChildren().add(closeBtn);

        // 제목 클릭 시 기존 교사 클릭 흐름(= handleQuizCardClick 내부 분기)
        titleBtn.setOnAction(e -> {
            Stage owner = (Stage) grid.getScene().getWindow();
            handleQuizCardClick(owner, quiz);
        });
    }

    private void onHideClicked(Quiz quiz, javafx.scene.Node cardNode) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete this quiz?");
        alert.setHeaderText("This quiz will be delete.");
        ButtonType ok = new ButtonType("Delete", ButtonType.OK.getButtonData());
        ButtonType cancel = ButtonType.CANCEL;
        alert.getButtonTypes().setAll(ok, cancel);

        Optional<ButtonType> res = alert.showAndWait();
        if (res.isPresent() && res.get() == ok) {
            QuizDao dao = new QuizDao();
            dao.UpdateQuizStatus(quiz.getQuizId(), true);
            grid.getChildren().remove(cardNode);
        }
    }


    //When click - S or T using session
    private void handleQuizCardClick(Stage owner, Quiz quiz) {
        QuizDao dao = new QuizDao();
        Quiz latest = dao.getQuizById(quiz.getQuizId());
        if (latest != null && latest.getIsHidden()) {
            new Alert(Alert.AlertType.INFORMATION,
                    "This quiz is not available. Please choose another quiz.",
                    ButtonType.OK).showAndWait();
            return;
        }

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


    //QuizInfoText
    private String buildQuizInfoText(Quiz quiz) {
        // 1) Tutor username
        String teacher = (quiz.getAuthorUsername() != null && !quiz.getAuthorUsername().isBlank())
                ? quiz.getAuthorUsername()
                : "Unknown";

        // 2) Q Number
        int questionCount = 0;
        try {
            QuizService qs = new QuizService();
            Quiz full = qs.loadQuizFully(quiz);
            if (full.getQuestions() != null) {
                questionCount = full.getQuestions().size();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 3) description
        String desc = (quiz.getDescription() == null || quiz.getDescription().isBlank())
                ? "(No description)"
                : quiz.getDescription();

        return "Tutor: " + teacher + "\n" +
                "Questions: " + questionCount + "\n" +
                "Description: " + desc;
    }

}

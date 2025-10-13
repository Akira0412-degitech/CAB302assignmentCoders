package com.example.cab302a1.ui;
import com.example.cab302a1.ui.action.HideQuizAction;
import com.example.cab302a1.ui.info.QuizInfoProvider;
import com.example.cab302a1.dao.QuizDao;
import com.example.cab302a1.dao.AttemptDao;
import com.example.cab302a1.dao.ResponseDao;
import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.service.QuizService;
import com.example.cab302a1.ui.view.card.QuizCardFactory;
import com.example.cab302a1.ui.action.CreateQuizAction;
import com.example.cab302a1.ui.flow.QuizFlow;
import com.example.cab302a1.ui.flow.StudentQuizFlow;
import com.example.cab302a1.ui.flow.TeacherQuizFlow;
import com.example.cab302a1.util.Session;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.Node;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.util.ResourceBundle;
import com.example.cab302a1.components.NavigationManager;


//Shared controller for both Student and Teacher home.
//Behavior (read-only vs editable) is toggled by UserRole.
public class HomeController implements Initializable {

    @FXML
    private TilePane grid;

    @FXML
    private Label pageTitle;

    // Store full Quiz objects instead of just titles
    private final List<Quiz> quizzes = new ArrayList<>();

    private final QuizCardFactory cardFactory = new QuizCardFactory();
    private final QuizFlow studentFlow = new StudentQuizFlow(new AttemptDao(), new ResponseDao(), new QuizService());
    private final QuizFlow teacherFlow = new TeacherQuizFlow();

    private final HideQuizAction hideAction = new HideQuizAction(new QuizDao());
    private final QuizInfoProvider infoProvider = new QuizInfoProvider(new QuizService());
    private CreateQuizAction createAction; // '+' Card action


    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        // Register this page with NavigationManager for proper navigation history
        NavigationManager.getInstance().setCurrentPage(NavigationManager.Pages.HOME);

        // Set role-based page title
        setupPageTitle();

        // Reset Card action (+ button)
        // open quiz editor
        this.createAction = new CreateQuizAction(e -> {
            // Stage = safety
            Stage owner = (Stage) ((Node) e.getSource()).getScene().getWindow();
            QuizEditorController.open(owner, createdQuiz -> {
                quizzes.add(createdQuiz);
                refresh();
            });
        });

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
            grid.getChildren().add(createAction.buildPlusCard());
        }

        setupPageTitle();
    }


    // 1) Assemble cards according to the role (student/teacher branch core)
    private javafx.scene.Node buildQuizCardForRole(Quiz quiz) {
        if (Session.isStudent()) {
            return cardFactory.buildStudentCard(
                    quiz,
                    () -> infoProvider.build(quiz),
                    e -> {
                        Stage owner = (Stage) ((Node) e.getSource()).getScene().getWindow();
                        studentFlow.open(owner, quiz, null);
                    }
            );
        } else {
            return cardFactory.buildTeacherCard(
                    quiz,
                    e -> {
                        Stage owner = (Stage) ((Node) e.getSource()).getScene().getWindow();
                        teacherFlow.open(owner, quiz, this::refresh);
                    },
                    cardNode -> hideAction.confirmAndHide(quiz, cardNode, grid)
            );
        }
    }

}

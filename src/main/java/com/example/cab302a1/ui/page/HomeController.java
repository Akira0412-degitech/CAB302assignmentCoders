package com.example.cab302a1.ui.page;

import com.example.cab302a1.ui.page.editor.QuizEditorController;
import com.example.cab302a1.ui.action.HideQuizAction;
import com.example.cab302a1.ui.info.QuizInfoProvider;
import com.example.cab302a1.dao.QuizDao;
import com.example.cab302a1.dao.AttemptDao;
import com.example.cab302a1.dao.DaoFactory;
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

/**
 * Controller for the shared Home page used by both students and teachers.
 * <p>
 * Displays a grid of available quizzes and dynamically changes behavior
 * depending on the logged-in user's role:
 * <ul>
 *   <li><b>Student:</b> Can view available quizzes, take them, or review results.</li>
 *   <li><b>Teacher:</b> Can view, edit, create, and hide quizzes.</li>
 * </ul>
 * </p>
 *
 * @since 1.0
 */
public class HomeController implements Initializable {

    /** Grid container displaying all quiz cards. */
    @FXML
    private TilePane grid;

    /** Label displaying the page title. */
    @FXML
    private Label pageTitle;

    /** List storing all visible quizzes retrieved from the database. */
    private final List<Quiz> quizzes = new ArrayList<>();

    /** Factory for generating quiz cards for display. */
    private final QuizCardFactory cardFactory = new QuizCardFactory();

    /** Handles quiz flow for student users (taking or reviewing quizzes). */
    private final QuizFlow studentFlow =
            new StudentQuizFlow(DaoFactory.getAttemptDao(), DaoFactory.getResponseDao(), new QuizService());

    /** Handles quiz flow for teacher users (managing quizzes). */
    private final QuizFlow teacherFlow = new TeacherQuizFlow();

    /** Action handler for hiding teacher-owned quizzes. */
    private final HideQuizAction hideAction = new HideQuizAction(DaoFactory.getQuizDao());

    /** Provides detailed quiz info for display in tooltips or popups. */
    private final QuizInfoProvider infoProvider = new QuizInfoProvider(new QuizService());

    /** Action for creating new quizzes (via '+' button). */
    private CreateQuizAction createAction;

    /**
     * Initializes the Home page for either student or teacher role.
     * <p>
     * Registers the page with {@link NavigationManager}, configures
     * the "+" quiz creation button, and schedules a refresh of quiz cards.
     * </p>
     *
     * @param location  the location of the FXML resource
     * @param resources the resource bundle (if any)
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        // Register this page with NavigationManager for proper navigation history
        NavigationManager.getInstance().setCurrentPage(NavigationManager.Pages.HOME);

        // Configure "+" button to open Quiz Editor
        this.createAction = new CreateQuizAction(e -> {
            Stage owner = (Stage) ((Node) e.getSource()).getScene().getWindow();
            QuizEditorController.open(owner, createdQuiz -> {
                quizzes.add(createdQuiz);
                refresh();
            });
        });

        // Update navigation bar to show "Home" as active
        com.example.cab302a1.components.NavbarController.updateNavbarState("home");

        // Refresh quiz cards after scene has been rendered
        Platform.runLater(this::refresh);
    }

    /**
     * Rebuilds the quiz grid depending on the current user's role.
     * <p>
     * - Teachers see only their own quizzes and can create new ones.<br>
     * - Students see all public quizzes available to them.
     * </p>
     */
    public void refresh() {
        grid.getChildren().clear();
        quizzes.clear();

        QuizDao quizDao = DaoFactory.getQuizDao();
        List<Quiz> all = new ArrayList<>();

        if (Session.isTeacher()) {
            all = quizDao.getQuizByTeacherId(Session.getCurrentUser().getUser_id());
        } else if (Session.isStudent()) {
            all = quizDao.getAllQuizzes();
        }

        for (Quiz q : all) {
            Boolean hidden = q.getIsHidden();
            if (hidden == null || !hidden) {   // Only show visible quizzes
                quizzes.add(q);
                grid.getChildren().add(buildQuizCardForRole(q));
            }
        }

        if (Session.isTeacher()) {
            grid.getChildren().add(createAction.buildPlusCard());
        }
    }

    /**
     * Builds a quiz card based on the current user's role.
     * <p>
     * - For students: Displays quiz completion status and navigation to attempt/review.<br>
     * - For teachers: Enables editing, hiding, and info viewing.
     * </p>
     *
     * @param quiz the quiz object to render in the grid
     * @return a JavaFX Node representing the quiz card
     */
    private javafx.scene.Node buildQuizCardForRole(Quiz quiz) {
        if (Session.isStudent()) {
            AttemptDao attemptDao = DaoFactory.getAttemptDao();
            int userId = (Session.getCurrentUser() != null) ? Session.getCurrentUser().getUser_id() : 0;
            boolean isCompleted = attemptDao.hasCompleted(quiz.getQuizId(), userId);

            return cardFactory.buildStudentCard(
                    quiz,
                    () -> infoProvider.build(quiz),
                    e -> {
                        Stage owner = (Stage) ((Node) e.getSource()).getScene().getWindow();
                        ((StudentQuizFlow) studentFlow).openOrShowResult(owner, quiz);
                    },
                    isCompleted
            );
        } else {
            // Teacher Card: allows edit, hide, and info actions
            return cardFactory.buildTeacherCard(
                    quiz,
                    e -> {
                        Stage owner = (Stage) ((Node) e.getSource()).getScene().getWindow();
                        teacherFlow.open(owner, quiz, this::refresh);
                    },
                    cardNode -> hideAction.confirmAndHide(quiz, cardNode, grid),
                    () -> infoProvider.build(quiz)
            );
        }
    }
}

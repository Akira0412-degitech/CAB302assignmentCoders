package com.example.cab302a1.ui.page.review.result;

//import com.example.cab302a1.dao.jdbc.JdbcOptionDao;
//import com.example.cab302a1.dao.jdbc.JdbcQuestionDao;
//import com.example.cab302a1.dao.jdbc.JdbcQuizDao;
//import com.example.cab302a1.dao.jdbc.JdbcResponseDao;
import com.example.cab302a1.dao.DaoFactory;
import com.example.cab302a1.model.QuizChoiceCreate;
import com.example.cab302a1.result.ResultDetail;
import com.example.cab302a1.result.ResultQuestion;
import com.example.cab302a1.service.QuizResultDetailService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class StudentResultDetailController {

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private VBox questionsBox;
    @FXML private Button doneButton;

    private Stage stage;
    private Runnable onDone;

    /**
     * Opens the result detail page as a modal dialog.
     * @param owner The parent stage
     * @param quizId The quiz ID
     * @param attemptId The attempt ID
     * @param onDone Optional callback when done button is clicked
     */
    public static void open(Stage owner, int quizId, int attemptId, Runnable onDone) {
        try {
            FXMLLoader loader = new FXMLLoader(StudentResultDetailController.class.getResource(
                    "/com/example/cab302a1/result/StudentResultDetailPage.fxml"));
            Parent root = loader.load();
            StudentResultDetailController controller = loader.getController();

            Stage stage = new Stage();
            stage.initOwner(owner);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Quiz Results - Detailed Review");
            
            Scene scene = new Scene(root, 920, 720);
            
            // Load CSS
            java.net.URL cssUrl = StudentResultDetailController.class.getResource(
                    "/com/example/cab302a1/result/StudentResultDetail.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }
            
            stage.setScene(scene);

            controller.stage = stage;
            controller.onDone = onDone;
            controller.loadResultDetail(quizId, attemptId);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to open result detail page:\n" + e.getMessage()).showAndWait();
        }
    }

    /**
     * Loads the result detail data and displays it
     */
    private void loadResultDetail(int quizId, int attemptId) {
        try {
            // Use QuizResultDetailService to get the data
            QuizResultDetailService service = new QuizResultDetailService(
                    DaoFactory.getQuizDao(), DaoFactory.getQuestionDao(), DaoFactory.getOptionDao(), DaoFactory.getResponseDao()
            );
            
            ResultDetail resultDetail = service.getResultDetail(attemptId, quizId);
            
            if (resultDetail == null) {
                new Alert(Alert.AlertType.ERROR, "Failed to load quiz results").showAndWait();
                if (stage != null) stage.close();
                return;
            }
            
            // Set quiz title and description
            var quiz = resultDetail.getQuiz();
            String title = (quiz != null && quiz.getTitle() != null) ? quiz.getTitle() : "Quiz Results";
            String desc  = (quiz != null && quiz.getDescription() != null) ? quiz.getDescription() : "Review your answers below";

            titleLabel.setText(title);
            descriptionLabel.setText(desc);

            if (quiz == null) {
                System.err.println("[ResultDetail] quiz is null - check service wiring");
            }

            // Load questions
            questionsBox.getChildren().clear();
            List<ResultQuestion> questions = resultDetail.getResultQuestions();
            
            if (questions == null || questions.isEmpty()) {
                Label noQuestions = new Label("No questions found");
                questionsBox.getChildren().add(noQuestions);
                return;
            }
            
            int total = questions.size();
            for (int i = 0; i < total; i++) {
                ResultQuestion rq = questions.get(i);
                
                try {
                    FXMLLoader fxml = new FXMLLoader(getClass().getResource(
                            "/com/example/cab302a1/result/ResultQuestionItem.fxml"));
                    Node node = fxml.load();
                    ResultQuestionItemController item = fxml.getController();

                    // Set question index
                    item.setIndex(i + 1, total);
                    
                    // Set question text
                    item.setQuestionText(rq.getQuestion().getQuestionText());

                    // Set answer texts
                    List<QuizChoiceCreate> choices = rq.getQuestion().getChoices();
                    List<String> answerTexts = choices.stream()
                            .map(QuizChoiceCreate::getText)
                            .collect(Collectors.toList());
                    item.setAnswers(answerTexts);

                    // Find correct answer index and user's selected answer index
                    int correctIndex = -1;
                    int selectedIndex = -1;
                    int chosenOptionId = rq.getChosenOption_id();
                    
                    for (int j = 0; j < choices.size(); j++) {
                        QuizChoiceCreate choice = choices.get(j);
                        if (choice.isCorrect()) {
                            correctIndex = j;
                        }
                        if (choice.getOption_id() == chosenOptionId) {
                            selectedIndex = j;
                        }
                    }

                    // Highlight the answers
                    item.highlightAnswers(correctIndex, selectedIndex);
                    
                    // Set explanation if available
                    String explanation = rq.getQuestion().getExplanation();
                    item.setExplanation(explanation);

                    questionsBox.getChildren().add(node);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.err.println("Failed to load question item: " + ex.getMessage());
                }
            }
            
            // Set up done button
            doneButton.setOnAction(ev -> {
                if (onDone != null) {
                    onDone.run();
                }
                stage.close();
            });
            
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading quiz results: " + e.getMessage()).showAndWait();
            if (stage != null) stage.close();
        }
    }
}


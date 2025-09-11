package com.example.cab302a1.ui;

import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.model.QuizChoiceCreate;
import com.example.cab302a1.model.QuizQuestionCreate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.function.Consumer;

public class TeacherQuizDetailController {

    @FXML private Label titleLabel;
    @FXML private Label descLabel;
    @FXML private VBox questionsBox;

    private Stage stage;
    private Quiz quiz;
    private Consumer<Quiz> onUpdated;

    /** 안전하게 FXML을 로드하고 상세 팝업을 띄움 */
    public static void open(Stage owner, Quiz quiz, Consumer<Quiz> onUpdated) {
        try {
            // ⚠️ 리소스 경로 확인: 보통은 슬래시 패턴이 맞습니다.
            URL fxml = TeacherQuizDetailController.class.getResource(
                    "/com/example/cab302a1/TeacherQuizDetail.fxml");      // ← 파일명이 TeacherQuizDetail.fxml 인 경우
            if (fxml == null) {
                // 혹시 파일명이 QuizDetail.fxml 이라면 아래 주석 해제
                // fxml = TeacherQuizDetailController.class.getResource("/com/example/cab302a1/QuizDetail.fxml");
            }

            if (fxml == null) {
                throw new IllegalStateException(
                        "Detail FXML not found. Expected at /com/example/cab302a1/TeacherQuizDetail.fxml (or QuizDetail.fxml)");
            }

            FXMLLoader loader = new FXMLLoader(fxml);
            VBox root = loader.load();

            TeacherQuizDetailController c = loader.getController();
            c.stage = new Stage();
            c.stage.initOwner(owner);
            c.stage.initModality(Modality.WINDOW_MODAL);
            c.stage.setTitle("Quiz Detail");
            c.stage.setScene(new Scene(root, 560, 480));

            c.setData(quiz, onUpdated);
            c.stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // 디버깅을 돕는 임시 경고
            javafx.scene.control.Alert a = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR,
                    "Failed to open detail: " + e.getMessage());
            a.setHeaderText("Load Error");
            a.showAndWait();
        }
    }

    private void setData(Quiz quiz, Consumer<Quiz> onUpdated) {
        this.quiz = quiz;
        this.onUpdated = onUpdated;
        render();
    }

    private void render() {
        titleLabel.setText(quiz.getTitle() == null ? "(Untitled)" : quiz.getTitle());
        descLabel.setText(quiz.getDescription() == null ? "" : quiz.getDescription());

        questionsBox.getChildren().clear();
        int qIdx = 1;
        for (QuizQuestionCreate q : quiz.getQuestions()) {
            VBox block = new VBox(4);

            Label qLabel = new Label(qIdx + ". " + (q.getQuestionText() == null ? "" : q.getQuestionText()));
            qLabel.setStyle("-fx-font-weight: bold;");
            block.getChildren().add(qLabel);

            int cIdx = 0;
            for (QuizChoiceCreate ch : q.getChoices()) {
                String mark = ch.isCorrect() ? " (✓)" : "";
                String text = ch.getText() == null ? "" : ch.getText();
                Label cLabel = new Label(" - " + (char)('A' + cIdx) + ". " + text + mark);
                block.getChildren().add(cLabel);
                cIdx++;
            }

            HBox sep = new HBox();
            sep.setStyle("-fx-border-color: #e0e0e0; -fx-border-width: 0 0 1 0; -fx-padding: 6 0 0 0;");
            questionsBox.getChildren().addAll(block, sep);
            qIdx++;
        }
    }

    @FXML
    private void onEdit() {
        Stage owner = (Stage) questionsBox.getScene().getWindow();
        QuizEditorController.openForEdit(owner, quiz, updated -> {
            this.quiz = updated;
            render(); // 상세 갱신
            if (onUpdated != null) onUpdated.accept(updated); // 홈 갱신
        });
    }

    @FXML
    private void onClose() {
        stage.close();
    }
}

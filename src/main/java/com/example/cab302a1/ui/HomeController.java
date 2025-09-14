package com.example.cab302a1.ui;

import com.example.cab302a1.UserRole;
import com.example.cab302a1.model.Quiz;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import com.example.cab302a1.util.DemoDataFactory;


public class HomeController {

    @FXML private TilePane grid;

    private UserRole role = UserRole.STUDENT;     // Role (after i will change)
    private final List<Quiz> quizzes = new ArrayList<>();

    @FXML
    public void initialize(){

        for (String t : List.of("Java Basics", "OOP Essentials", "Exceptions")) {

            quizzes.add(DemoDataFactory.demoQuizB(t));    //Dummy data
        }
        refresh();
    }

    public void setRole(UserRole role) {
        this.role = role;
        refresh();
    }

    // divided with ROLE
    public void refresh() {
        grid.getChildren().clear();

        for (Quiz q : quizzes) {
            grid.getChildren().add(buildQuizCard(q));   // Card UI
        }

        // ONLY Teacher + card
        if (role == UserRole.TEACHER) {
            grid.getChildren().add(buildPlusCard());
        }
    }

//make card UI using OnClick
    private Button buildQuizCard(Quiz quiz) {
        Button card = new Button(quiz.getTitle());
        card.getStyleClass().add("quiz-card");
        card.setPrefSize(160, 160);
        card.setWrapText(true);

        card.setOnAction(e -> onQuizCardClick(quiz));   // when click setting other roles
        return card;
    }

    //when click card divided role
    private void onQuizCardClick(Quiz quiz) {
        Stage owner = (Stage) grid.getScene().getWindow();

        if (role == UserRole.STUDENT) {
            // student: take quiz
            StudentTakeQuizController.open(owner, quiz, selections -> {
                System.out.println("[Student] selections = " + selections);
            });
        } else {
            // Teacher: Edit and Details
            TeacherQuizDetailController.open(owner, quiz, updated -> refresh());
        }
    }

// Teacher UI
    private Button buildPlusCard() {
        Button plus = new Button("+");
        plus.getStyleClass().add("plus-card");
        plus.setPrefSize(160, 160);
        plus.setOnAction(e -> openCreateQuizEditor());
        return plus;
    }

// Teacher Edit
    private void openCreateQuizEditor() {
        Stage owner = (Stage) grid.getScene().getWindow();
        QuizEditorController.open(owner, quiz -> {
            quizzes.add(quiz);
            refresh();
        });
    }

}

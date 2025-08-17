package com.example.cab302a1.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.TilePane;

import java.util.List;

public class HomeGridController {

    @FXML private TilePane grid;

    @FXML
    public void initialize() {
        // test
        List<String> quizNames = List.of(
                "Java test", "OOP", "Math",
                "A", "B", "Cd",
                "D", "E"
        );

        grid.getChildren().clear();
        for (String name : quizNames) {
            grid.getChildren().add(createQuizCard(name));
        }
    }

    private Button createQuizCard(String title) {
        Button card = new Button(title);
        card.getStyleClass().add("quiz-card");
        // Square card size
        card.setPrefSize(160, 160);
        card.setWrapText(true);
        card.setOnAction(e -> showInfo("Select Quiz", "Select Quiz: " + title));
        return card;
    }

    private void showInfo(String header, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(header);
        a.showAndWait();
    }
}

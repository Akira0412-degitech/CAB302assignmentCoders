package com.example.cab302a1.ui;

import com.example.cab302a1.model.QuizResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TeacherReviewController {

    // Main content only — navbar buttons are handled by NavbarController
    // Sidebar buttons — public for unit tests
    @FXML public Button dashboardBtn;
    @FXML public Button quizzesBtn;
    @FXML public Button reviewStudentsBtn;
    @FXML public Button exitBtn;

    // Student list + details
    @FXML public VBox studentListContainer;
    @FXML public Label studentNameLabel;
    @FXML public Button assignReviewBtn;

    // TableView + Columns
    @FXML public TableView<QuizResult> quizTable;
    @FXML public TableColumn<QuizResult, String> quizNameCol;
    @FXML public TableColumn<QuizResult, String> scoreCol;
    @FXML public TableColumn<QuizResult, Void> resultCol;

    @FXML public Button reviewBtn;            // for whatever “Review” button the test expects
    @FXML public TableColumn<?, ?> studentNameCol; // if your test wants a column showing student names
    @FXML public TableColumn<?, ?> viewBtnCol;     // if your test wants the “View” button column

    private final ObservableList<QuizResult> quizData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set default student
        if (studentNameLabel != null) {
            studentNameLabel.setText("Student Name: Name 5");
        }

        // Assign Review button action
        if (assignReviewBtn != null) {
            assignReviewBtn.setOnAction(e -> System.out.println("Assign Review clicked"));
        }

        // Set constrained resize policy
        quizTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Table column bindings
        quizNameCol.setCellValueFactory(data -> data.getValue().quizNameProperty());
        scoreCol.setCellValueFactory(data -> data.getValue().scoreProperty());

        // "View Result" button column
        resultCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("View");

            {
                btn.getStyleClass().add("action-button");
                btn.setOnAction(e -> {
                    QuizResult item = getTableView().getItems().get(getIndex());
                    System.out.println("Viewing result for " + item.getQuizName());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(btn));
            }
        });

        // Mock quiz data
        quizData.addAll(
                new QuizResult("Quiz 1", "16/20"),
                new QuizResult("Quiz 2", "18/20"),
                new QuizResult("Quiz 3", "12/20"),
                new QuizResult("Quiz 4", "20/20"),
                new QuizResult("Quiz 5", "15/20")
        );

        if (quizTable != null) {
            quizTable.setItems(quizData);
        }
    }
}

package com.example.cab302a1.ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class HomeController {

    @FXML private ListView<String> todoList;
    @FXML private ListView<String> doneList;

    @FXML
    public void initialize() {
        todoList.setItems(FXCollections.observableArrayList(
                "Java Basic",
                "OOP Basic",
                "Math Basic"
        ));
        doneList.setItems(FXCollections.observableArrayList(
                "Science(Complete)",
                "History(Complete)"
        ));
    }
}

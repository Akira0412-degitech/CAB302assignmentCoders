package com.example.cab302a1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class PlannerPage extends Application {

    @Override
    public void init() {

    }

    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader loader = new FXMLLoader (
                Objects.requireNonNull(getClass().getResource("/com/example/cab302a1/Planner/Planner.fxml"))
        );
        Scene scene = new Scene(loader.load(), 1000, 600);

        stage.setTitle("Quiz App");
        stage.setScene(scene); //please work
        stage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}

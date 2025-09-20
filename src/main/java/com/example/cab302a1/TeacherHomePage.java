//package com.example.cab302a1;
//import com.example.cab302a1.components.NavigationManager;
//
//import com.example.cab302a1.ui.HomeController;
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//import java.net.URL;
//import java.util.Objects;
//
//public class TeacherHomePage extends Application {
//    @Override
//    public void start(Stage stage) throws Exception {
//        URL fxml = Objects.requireNonNull(
//                getClass().getResource("/com/example/cab302a1/HomePage.fxml"));
//        FXMLLoader loader = new FXMLLoader(fxml);
//        Scene scene = new Scene(loader.load(), 1100, 680);
//
//        URL css = getClass().getResource("/HomePage.css");
//        if (css != null) scene.getStylesheets().add(css.toExternalForm());
//
////        HomeController c = loader.getController();
////        c.setRole(UserRole.TEACHER);
////        c.refresh();
//
//        stage.setTitle("Quiz App — Teacher");
//        stage.setScene(scene);
//        stage.show();
//    }
//    public static void main(String[] args) { launch(args); }
//}

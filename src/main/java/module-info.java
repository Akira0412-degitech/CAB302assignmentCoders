module com.example.cab302a1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires ical4j.core;


    opens com.example.cab302a1 to javafx.fxml;
    exports com.example.cab302a1;
    opens com.example.cab302a1.Login to javafx.fxml;
    opens com.example.cab302a1.SignUp to javafx.fxml;
    opens com.example.cab302a1.ui to javafx.fxml;
    exports com.example.cab302a1.ui;

    exports com.example.cab302a1.Planner;
    opens com.example.cab302a1.Planner to javafx.fxml;

    uses net.fortuna.ical4j.validate.CalendarValidatorFactory;

}
module com.example.cab302a1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cab302a1 to javafx.fxml;
    exports com.example.cab302a1;
    opens com.example.cab302a1.Login to javafx.fxml;
}
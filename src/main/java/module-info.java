module com.example.cab302a1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires java.prefs;

    // JDBC & Flyway
    requires mysql.connector.j;
    requires flyway.core;
    requires javafx.graphics;
    requires jbcrypt;
    requires org.jdbi.v3.core;
    //requires com.example.cab302a;

    // FXML
    opens com.example.cab302a1 to javafx.fxml;
    opens com.example.cab302a1.Login to javafx.fxml;
    opens com.example.cab302a1.SignUp to javafx.fxml;
    opens com.example.cab302a1.ui to javafx.fxml;
    opens com.example.cab302a1.components to javafx.fxml;
    opens com.example.cab302a1.logout to javafx.fxml;
    opens com.example.cab302a1.result to javafx.fxml;

    // for Flyway to read migration path
    opens db.migration;

    // export
    exports com.example.cab302a1;
    exports com.example.cab302a1.ui;
    exports com.example.cab302a1.components;
    exports com.example.cab302a1.logout;
    exports com.example.cab302a1.result;
    exports com.example.cab302a1.demo;
    exports com.example.cab302a1.ui.view.components;
    opens com.example.cab302a1.ui.view.components to javafx.fxml;


}

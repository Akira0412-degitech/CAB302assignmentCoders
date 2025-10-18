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

    opens com.example.cab302a1.components to javafx.fxml;
    opens com.example.cab302a1.logout to javafx.fxml;
    opens com.example.cab302a1.result to javafx.fxml;

    // for Flyway to read migration path
    opens db.migration;

    // export
    exports com.example.cab302a1;

    exports com.example.cab302a1.components;
    exports com.example.cab302a1.logout;
    exports com.example.cab302a1.result;
    exports com.example.cab302a1.demo;
    exports com.example.cab302a1.ui.view.components;
    opens com.example.cab302a1.ui.view.components to javafx.fxml;
    exports com.example.cab302a1.ui.page;
    opens com.example.cab302a1.ui.page to javafx.fxml;
    exports com.example.cab302a1.ui.page.editor;
    opens com.example.cab302a1.ui.page.editor to javafx.fxml;
    exports com.example.cab302a1.ui.page.teacher;
    opens com.example.cab302a1.ui.page.teacher to javafx.fxml;
    exports com.example.cab302a1.ui.page.student;
    opens com.example.cab302a1.ui.page.student to javafx.fxml;
    exports com.example.cab302a1.ui.view.components.question;
    opens com.example.cab302a1.ui.view.components.question to javafx.fxml;
    exports com.example.cab302a1.ui.page.review.student;
    opens com.example.cab302a1.ui.page.review.student to javafx.fxml;
    exports com.example.cab302a1.ui.page.review.teacher;
    opens com.example.cab302a1.ui.page.review.teacher to javafx.fxml;
    exports com.example.cab302a1.ui.page.review;
    opens com.example.cab302a1.ui.page.review to javafx.fxml;
    exports com.example.cab302a1.ui.page.review.result;
    opens com.example.cab302a1.ui.page.review.result to javafx.fxml;


}

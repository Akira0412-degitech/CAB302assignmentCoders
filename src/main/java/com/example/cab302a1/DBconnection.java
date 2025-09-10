package com.example.cab302a1;

import java.sql.*;
import org.flywaydb.core.Flyway;
import java.util.logging.*;

public class DBconnection {
    private static final String URL =
            "jdbc:mysql://localhost:3306/cab302_quizapp?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "appuser";
    private static final String PASSWORD = "AppPass#2025";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void migrate() {
        //not to show flyway log
        Logger flywayLogger = Logger.getLogger("org.flywaydb");
        flywayLogger.setLevel(Level.WARNING);
        for (var h : Logger.getLogger("").getHandlers()) {
            h.setLevel(Level.WARNING);
        }

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found!", e);
        }

        Flyway flyway = Flyway.configure()
                .dataSource(URL, USER, PASSWORD)
                .load();

        flyway.migrate();
        System.out.println("Flyway migration applied");
    }

}

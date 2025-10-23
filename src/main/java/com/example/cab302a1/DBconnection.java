package com.example.cab302a1;

import java.sql.*;
import org.flywaydb.core.Flyway;
import java.util.logging.*;
import org.jdbi.v3.core.Jdbi;

/**
 * {@code DBconnection} provides a single, shared database access point
 * for both JDBC and JDBI operations.
 *
 * <p>This class follows a <b>Singleton utility</b> design pattern:
 * all methods and the JDBI instance are static, ensuring a single
 * consistent connection configuration across the entire application.</p>
 */

public final class DBconnection {

    // Read from environment variables first; fall back to defaults if not set
    private static final String URL =
            System.getenv().getOrDefault("DB_URL",
                    "jdbc:mysql://localhost:3306/cab302_quizapp?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");

    private static final String USER =
            System.getenv().getOrDefault("DB_USER", "appuser");

    private static final String PASSWORD =
            System.getenv().getOrDefault("DB_PASS", "AppPass#2025");


    private DBconnection() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }


    /**
     * Get a JDBC connection using the configured URL, USER, and PASSWORD.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static final Jdbi jdbi = Jdbi.create(URL, USER, PASSWORD);

    public static Jdbi getJdbi() {
        return jdbi;
    }

    /**
     * Run Flyway migrations against the configured database.
     * This ensures the schema is up-to-date before tests or runtime.
     */
    public static void migrate() {
        // Suppress Flyway logging output to keep logs clean
        Logger flywayLogger = Logger.getLogger("org.flywaydb");
        flywayLogger.setLevel(Level.WARNING);
        for (var h : Logger.getLogger("").getHandlers()) {
            h.setLevel(Level.WARNING);
        }

        try {
            // Ensure MySQL driver is loaded
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found!", e);
        }

        // Configure Flyway with current DB connection info
        Flyway flyway = Flyway.configure()
                .dataSource(URL, USER, PASSWORD)
                .load();

        // Repair any corrupted Flyway history and apply migrations
        flyway.repair();
        flyway.migrate();

        System.out.println("Flyway migration applied");
    }
}

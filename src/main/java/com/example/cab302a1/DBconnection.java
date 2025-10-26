package com.example.cab302a1;

import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import java.util.logging.*;

/**
 * Provides a centralized database connection utility for JDBI operations.
 * <p>
 * This class follows the <b>Singleton design pattern</b>, ensuring a single shared
 * {@link Jdbi} instance across the application. It also supports Flyway migrations
 * to keep the database schema synchronized.
 * </p>
 *
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Expose a single shared {@link Jdbi} instance for all DAO classes</li>
 *   <li>Run Flyway migrations to maintain the database schema</li>
 * </ul>
 *
 * <p>Usage Example:</p>
 * <pre>
 *     Jdbi jdbi = DBconnection.getJdbi();
 *     DBconnection.migrate();
 * </pre>
 */
public final class DBconnection {

    // Database credentials (environment variable first, then defaults)
    private static final String URL =
            System.getenv().getOrDefault("DB_URL",
                    "jdbc:mysql://localhost:3306/cab302_quizapp?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");

    private static final String USER =
            System.getenv().getOrDefault("DB_USER", "appuser");

    private static final String PASSWORD =
            System.getenv().getOrDefault("DB_PASS", "AppPass#2025");

    /**
     * Private constructor prevents instantiation.
     * <p>
     * Enforces the Singleton design pattern by disallowing creation of class instances.
     * </p>
     */
    private DBconnection() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /** Shared JDBI instance for the entire application. */
    private static final Jdbi jdbi = Jdbi.create(URL, USER, PASSWORD);

    /**
     * Returns the shared {@link Jdbi} instance.
     * <p>
     * Ensures all DAO classes use a single consistent configuration and connection
     * management strategy throughout the application.
     * </p>
     *
     * @return the shared {@link Jdbi} instance
     */
    public static Jdbi getJdbi() {
        return jdbi;
    }

    /**
     * Provides a plain JDBC connection for legacy code or testing.
     * <p>
     * While most DAOs use {@link #getJdbi()}, this method allows
     * direct JDBC access for backward compatibility in tests or setup scripts.
     * </p>
     *
     * @return an open {@link java.sql.Connection} to the database
     * @throws java.sql.SQLException if the connection fails
     */
    public static java.sql.Connection getConnection() throws java.sql.SQLException {
        return java.sql.DriverManager.getConnection(URL, USER, PASSWORD);
    }


    /**
     * Executes Flyway migrations to update the database schema.
     * <p>
     * Automatically repairs migration history and applies any pending schema changes.
     * Flyway logging is suppressed for cleaner console output.
     * </p>
     */
    public static void migrate() {
        // Suppress Flyway logging output
        Logger flywayLogger = Logger.getLogger("org.flywaydb");
        flywayLogger.setLevel(Level.WARNING);
        for (var handler : Logger.getLogger("").getHandlers()) {
            handler.setLevel(Level.WARNING);
        }

        try {
            // Ensure MySQL driver is loaded
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found!", e);
        }

        // Configure and run Flyway migrations
        Flyway flyway = Flyway.configure()
                .dataSource(URL, USER, PASSWORD)
                .load();

        flyway.repair();
        flyway.migrate();

    }
}

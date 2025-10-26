package com.example.cab302a1;

import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link DBconnection}.
 * <p>
 * Verifies that the JDBI connection is properly initialized and
 * Flyway migrations can be executed successfully.
 * </p>
 */
public class DBconnectionTest {

    /**
     * Ensures that the shared JDBI instance is created successfully.
     */
    @Test
    void testJdbiInstanceNotNull() {
        Jdbi jdbi = DBconnection.getJdbi();
        assertNotNull(jdbi, "JDBI instance should not be null");
    }

    /**
     * Verifies that Flyway migration runs without throwing an exception.
     * <p>
     * This test checks schema synchronization and ensures that the
     * migration process completes successfully.
     * </p>
     */
    @Test
    void testFlywayMigrationRunsSuccessfully() {
        assertDoesNotThrow(DBconnection::migrate, "Flyway migration should run without exceptions");
    }
}

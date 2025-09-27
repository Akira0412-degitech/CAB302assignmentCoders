package com.example.cab302a1;

import jdk.jfr.Experimental;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DBconnectionTest {
    @Test
    void testMySQLConnection() throws Exception {
        try(Connection conn = DBconnection.getConnection()){
            assertNotNull(conn);
            assertTrue(conn.isValid(5));
        }
    }
}

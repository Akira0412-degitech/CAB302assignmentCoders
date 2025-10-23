//package com.example.cab302a1.dao;
//
//import com.example.cab302a1.DBconnection;
//import com.example.cab302a1.dao.jdbc.JdbcResponseDao;
//import com.example.cab302a1.model.QuestionResponse;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockedStatic;
//
//import java.sql.*;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
///**
// * Unit tests for {@link JdbcResponseDao}.
// *
// * Each method is tested for:
// *  - Normal operation
// *  - No result / empty data
// *  - SQLException handling
// */
//public class ResponseDaoTest {
//
//    // --- saveResponse() -----------------------------------------------------
//
//    /** ✅ saveResponse(): should execute batch insert for all responses */
//    @Test
//    void saveResponse_SuccessfulBatchInsert() throws Exception {
//        Connection conn = mock(Connection.class);
//        PreparedStatement stmt = mock(PreparedStatement.class);
//
//        // Mock connection + statement behavior
//        when(conn.prepareStatement(anyString())).thenReturn(stmt);
//        when(stmt.executeBatch()).thenReturn(new int[]{1, 1});
//
//        // Prepare mock responses
//        QuestionResponse r1 = new QuestionResponse();
//        r1.setQuestion_id(2);
//        r1.setOption_id(3);
//        r1.setIs_correct(true);
//
//        QuestionResponse r2 = new QuestionResponse();
//        r2.setQuestion_id(4);
//        r2.setOption_id(6);
//        r2.setIs_correct(false);
//
//        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
//            mocked.when(DBconnection::getConnection).thenReturn(conn);
//
//            JdbcResponseDao dao = new JdbcResponseDao();
//            dao.saveResponse(10, List.of(r1, r2));
//
//            // ✅ Each response triggers:
//            // 3 x setInt + 1 x setBoolean + 1 x addBatch
//            verify(stmt, times(6)).setInt(anyInt(), anyInt());
//            verify(stmt, times(2)).setBoolean(anyInt(), anyBoolean());
//            verify(stmt, times(2)).addBatch();
//
//            // ✅ executeBatch called once overall
//            verify(stmt, times(1)).executeBatch();
//        }
//    }
//
//
//    /** ❌ saveResponse(): should handle empty response list gracefully */
//    @Test
//    void saveResponse_EmptyListDoesNothing() throws Exception {
//        Connection conn = mock(Connection.class);
//        PreparedStatement stmt = mock(PreparedStatement.class);
//        when(conn.prepareStatement(anyString())).thenReturn(stmt);
//
//        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
//            mocked.when(DBconnection::getConnection).thenReturn(conn);
//
//            JdbcResponseDao dao = new JdbcResponseDao();
//            dao.saveResponse(99, List.of()); // Empty list
//
//            verify(stmt, never()).addBatch();
//            verify(stmt, never()).executeBatch();
//        }
//    }
//
//    /** 💥 saveResponse(): should handle SQLException and not crash */
//    @Test
//    void saveResponse_SQLExceptionHandled() throws Exception {
//        when(DBconnection.getConnection()).thenThrow(new SQLException("DB down"));
//
//        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
//            mocked.when(DBconnection::getConnection).thenThrow(new SQLException("DB down"));
//
//            JdbcResponseDao dao = new JdbcResponseDao();
//            assertDoesNotThrow(() -> dao.saveResponse(1, List.of()));
//        }
//    }
//
//    // --- calculateScoreFromResponses() -------------------------------------
//
//    /** ✅ calculateScoreFromResponses(): should return score when record exists */
//    @Test
//    void calculateScoreFromResponses_Success() throws Exception {
//        Connection conn = mock(Connection.class);
//        PreparedStatement stmt = mock(PreparedStatement.class);
//        ResultSet rs = mock(ResultSet.class);
//
//        when(conn.prepareStatement(anyString())).thenReturn(stmt);
//        when(stmt.executeQuery()).thenReturn(rs);
//        when(rs.next()).thenReturn(true);
//        when(rs.getInt("score")).thenReturn(5);
//
//        int result;
//        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
//            mocked.when(DBconnection::getConnection).thenReturn(conn);
//            JdbcResponseDao dao = new JdbcResponseDao();
//            result = dao.calculateScoreFromResponses(42);
//        }
//
//        assertEquals(5, result);
//        verify(stmt).setInt(1, 42);
//    }
//
//    /** ❌ calculateScoreFromResponses(): should return -1 when no rows found */
//    @Test
//    void calculateScoreFromResponses_NoRows() throws Exception {
//        Connection conn = mock(Connection.class);
//        PreparedStatement stmt = mock(PreparedStatement.class);
//        ResultSet rs = mock(ResultSet.class);
//
//        when(conn.prepareStatement(anyString())).thenReturn(stmt);
//        when(stmt.executeQuery()).thenReturn(rs);
//        when(rs.next()).thenReturn(false);
//
//        int result;
//        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
//            mocked.when(DBconnection::getConnection).thenReturn(conn);
//            JdbcResponseDao dao = new JdbcResponseDao();
//            result = dao.calculateScoreFromResponses(77);
//        }
//
//        assertEquals(-1, result);
//    }
//
//    /** 💥 calculateScoreFromResponses(): should handle SQLException gracefully */
//    @Test
//    void calculateScoreFromResponses_SQLExceptionHandled() throws Exception {
//        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
//            // ✅ Correctly mock static method call
//            mocked.when(DBconnection::getConnection)
//                    .thenThrow(new SQLException("DB failure"));
//
//            JdbcResponseDao dao = new JdbcResponseDao();
//            int result = dao.calculateScoreFromResponses(55);
//
//            assertEquals(-1, result);
//        }
//    }
//
//
//    // --- getChosenOptionId() -----------------------------------------------
//
//    /** ✅ getChosenOptionId(): should return option ID when record exists */
//    @Test
//    void getChosenOptionId_Success() throws Exception {
//        Connection conn = mock(Connection.class);
//        PreparedStatement stmt = mock(PreparedStatement.class);
//        ResultSet rs = mock(ResultSet.class);
//
//        when(conn.prepareStatement(anyString())).thenReturn(stmt);
//        when(stmt.executeQuery()).thenReturn(rs);
//        when(rs.next()).thenReturn(true);
//        when(rs.getInt("option_id")).thenReturn(123);
//
//        int result;
//        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
//            mocked.when(DBconnection::getConnection).thenReturn(conn);
//            JdbcResponseDao dao = new JdbcResponseDao();
//            result = dao.getChosenOptionId(10, 20);
//        }
//
//        assertEquals(123, result);
//        verify(stmt).setInt(1, 10);
//        verify(stmt).setInt(2, 20);
//    }
//
//    /** ❌ getChosenOptionId(): should return -1 when no record found */
//    @Test
//    void getChosenOptionId_NoResult() throws Exception {
//        Connection conn = mock(Connection.class);
//        PreparedStatement stmt = mock(PreparedStatement.class);
//        ResultSet rs = mock(ResultSet.class);
//
//        when(conn.prepareStatement(anyString())).thenReturn(stmt);
//        when(stmt.executeQuery()).thenReturn(rs);
//        when(rs.next()).thenReturn(false);
//
//        int result;
//        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
//            mocked.when(DBconnection::getConnection).thenReturn(conn);
//            JdbcResponseDao dao = new JdbcResponseDao();
//            result = dao.getChosenOptionId(11, 22);
//        }
//
//        assertEquals(-1, result);
//    }
//
//    /** 💥 getChosenOptionId(): should handle SQLException gracefully */
//    @Test
//    void getChosenOptionId_SQLExceptionHandled() throws Exception {
//
//        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
//            mocked.when(DBconnection::getConnection).thenThrow(new SQLException("DB failure"));
//            JdbcResponseDao dao = new JdbcResponseDao();
//            int result = dao.getChosenOptionId(5, 9);
//            assertEquals(-1, result);
//        }
//    }
//}

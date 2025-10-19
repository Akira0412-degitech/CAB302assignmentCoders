//package com.example.cab302a1.dao;
//
//import com.example.cab302a1.DBconnection;
//import com.example.cab302a1.dao.jdbc.JdbcReviewDao;
//import com.example.cab302a1.model.QuizReview;
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
// * Unit tests for {@link JdbcReviewDao}.
// *
// * These tests ensure correct SQL behavior, dependency interactions,
// * and object mapping for QuizReview objects.
// */
//public class ReviewDaoTest {
//
//    /** ✅ Should return multiple QuizReview records correctly mapped */
//    @Test
//    void getAllAttemptsById_ReturnsList() throws Exception {
//        QuestionDao mockQuestionDao = mock(QuestionDao.class);
//        JdbcReviewDao dao = new JdbcReviewDao(mockQuestionDao);
//
//        Connection conn = mock(Connection.class);
//        PreparedStatement stmt = mock(PreparedStatement.class);
//        ResultSet rs = mock(ResultSet.class);
//
//        // Simulate 2 rows returned
//        when(rs.next()).thenReturn(true, true, false);
//        when(rs.getInt("attempt_id")).thenReturn(1, 2);
//        when(rs.getInt("quiz_id")).thenReturn(10, 20);
//        when(rs.getInt("score")).thenReturn(85, 90);
//        when(rs.getString("title")).thenReturn("Math", "Science");
//        when(rs.getString("feedback")).thenReturn("Good", "Excellent");
//
//        // QuestionDao returns number of questions
//        when(mockQuestionDao.getNumQuestion(anyInt())).thenReturn(5);
//
//        when(conn.prepareStatement(anyString())).thenReturn(stmt);
//        when(stmt.executeQuery()).thenReturn(rs);
//
//        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
//            mocked.when(DBconnection::getConnection).thenReturn(conn);
//
//            List<QuizReview> result = dao.getAllAttemptsById(42);
//
//            // ✅ Assertions
//            assertEquals(2, result.size());
//            assertEquals("Math", result.get(0).getQuizName());
//            assertEquals(5, result.get(0).getTotal());
//            assertEquals("Science", result.get(1).getQuizName());
//            assertEquals("Excellent", result.get(1).getFeedback());
//            assertEquals("90/5", result.get(1).getScoreSummary());
//
//            verify(mockQuestionDao, times(2)).getNumQuestion(anyInt());
//            verify(stmt, times(1)).setInt(1, 42);
//        }
//    }
//
//    /** ❌ Should return empty list when no quiz attempts found */
//    @Test
//    void getAllAttemptsById_NoResults() throws Exception {
//        QuestionDao mockQuestionDao = mock(QuestionDao.class);
//        JdbcReviewDao dao = new JdbcReviewDao(mockQuestionDao);
//
//        Connection conn = mock(Connection.class);
//        PreparedStatement stmt = mock(PreparedStatement.class);
//        ResultSet rs = mock(ResultSet.class);
//
//        when(rs.next()).thenReturn(false);
//        when(conn.prepareStatement(anyString())).thenReturn(stmt);
//        when(stmt.executeQuery()).thenReturn(rs);
//
//        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
//            mocked.when(DBconnection::getConnection).thenReturn(conn);
//
//            List<QuizReview> result = dao.getAllAttemptsById(100);
//            assertNotNull(result);
//            assertTrue(result.isEmpty(), "Expected empty list when no records found");
//            verify(mockQuestionDao, never()).getNumQuestion(anyInt());
//        }
//    }
//
//    /** 💥 Should handle SQLException gracefully and return empty list */
//    @Test
//    void getAllAttemptsById_SQLExceptionHandled() throws Exception {
//        QuestionDao mockQuestionDao = mock(QuestionDao.class);
//        JdbcReviewDao dao = new JdbcReviewDao(mockQuestionDao);
//
//        Connection conn = mock(Connection.class);
//        when(conn.prepareStatement(anyString())).thenThrow(new SQLException("Database down"));
//
//        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
//            mocked.when(DBconnection::getConnection).thenReturn(conn);
//
//            List<QuizReview> result = dao.getAllAttemptsById(55);
//            assertNotNull(result);
//            assertTrue(result.isEmpty(), "Expected empty list when SQLException occurs");
//        }
//    }
//
//    /** ⚙️ Should call QuestionDao.getNumQuestion() for every quiz returned */
//    @Test
//    void getAllAttemptsById_CallsQuestionDaoForEachQuiz() throws Exception {
//        QuestionDao mockQuestionDao = mock(QuestionDao.class);
//        JdbcReviewDao dao = new JdbcReviewDao(mockQuestionDao);
//
//        Connection conn = mock(Connection.class);
//        PreparedStatement stmt = mock(PreparedStatement.class);
//        ResultSet rs = mock(ResultSet.class);
//
//        // 3 attempts found
//        when(rs.next()).thenReturn(true, true, true, false);
//        when(rs.getInt("attempt_id")).thenReturn(1, 2, 3);
//        when(rs.getInt("quiz_id")).thenReturn(10, 11, 12);
//        when(rs.getInt("score")).thenReturn(50, 60, 70);
//        when(rs.getString("title")).thenReturn("A", "B", "C");
//        when(rs.getString("feedback")).thenReturn("ok", "good", "nice");
//
//        when(mockQuestionDao.getNumQuestion(anyInt())).thenReturn(4);
//
//        when(conn.prepareStatement(anyString())).thenReturn(stmt);
//        when(stmt.executeQuery()).thenReturn(rs);
//
//        try (MockedStatic<DBconnection> mocked = mockStatic(DBconnection.class)) {
//            mocked.when(DBconnection::getConnection).thenReturn(conn);
//
//            dao.getAllAttemptsById(88);
//            verify(mockQuestionDao, times(3)).getNumQuestion(anyInt());
//        }
//    }
//
//
//}

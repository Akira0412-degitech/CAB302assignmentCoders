# Quiz Result Page Integration Guide

## Overview

The Quiz Result page is a central component in the Interactive Quiz Creator application that displays quiz scores and provides navigation options after quiz completion. This guide provides comprehensive instructions for team members to integrate their components with the quiz result system.

**🆕 NEW: Database Integration** - The quiz result system now supports direct integration with your database using `AttemptDao.getScore()` method. You can fetch real quiz scores from the `quiz_attempts` table using `quiz_id` and `user_id` parameters.

## Quick Start for Team Members

### 🆕 1. Database Integration (Recommended - NEW)

**Use AttemptDao.getScore() to fetch real quiz results from database:**

```java
// From your quiz completion handler - DATABASE METHOD
try {
    // Using current logged-in user
    QuizResultController.showQuizResultFromDatabaseForCurrentUser(currentStage, quizId);
    
    // OR using specific user ID
    QuizResultController.showQuizResultFromDatabase(currentStage, quizId, userId);
    
} catch (IOException | QuizResultService.QuizResultException e) {
    System.err.println("Error showing quiz results from database: " + e.getMessage());
}
```

### 2. Manual Data Integration (Legacy)

If you need to provide your own score data:

```java
// From your quiz completion handler - MANUAL METHOD
try {
    QuizResultData resultData = new QuizResultData(correctAnswers, totalQuestions, "Your Quiz Title", quizId, userId);
    QuizResultController.showQuizResult(currentStage, resultData);
} catch (IOException e) {
    System.err.println("Error showing quiz results: " + e.getMessage());
}
```

### 3. Quick Testing

To quickly test the quiz result page:

```java
// Run the standalone demo (now with database integration)
QuizResultDemo.main(new String[]{});
```

The demo will:
1. ✅ Try to load real data from database using `AttemptDao.getScore()`
2. 🔄 Fall back to sample data (13/20) if no database data found
3. 📊 Show which method was used in console output

## File Structure

```
src/main/java/com/example/cab302a1/result/
├── QuizResultController.java     # Main controller with integration points
├── QuizResultData.java          # Data model for quiz results
├── QuizResultService.java       # 🆕 Database integration service
└── QuizResultDemo.java          # Standalone test runner with database demo

src/main/resources/com/example/cab302a1/result/
├── QuizResult.fxml              # UI layout with navbar integration
└── QuizResult.css               # Design system compliant styling
```

## Data Structures

### QuizResultData Class

The primary data structure for passing quiz information:

```java
public class QuizResultData {
    // Constructor options
    QuizResultData(int correctAnswers, int totalQuestions, String quizTitle, int quizId, int userId)
    QuizResultData(int correctAnswers, int totalQuestions) // Simplified version
    
    // Key methods
    String getScoreString()          // Returns "X/Y" format
    double getScorePercentage()      // Returns 0.0-100.0
    String getLetterGrade()          // Returns A, B, C, D, or F
    boolean isPassing()              // Returns true if >= 60%
    boolean isValid()               // Validates the data
    
    // Utility methods
    static QuizResultData[] getSampleData()  // For testing
    String toJsonString()                    // For debugging
}
```

### Sample Usage

```java
// Create result data
QuizResultData result = new QuizResultData(17, 20, "Mathematics Quiz", 123, 456);

// Access computed properties
System.out.println("Score: " + result.getScoreString());        // "17/20"
System.out.println("Percentage: " + result.getScorePercentage()); // 85.0
System.out.println("Grade: " + result.getLetterGrade());          // "B"
System.out.println("Passing: " + result.isPassing());             // true
```

## 🆕 Database Integration with AttemptDao.getScore()

### Overview

The quiz result system now integrates directly with your database team member's implementation. It uses:
- **`AttemptDao.getScore(quiz_id, user_id)`** - Returns the score from `quiz_attempts` table
- **`Quiz` objects** - Fetched from `QuizDao.getAllQuizzes()`
- **`User` objects** - Fetched from `UserDao.getUserById()`
- **`Session.getCurrentUser()`** - Gets the currently logged-in user

### QuizResultService Class

The new `QuizResultService` class handles all database operations:

```java
// Create service instance
QuizResultService service = new QuizResultService();

// Get quiz result from database
QuizResultData result = service.getQuizResult(quizId, userId);

// Or for current user
QuizResultData result = service.getQuizResultForCurrentUser(quizId);

// Check if result exists
boolean hasResult = service.hasQuizResult(quizId, userId);
```

### Database Requirements

Your database should have these tables (already implemented by your team):

```sql
-- quiz_attempts table (contains the scores)
quiz_attempts (
    attempt_id INT PRIMARY KEY AUTO_INCREMENT,
    quiz_id INT NOT NULL,
    answered_by INT NOT NULL,  -- user_id
    score INT,                 -- this is what AttemptDao.getScore() returns
    is_completed BOOLEAN NOT NULL DEFAULT FALSE
)

-- quizzes table (contains quiz information)
quizzes (
    quiz_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    created_by INT NOT NULL
)

-- users table (contains user information)
users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
)
```

### Integration Examples

#### Example 1: Basic Database Integration

```java
public class YourQuizController {
    
    public void handleQuizCompletion(int quizId) {
        try {
            // Using current logged-in user (recommended)
            Stage currentStage = (Stage) yourButton.getScene().getWindow();
            QuizResultController.showQuizResultFromDatabaseForCurrentUser(currentStage, quizId);
            
        } catch (IOException | QuizResultService.QuizResultException e) {
            System.err.println("Error showing quiz results: " + e.getMessage());
            // Show error message to user or fallback behavior
        }
    }
}
```

#### Example 2: Specific User Integration

```java
public class TeacherReviewController {
    
    public void viewStudentResult(int quizId, int studentId) {
        try {
            // View specific student's result
            Stage currentStage = (Stage) reviewButton.getScene().getWindow();
            QuizResultController.showQuizResultFromDatabase(currentStage, quizId, studentId);
            
        } catch (IOException | QuizResultService.QuizResultException e) {
            handleError("Cannot load student result", e);
        }
    }
}
```

#### Example 3: Advanced Integration with Error Handling

```java
public class ComprehensiveIntegration {
    
    public void showQuizResultWithFallback(Stage stage, int quizId, int userId) {
        QuizResultService service = new QuizResultService();
        
        // Check if result exists first
        if (!service.hasQuizResult(quizId, userId)) {
            showMessage("No quiz attempt found for this user");
            return;
        }
        
        try {
            // Try database integration
            QuizResultController.showQuizResultFromDatabase(stage, quizId, userId);
            
        } catch (QuizResultService.QuizResultException e) {
            // Database error - show error message
            showError("Database error: " + e.getMessage());
            
        } catch (IOException e) {
            // UI loading error - show error message  
            showError("UI error: " + e.getMessage());
        }
    }
}
```

### Factory Methods for Quick Usage

```java
// Quick factory methods for common scenarios
try {
    // Get result data for processing
    QuizResultData data = QuizResultService.createFromDatabase(quizId, userId);
    
    // Or for current user
    QuizResultData data = QuizResultService.createForCurrentUser(quizId);
    
    // Process the data as needed
    double percentage = data.getScorePercentage();
    String grade = data.getLetterGrade();
    
} catch (QuizResultService.QuizResultException e) {
    // Handle database errors
}
```

## Integration Points

### 1. Incoming Navigation (TO Quiz Result Page)

#### 🆕 From Quiz Completion Logic (Database Method - Recommended)

```java
public class YourQuizController {
    
    private void handleQuizCompletion(int quizId) {
        try {
            // DATABASE METHOD - Uses AttemptDao.getScore() automatically
            Stage currentStage = (Stage) yourButton.getScene().getWindow();
            QuizResultController.showQuizResultFromDatabaseForCurrentUser(currentStage, quizId);
            
            System.out.println("✅ Quiz result displayed using database integration");
            
        } catch (IOException | QuizResultService.QuizResultException e) {
            handleNavigationError(e);
        }
    }
    
    // Alternative: if you need to specify a different user
    private void handleQuizCompletionForUser(int quizId, int userId) {
        try {
            Stage currentStage = (Stage) yourButton.getScene().getWindow();
            QuizResultController.showQuizResultFromDatabase(currentStage, quizId, userId);
            
        } catch (IOException | QuizResultService.QuizResultException e) {
            handleNavigationError(e);
        }
    }
}
```

#### Legacy Method (Manual Data)

```java
public class YourQuizController {
    
    private void handleQuizCompletion(int correct, int total) {
        try {
            // MANUAL METHOD - When you calculate scores yourself
            QuizResultData resultData = new QuizResultData(
                correct, 
                total, 
                currentQuiz.getTitle(), 
                currentQuiz.getId(), 
                currentUser.getId()
            );
            
            // Navigate to result page
            Stage currentStage = (Stage) yourButton.getScene().getWindow();
            QuizResultController.showQuizResult(currentStage, resultData);
            
        } catch (IOException e) {
            handleNavigationError(e);
        }
    }
}
```

#### Alternative Approach (Load Controller Manually)

```java
public void showCustomQuizResult(Stage stage, QuizResultData data) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cab302a1/result/QuizResult.fxml"));
    Scene scene = new Scene(loader.load(), 1200, 700);
    
    // Get controller and set data
    QuizResultController controller = loader.getController();
    controller.setQuizResult(data);
    
    // Set custom handlers (optional)
    controller.setAnswerReviewHandler(this::handleAnswerReview);
    controller.setHomeNavigationHandler(this::handleHomeNavigation);
    
    stage.setScene(scene);
    stage.show();
}
```

### 2. Outgoing Navigation (FROM Quiz Result Page)

#### Answer Review Integration

Connect your answer review component:

```java
public class AnswerReviewIntegration {
    
    public void setupAnswerReviewIntegration(QuizResultController controller) {
        controller.setAnswerReviewHandler((stage, quizData) -> {
            try {
                // Your answer review navigation logic
                showAnswerReviewPage(stage, quizData.getQuizId(), quizData.getUserId());
                System.out.println("Showing answers for: " + quizData.getQuizTitle());
            } catch (Exception e) {
                System.err.println("Error showing answer review: " + e.getMessage());
            }
        });
    }
    
    private void showAnswerReviewPage(Stage stage, int quizId, int userId) {
        // Your implementation here
        // Load your answer review FXML
        // Pass quiz and user data
        // Navigate to your answer review scene
    }
}
```

#### Home Navigation Integration

Customize the home navigation:

```java
public class HomeNavigationIntegration {
    
    public void setupHomeNavigation(QuizResultController controller) {
        controller.setHomeNavigationHandler((stage) -> {
            try {
                // Your custom home navigation
                navigateToCustomHome(stage);
            } catch (Exception e) {
                System.err.println("Error navigating home: " + e.getMessage());
            }
        });
    }
    
    private void navigateToCustomHome(Stage stage) throws IOException {
        // Your implementation:
        // - Load your home page FXML
        // - Clear any temporary quiz data
        // - Update user session if needed
        // - Navigate to your home scene
    }
}
```

## Testing and Development

### 1. Standalone Testing

Use the provided demo class for isolated testing:

```bash
# Run the demo application
java com.example.cab302a1.result.QuizResultDemo

# Or from your IDE, run QuizResultDemo.main()
```

The demo provides multiple test scenarios:
- Perfect Score (20/20)
- Good Score (13/20) 
- Low Score (1/2)
- Failing Score (8/20)
- Random Score

### 2. Integration Testing

Test your integration with various scenarios:

```java
public class IntegrationTest {
    
    @Test
    public void testQuizResultIntegration() {
        // Test data
        QuizResultData[] testCases = QuizResultData.getSampleData();
        
        for (QuizResultData testData : testCases) {
            // Test navigation
            try {
                QuizResultController.showQuizResult(testStage, testData);
                // Verify UI elements
                // Test button functionality
            } catch (IOException e) {
                fail("Navigation failed for: " + testData.toString());
            }
        }
    }
}
```

### 3. Mock Data for Development

Use sample data during development:

```java
// Quick test data
QuizResultData quickTest = new QuizResultData(15, 20);

// Comprehensive test data
QuizResultData[] samples = QuizResultData.getSampleData();

// Custom scenarios
QuizResultData perfectScore = new QuizResultData(50, 50, "Final Exam", 999, 123);
QuizResultData failingScore = new QuizResultData(5, 20, "Pop Quiz", 888, 123);
```

## Database Integration Preparation

### Future Database Schema

When implementing database storage, consider this structure:

_(Dude plssss change your table name from "quiz_attempts" to this, plssss)_

```sql
CREATE TABLE quiz_results (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    quiz_id INT NOT NULL,
    correct_answers INT NOT NULL,
    total_questions INT NOT NULL,
    score_percentage DECIMAL(5,2) NOT NULL,
    completion_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);
```

### Database Service Interface

Prepare for database integration with this interface:

```java
public interface QuizResultService {
    // Save quiz result
    void saveQuizResult(QuizResultData resultData);
    
    // Retrieve results
    List<QuizResultData> getUserResults(int userId);
    List<QuizResultData> getQuizResults(int quizId);
    QuizResultData getResult(int resultId);
    
    // Statistics
    double getAverageScore(int quizId);
    int getTotalAttempts(int quizId);
    List<QuizResultData> getTopScores(int quizId, int limit);
}
```

### Integration with Database

```java
public class DatabaseIntegratedController extends QuizResultController {
    
    private QuizResultService quizResultService;
    
    @Override
    public void setQuizResult(QuizResultData quizResultData) {
        super.setQuizResult(quizResultData);
        
        // Save to database
        if (quizResultService != null) {
            try {
                quizResultService.saveQuizResult(quizResultData);
                System.out.println("Quiz result saved to database");
            } catch (Exception e) {
                System.err.println("Error saving to database: " + e.getMessage());
            }
        }
    }
}
```

## Common Integration Patterns

### 1. Simple Quiz to Result Flow

```java
public class SimpleQuizFlow {
    
    public void completeQuiz(List<Answer> answers, Quiz quiz, User user) {
        // Calculate score
        int correct = calculateCorrectAnswers(answers, quiz);
        int total = quiz.getQuestions().size();
        
        // Create result data
        QuizResultData result = new QuizResultData(
            correct, total, quiz.getTitle(), quiz.getId(), user.getId()
        );
        
        // Show results
        Stage stage = getCurrentStage();
        try {
            QuizResultController.showQuizResult(stage, result);
        } catch (IOException e) {
            showErrorMessage("Cannot display results", e.getMessage());
        }
    }
}
```

### 2. Advanced Flow with Custom Handlers

```java
public class AdvancedQuizFlow {
    
    public void setupCompleteFlow(Stage stage) throws IOException {
        // Load quiz result page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cab302a1/result/QuizResult.fxml"));
        Scene scene = new Scene(loader.load());
        
        QuizResultController controller = loader.getController();
        
        // Set up integrations
        controller.setAnswerReviewHandler(this::showDetailedReview);
        controller.setHomeNavigationHandler(this::navigateToStudentDashboard);
        
        // Apply scene
        stage.setScene(scene);
    }
    
    private void showDetailedReview(Stage stage, QuizResultData data) {
        // Your detailed answer review implementation
        DetailedReviewController.show(stage, data.getQuizId(), data.getUserId());
    }
    
    private void navigateToStudentDashboard(Stage stage) {
        // Your student dashboard navigation
        StudentDashboard.show(stage);
    }
}
```

## Error Handling

### Common Error Scenarios

```java
public class ErrorHandling {
    
    public void handleQuizResultErrors(QuizResultData data, Stage stage) {
        // Validate data
        if (data == null || !data.isValid()) {
            showError("Invalid quiz result data");
            return;
        }
        
        // Handle navigation errors
        try {
            QuizResultController.showQuizResult(stage, data);
        } catch (IOException e) {
            showError("Cannot load result page: " + e.getMessage());
            // Fallback to simple message or alternative page
        } catch (Exception e) {
            showError("Unexpected error: " + e.getMessage());
            // Log error for debugging
            e.printStackTrace();
        }
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Quiz Results Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
```

## Best Practices

### 1. Data Validation

Always validate quiz result data:

```java
QuizResultData result = new QuizResultData(correct, total, title, quizId, userId);
if (!result.isValid()) {
    throw new IllegalArgumentException("Invalid quiz result data");
}
```

### 2. Error Handling

Wrap navigation calls in try-catch blocks:

```java
try {
    QuizResultController.showQuizResult(stage, resultData);
} catch (IOException e) {
    // Handle gracefully - show error message, log, or fallback
    handleNavigationError(e);
}
```

### 3. Resource Management

Ensure proper resource cleanup:

```java
@Override
public void stop() throws Exception {
    // Clean up any resources
    if (quizResultService != null) {
        quizResultService.cleanup();
    }
    super.stop();
}
```

### 4. Accessibility

Consider accessibility in your integrations:

```java
// Set accessible descriptions
button.setAccessibleText("View detailed answers for " + quiz.getTitle());
```

## Troubleshooting

### 🆕 Database Integration Issues

1. **QuizResultService.QuizResultException: No quiz attempt found**
   - **Cause**: No record in `quiz_attempts` table for the given `quiz_id` and `user_id`
   - **Solution**: Ensure the quiz attempt was saved to database before calling result page
   ```java
   // Check if attempt exists first
   QuizResultService service = new QuizResultService();
   if (!service.hasQuizResult(quizId, userId)) {
       System.out.println("No attempt found - user hasn't taken this quiz yet");
       return;
   }
   ```

2. **Quiz not found with id: X**
   - **Cause**: Quiz doesn't exist in `quizzes` table
   - **Solution**: Verify quiz exists and `quiz_id` is correct
   ```java
   QuizDao quizDao = new QuizDao();
   List<Quiz> allQuizzes = quizDao.getAllQuizzes();
   // Check if your quiz_id exists in the list
   ```

3. **User not found with id: X**
   - **Cause**: User doesn't exist in `users` table
   - **Solution**: Verify user exists and `user_id` is correct

4. **No user is currently logged in**
   - **Cause**: `Session.getCurrentUser()` returns null
   - **Solution**: Ensure user is logged in before calling current user methods
   ```java
   if (!Session.isLoggedaIn()) {
       System.out.println("User must be logged in first");
       // Navigate to login page
       return;
   }
   ```

5. **Database connection errors**
   - **Cause**: Database connection issues in DAOs
   - **Solution**: Check `DBconnection.getConnection()` and database configuration

### Common Issues

1. **CSS not loading**: Ensure the CSS file is in the correct resource path
2. **FXML not found**: Check the resource path in your FXMLLoader
3. **Module access errors**: Verify module-info.java exports the result package
4. **Navigation fails**: Check that handlers are properly set before navigation

### Debug Mode

Enable debug output:

```java
System.setProperty("javafx.verbose", "true");
// Your navigation code here
```

### Logging

Add comprehensive logging:

```java
private static final Logger logger = LoggerFactory.getLogger(YourClass.class);

logger.info("Navigating to quiz result for quiz: {}", quizId);
logger.debug("Quiz result data: {}", resultData.toJsonString());
```

## Support and Contribution

- For integration questions, consult the team lead
- Report bugs through the project's issue tracking system
- Contribute improvements via pull requests
- Document any custom integration patterns you develop

## 🆕 Database Integration Summary

### What's New
- ✅ **Direct database integration** using `AttemptDao.getScore(quiz_id, user_id)`
- ✅ **Automatic data fetching** from `quiz_attempts`, `quizzes`, and `users` tables  
- ✅ **Session integration** with `Session.getCurrentUser()`
- ✅ **Error handling** for missing data and database issues
- ✅ **Fallback support** to manual data if needed

### Key Benefits
- 🎯 **Zero score calculation** needed - scores come directly from database
- 🔄 **Automatic data synchronization** with your quiz attempts
- 👤 **User session integration** - works with current logged-in user
- 📊 **Real-time data** - always shows latest scores from database
- 🛡️ **Robust error handling** - graceful fallbacks for missing data

### Migration Guide
If you're currently using manual QuizResultData creation:

```java
// OLD WAY (still supported)
QuizResultData data = new QuizResultData(score, total, title, quizId, userId);
QuizResultController.showQuizResult(stage, data);

// NEW WAY (recommended)
QuizResultController.showQuizResultFromDatabaseForCurrentUser(stage, quizId);
```

### Team Integration Checklist
- [ ] Replace manual score calculations with database calls
- [ ] Use `showQuizResultFromDatabaseForCurrentUser()` for student completion
- [ ] Use `showQuizResultFromDatabase()` for teacher reviewing student results
- [ ] Add error handling for `QuizResultService.QuizResultException`
- [ ] Test with actual database data
- [ ] Verify quiz attempts are saved to database before showing results

## Version Information

- **Current Version**: 2.0 (Database Integration)
- **Compatible JavaFX Version**: 21+
- **Required Modules**: javafx.controls, javafx.fxml, java.sql
- **Database Dependencies**: AttemptDao, QuizDao, UserDao, Session utility
- **Last Updated**: December 2024

---

This integration guide provides comprehensive information for connecting your components with the Quiz Result page, including the new database integration features. For additional help or clarification, please reach out to the development team.

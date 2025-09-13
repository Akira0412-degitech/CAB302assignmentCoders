# Quiz Result Page Integration Guide

## Overview

The Quiz Result page is a central component in the Interactive Quiz Creator application that displays quiz scores and provides navigation options after quiz completion. This guide provides comprehensive instructions for team members to integrate their components with the quiz result system.

## Quick Start for Team Members

### 1. Basic Integration (Show Quiz Results)

The simplest way to navigate to the quiz result page from your quiz completion logic:

```java
// From your quiz completion handler
try {
    QuizResultData resultData = new QuizResultData(correctAnswers, totalQuestions, "Your Quiz Title", quizId, userId);
    QuizResultController.showQuizResult(currentStage, resultData);
} catch (IOException e) {
    System.err.println("Error showing quiz results: " + e.getMessage());
}
```

### 2. Quick Testing

To quickly test the quiz result page:

```java
// Run the standalone demo
QuizResultDemo.main(new String[]{});

// Or programmatically with custom data
QuizResultDemo.runQuickDemo(15, 20, "My Test Quiz");
```

## File Structure

```
src/main/java/com/example/cab302a1/result/
├── QuizResultController.java     # Main controller with integration points
├── QuizResultData.java          # Data model for quiz results
└── QuizResultDemo.java          # Standalone test runner

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

## Integration Points

### 1. Incoming Navigation (TO Quiz Result Page)

#### From Quiz Completion Logic

```java
public class YourQuizController {
    
    private void handleQuizCompletion(int correct, int total) {
        try {
            // Create result data
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

## Version Information

- **Current Version**: 1.0
- **Compatible JavaFX Version**: 21+
- **Required Modules**: javafx.controls, javafx.fxml
- **Last Updated**: [Current Date]

---

This integration guide provides comprehensive information for connecting your components with the Quiz Result page. For additional help or clarification, please reach out to the development team.

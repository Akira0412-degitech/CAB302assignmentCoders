# Student Quiz Result Detail Implementation

## Overview
This document describes the implementation of the quiz result detail feature for students, including:
- Viewing detailed answers after completing a quiz
- Preventing quiz retakes
- Visual indication of completed quizzes
- Quiz history tracking

## Features Implemented

### 1. Quiz Result Detail Page
**Location:** `src/main/java/com/example/cab302a1/ui/StudentResultDetailController.java`

A new page that shows:
- All quiz questions with their options
- The answer the student selected (highlighted in red if wrong)
- The correct answer (highlighted in green)
- Explanations provided by the teacher (if available)
- A "Done" button to return to the previous page

**FXML:** `src/main/resources/com/example/cab302a1/result/StudentResultDetailPage.fxml`
**CSS:** `src/main/resources/com/example/cab302a1/result/StudentResultDetail.css`

#### Color Coding
- **Green (#B2F2BB)**: Correct answer
- **Red (#FFE0E0)**: Wrong answer selected by student
- **Gray (#F1F3F5)**: Options not selected
- **Yellow (#FFF9DB)**: Explanation box background

### 2. Result Question Item Component
**Location:** `src/main/java/com/example/cab302a1/ui/ResultQuestionItemController.java`

A reusable component for displaying individual questions in the result detail page:
- Shows question text (read-only)
- Displays all answer options (disabled toggle buttons)
- Highlights correct and incorrect answers
- Shows explanation if provided by teacher

**FXML:** `src/main/resources/com/example/cab302a1/result/ResultQuestionItem.fxml`

### 3. Enhanced AttemptDao
**Location:** `src/main/java/com/example/cab302a1/dao/AttemptDao.java`

Added new method:
```java
public Integer getAttemptId(int _quiz_id, int _user_id)
```
Returns the attempt ID for a user's most recent completed quiz attempt.

### 4. Quiz Result Controller Integration
**Location:** `src/main/java/com/example/cab302a1/result/QuizResultController.java`

Updated the "View the answer" button handler to:
- Retrieve the attempt ID for the quiz
- Open the `StudentResultDetailController` with detailed results
- Show all questions with correct/incorrect answers highlighted

### 5. Home Page Enhancements
**Location:** `src/main/java/com/example/cab302a1/ui/HomeController.java`

#### Visual Indication
Completed quizzes now show with a green background to distinguish them from available quizzes:
- **Available Quiz**: Light blue (#A5D8FF) background
- **Completed Quiz**: Light green (#B2F2BB) background with green border

#### Behavior Changes
- **First-time quiz**: Opens the quiz-taking interface
- **Completed quiz**: Opens the result detail page directly (prevents retaking)
- Students cannot retake a quiz once completed

**CSS Changes:** `src/main/resources/com/example/cab302a1/HomePage.css`

Added styles:
```css
.quiz-card-completed {
    -fx-background-color: #B2F2BB; /* Light green for completed */
    -fx-border-color: #37B24D; /* Dark green border */
}
```

## User Flow

### Taking a Quiz (First Time)
1. Student logs in and sees the home page
2. Available quizzes display with light blue background
3. Student clicks on a quiz
4. Quiz-taking interface opens
5. Student answers questions and clicks "Done"
6. Quiz result summary page shows (score, percentage, grade)
7. Student can click "View the answer" to see detailed results
8. Detailed results show all questions with correct/incorrect answers highlighted
9. Student clicks "Done" to return

### Viewing Completed Quiz
1. Student logs in and sees the home page
2. Completed quizzes display with light green background
3. Student clicks on a completed quiz
4. Detailed results page opens directly (no retake allowed)
5. Student can review all answers and explanations
6. Student clicks "Done" to return to home page

## Database Integration

The implementation uses existing DAOs:
- **AttemptDao**: Track quiz attempts and completion status
- **QuizDao**: Retrieve quiz information
- **QuestionDao**: Get quiz questions
- **OptionDao**: Get answer options for each question
- **ResponseDao**: Get student's selected answers
- **QuizResultDetailService**: Combines all data for display

### Tables Used
- `quiz_attempts`: Stores quiz attempt records with completion status
- `question_responses`: Stores student's answers for each question
- `quizzes`: Quiz information
- `questions`: Quiz questions with explanations
- `options`: Answer options with correct/incorrect flags

## Key Classes and Methods

### StudentResultDetailController
```java
public static void open(Stage owner, int quizId, int attemptId, Runnable onDone)
```
Opens the result detail page as a modal dialog.

### ResultQuestionItemController
```java
public void highlightAnswers(int correctIndex, int selectedIndex)
```
Highlights the correct answer and student's selection.

```java
public void setExplanation(String explanation)
```
Shows explanation if available, hides section if null/empty.

### AttemptDao
```java
public Integer getAttemptId(int _quiz_id, int _user_id)
```
Gets the most recent completed attempt ID.

```java
public boolean hasCompleted(int _quiz_id, int _user_id)
```
Checks if user has completed a quiz.

### HomeController
```java
private Button createQuizCard(Quiz quiz)
```
Creates quiz cards with completion status styling.

```java
private void handleQuizCardClick(Stage owner, Quiz quiz)
```
Handles quiz card clicks - opens quiz or results based on completion status.

## Files Created

### Java Classes
1. `src/main/java/com/example/cab302a1/ui/StudentResultDetailController.java`
2. `src/main/java/com/example/cab302a1/ui/ResultQuestionItemController.java`
3. Rename `src\main\java\com\example\cab302a1\service\QuizResultService.java` to `src\main\java\com\example\cab302a1\service\QuizResultDetailService.java` to avoid the name overlap issue

### FXML Files
1. `src/main/resources/com/example/cab302a1/result/StudentResultDetailPage.fxml`
2. `src/main/resources/com/example/cab302a1/result/ResultQuestionItem.fxml`

### CSS Files
1. `src/main/resources/com/example/cab302a1/result/StudentResultDetail.css`

### Documentation
1. `docs/Student-Quiz-Result-Detail-Implementation.md` (this file)

## Files Modified

### Java Classes
1. `src/main/java/com/example/cab302a1/dao/AttemptDao.java`
   - Added `getAttemptId()` method
2. `src/main/java/com/example/cab302a1/result/QuizResultController.java`
   - Updated `handleViewAnswerAction()` to open result detail page
   - Added imports for AttemptDao and StudentResultDetailController
3. `src/main/java/com/example/cab302a1/ui/HomeController.java`
   - Modified `createQuizCard()` to check completion status and add styling
   - Updated `handleQuizCardClick()` to prevent retakes and show results

### CSS Files
1. `src/main/resources/com/example/cab302a1/HomePage.css`
   - Added `.quiz-card-completed` styles for completed quizzes

## Testing Recommendations

### Manual Testing Steps
1. **First Quiz Attempt**
   - Log in as a student
   - Click on a quiz (should have blue background)
   - Complete the quiz
   - Verify result summary page shows
   - Click "View the answer"
   - Verify all questions show with correct answers highlighted in green
   - Verify incorrect answers are highlighted in red
   - Verify explanations show (if provided)
   - Click "Done" to return

2. **Completed Quiz Access**
   - Return to home page
   - Verify the completed quiz now has a green background
   - Click on the completed quiz
   - Verify it opens directly to the result page (no retake)
   - Verify all data displays correctly

3. **Quiz History**
   - Complete multiple quizzes
   - Verify all completed quizzes show with green background
   - Click on each to view results
   - Verify correct data for each quiz

### Edge Cases to Test
- Quiz with no explanation text
- Quiz with all correct answers
- Quiz with all incorrect answers
- Quiz with mixed correct/incorrect answers
- Multiple students completing the same quiz
- Very long question text or explanation text

## Future Enhancements

Potential improvements that could be added:
1. Allow teachers to enable quiz retakes (feature flag)
2. Show score comparison across multiple attempts (if retakes enabled)
3. Export quiz results to PDF
4. Add statistics showing question difficulty based on student performance
5. Allow students to add notes/comments on explanations

## Integration Notes

This implementation reuses existing components:
- **QuizResultDetailService**: For fetching quiz results with student responses
- **AttemptDao**: For tracking quiz completion
- **Session**: For current user management
- **NavigationManager**: For page navigation
- All existing DAOs for data access

The implementation follows the existing code patterns and styling conventions used throughout the project.


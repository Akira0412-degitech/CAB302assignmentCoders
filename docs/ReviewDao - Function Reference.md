# ReviewDao – Function Reference

The `ReviewDao` class manages database operations related to **quiz attempts review**.  
It retrieves attempt history for a specific student and maps it into `QuizReview` objects.

---

## Related Classes
- **`QuizReview`** – Model containing quiz title, score, total questions, and feedback.
- **`ResponseDao`** – Used internally to calculate the number of questions answered for each attempt.
- **`Student`** – Identifies the student whose attempts are being fetched.

---

## `public List<QuizReview> getAllAttemptsById(int userId)`

**Purpose**  
Fetches all quiz attempts for a given student.

**Expected Behavior**
- Joins `quiz_attempts` with `quizzes` to fetch quiz title, score, feedback, and attempt ID.
- Calls `ResponseDao.calculateScoreFromResponses(attemptId)` to determine total questions.
- Constructs a `QuizReview` for each attempt.
- Returns a list of reviews.

**Parameters**
- `userId` – The student’s user ID.

**Returns**
- A `List<QuizReview>` containing all attempts by the student.
- Returns an empty list if no attempts exist.

---

## Usage Example
```java
ReviewDao reviewDao = new ReviewDao();
List<QuizReview> attempts = reviewDao.getAllAttemptsById(studentId);

for (QuizReview review : attempts) {
    System.out.println(review.getQuizName() + ": " +
                       review.getScore() + "/" + review.getTotalQuestion() +
                       " | Feedback: " + review.getFeedback());
}

# ResponseDao – Function Reference

The `ResponseDao` class manages database operations for **question_responses** table.  
It provides methods to save a list of responses for an attempt and to calculate the score (number of correct answers) for that attempt.

---

## Related Class: `QuestionResponse`

The `QuestionResponse` model is used as input for this DAO. It typically contains:
- `response_id` – Unique identifier for the response (auto-generated in DB).
- `attempt_id` – ID of the quiz attempt this response belongs to.
- `question_id` – ID of the question answered.
- `option_id` – ID of the chosen option.
- `isCorrect` – Boolean (`true` if the chosen option was correct, `false` otherwise).

---

## `public void saveResponse(int attemptId, List<QuestionResponse> responses)`

**Purpose**  
Saves all responses for a given attempt into the `question_responses` table.

**Expected Behavior**
- Iterates over the provided list of `QuestionResponse` objects.
- Inserts `attempt_id`, `question_id`, `option_id`, and `isCorrect` for each response.
- Uses **batch execution** (`addBatch` + `executeBatch`) for better performance.

**Parameters**
- `attemptId` – The ID of the quiz attempt.
- `responses` – A list of `QuestionResponse` objects, each containing the response data.

**Returns**
- Nothing (`void`).
- Inserts one row per response into the database.

---

## `public int calculateScoreFromResponses(int attemptId)`

**Purpose**  
Calculates the score for a given attempt based on stored responses.

**Expected Behavior**
- Runs a query on `question_responses`.
- Counts how many rows have `is_correct = TRUE` for the given `attemptId`.

**Parameters**
- `attemptId` – The ID of the quiz attempt to calculate the score for.

**Returns**
- The number of correct answers (`int`).
- `-1` if the query fails.

---

# Usage Notes
1. **Save Responses**
    - Call `saveResponse(attemptId, responses)` immediately after a student submits their answers.
2. **Calculate Score**
    - Use `calculateScoreFromResponses(attemptId)` to compute how many answers were correct.
    - This value is typically passed into `AttemptDao.endAttempt` to finalize the attempt with a score.

---

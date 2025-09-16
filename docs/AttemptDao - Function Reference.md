# AttemptDao – Function Reference

The `AttemptDao` class manages database operations related to **quiz_attempts** table.  
It is responsible for creating attempts, checking existence, finalizing attempts, and retrieving scores.

---

## `public int startAttempt(int quizId, int userId)`

**Purpose**  
Creates a new quiz attempt for a given quiz and user. Initializes with `score = 0` and `is_completed = false`.

**Parameters**
- `quizId` – ID of the quiz being attempted.
- `userId` – ID of the student starting the attempt.

**Returns**
- `attempt_id` (auto-generated primary key) if successful.
- `-1` if the insert fails.

---

## `public boolean attemptExist(int attemptId)`

**Purpose**  
Checks whether a specific attempt record exists in the database.

**Parameters**
- `attemptId` – ID of the attempt to check.

**Returns**
- `true` if the attempt exists.
- `false` if not found or an error occurs.

---

## `public void endAttempt(int attemptId)`

**Purpose**  
Finalizes an attempt by:
1. Calculating the score from saved responses (`ResponseDao.calculateScoreFromResponses`).
2. Updating the attempt record with the score and marking it as completed.

**Parameters**
- `attemptId` – ID of the attempt to finalize.

**Returns**
- Nothing (`void`).
- Updates the database in place.

---

## `public Integer getScore(int quizId, int userId)`

**Purpose**  
Fetches the **latest completed attempt’s score** for a given user and quiz.

**Parameters**
- `quizId` – ID of the quiz.
- `userId` – ID of the student.

**Returns**
- `Integer` score of the most recent completed attempt.
- `null` if no completed attempts are found.

---

## `public boolean hasCompleted(int quizId, int userId)`

**Purpose**  
Checks whether a user has completed **at least one attempt** for a specific quiz.

**Parameters**
- `quizId` – ID of the quiz.
- `userId` – ID of the student.

**Returns**
- `true` if at least one attempt has been completed.
- `false` otherwise.  

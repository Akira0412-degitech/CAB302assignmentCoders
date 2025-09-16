# QuestionDao – Function Reference

The `QuestionDao` class manages database operations related to **questions** table.  
It works together with:
- **`Quiz`** – Represents a quiz (used when fetching questions by quiz).
- **`QuizQuestionCreate`** – A model used for creating and updating questions (holds question text, explanation, and IDs).

---

# Related Models

### `Quiz`
Represents a quiz.
- `quizId` – Unique ID of the quiz.
- Other fields (title, description, etc.) may exist but are not used directly in `QuestionDao`.

### `QuizQuestionCreate`
Represents a question object.
- `questionId` – Unique ID of the question.
- `quizId` – ID of the quiz this question belongs to.
- `questionText` – The text of the question.
- `explanation` – Optional explanation text.

---

## `public List<QuizQuestionCreate> getAllQuestions(Quiz quiz)`

**Purpose**  
Fetches all questions that belong to a given quiz.

**Parameters**
- `quiz` – A `Quiz` object (only `quizId` is used here).

**Returns**
- A `List<QuizQuestionCreate>` containing each question’s:
    - `question_id`
    - `quiz_id`
    - `statement` (question text)
    - `explanation`
- An empty list if no questions are found.

---

## `public int insertQuestion(QuizQuestionCreate question)`

**Purpose**  
Inserts a new question into the `questions` table.

**Parameters**
- `question` – A `QuizQuestionCreate` object containing:
    - `quizId` – The quiz the question belongs to.
    - `questionText` – The text of the question.
    - `explanation` – An explanation for the correct answer.

**Notes**
- Currently the `type` column is always set to `"MCQ"` (multiple-choice question).

**Returns**
- The generated `question_id` if successful.
- `-1` if insertion fails.

---

## `public void updateQuestion(QuizQuestionCreate question)`

**Purpose**  
Updates the text and explanation of an existing question.

**Parameters**
- `question` – A `QuizQuestionCreate` object containing:
    - `questionId` – The ID of the question to update.
    - `questionText` – New text for the question.
    - `explanation` – Updated explanation.

**Returns**
- Nothing (`void`).
- The database row is updated in place.

---

## `public int getNumQuestion(int quizId)`

**Purpose**  
Counts how many questions exist for a given quiz.

**Parameters**
- `quizId` – The ID of the quiz.

**Returns**
- The number of questions (`int`) for the quiz.
- `-1` if the query fails.



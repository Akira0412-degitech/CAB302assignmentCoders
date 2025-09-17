# UserDao – Function Reference

The `UserDao` class manages database operations related to **users**.  
It supports creating accounts, logging in, checking existence, and retrieving user data.

---

## Related Classes

- **`User`** – The base class for all users, containing shared fields like `user_id`, `username`, `email`, `password`, `role`, and `created_at`.
- **`Teacher`** – Extends `User`, representing a teacher account.
- **`Student`** – Extends `User`, representing a student account.

The DAO decides whether to return a `Teacher` or `Student` object depending on the `role` field in the database.

---

## `public User getUserById(int userId)`

**Purpose**  
Fetches a user by their ID.

**Expected Behavior**
- Queries the `users` table for the given `userId`.
- Constructs and returns either a `Teacher` or `Student` object based on the user’s role.

**Parameters**
- `userId` – The ID of the user.

**Returns**
- A `User` object (`Teacher` or `Student`) if found.
- `null` if no user is found.

---

## `public void printAllUsers()`

**Purpose**  
Prints all users in the database to the console (for debugging/testing).

**Expected Behavior**
- Queries the `users` table and prints each record’s details (`user_id`, `username`, `email`, `created_at`, `password`, `role`).

**Parameters**
- None.

**Returns**
- Nothing (`void`).
- Outputs results directly to the console.

---

## `public boolean existsByEmail(String email)`

**Purpose**  
Checks if a user with the given email exists.

**Expected Behavior**
- Runs a query against the `users` table.
- Returns true if at least one record exists.

**Parameters**
- `email` – The email to check.

**Returns**
- `true` if the email is already registered.
- `false` otherwise.

---

## `public User signUpUser(String username, String email, String password, String role)`

**Purpose**  
Registers a new user in the system.

**Expected Behavior**
- If the email already exists, prints a message and returns `null`.
- Otherwise, inserts a new row into the `users` table.
- Fetches the newly created user with `getUserById` and returns it.

**Parameters**
- `username` – The new user’s name.
- `email` – The new user’s email.
- `password` – The new user’s password.
- `role` – The user’s role (`Teacher` or `Student`).

**Returns**
- A `User` object (either `Teacher` or `Student`) for the newly registered user.
- `null` if sign-up fails or the email already exists.

---

## `public User login(String email, String password)`

**Purpose**  
Authenticates a user using email and password.

**Expected Behavior**
- If the email does not exist, prints a message and returns `null`.
- If the email exists but the password does not match, returns `null`.
- If both match, returns a `Teacher` or `Student` object depending on the stored role.

**Parameters**
- `email` – The user’s email.
- `password` – The user’s password.

**Returns**
- A `User` object (`Teacher` or `Student`) if authentication succeeds.
- `null` if login fails.

---

# Usage Notes

- `signUpUser` and `login` are the main entry points for controllers like `SignUpController` or `LoginController`.
- Roles are strictly string-based in the DB (`Teacher` or `Student`). DAO returns the appropriate subclass.
- `printAllUsers` is for debugging only and should not be used in production code due to exposing sensitive data (passwords).

---

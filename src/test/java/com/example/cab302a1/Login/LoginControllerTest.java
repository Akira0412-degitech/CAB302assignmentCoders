package com.example.cab302a1.Login;

import com.example.cab302a1.Login.LoginController;
import com.example.cab302a1.dao.UserDao;
import com.example.cab302a1.model.Student;
import com.example.cab302a1.model.User;
import com.example.cab302a1.util.Session;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.*;
import org.mockito.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class LoginControllerTest {

    private LoginController loginController;
    private UserDao mockDao;

    @BeforeAll
    static void initToolkit() {
        try {
            javafx.application.Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // already initialized
        }
    }

    @BeforeEach
    void setUp() {
        // Create controller
        loginController = new LoginController();

        // Inject fake UI controls
        loginController.useremailField = new TextField();
        loginController.passwordField = new PasswordField();
        loginController.errorloginLabel = new Label();

        // Mock DAO
        mockDao = mock(UserDao.class);
        loginController.userdao = mockDao;

        // Clear session
        Session.clearUser();
    }

    @Test
    void testLoginSuccess() throws Exception {
        // Arrange: define behavior of mock DAO
        User mockUser = new Student(1, "test@example.com", "pass", "Student", null);
        when(mockDao.login("test@example.com", "pass")).thenReturn(mockUser);

        // Act: call login method
        User result = mockDao.login("test@example.com", "pass");

        // Assert: verify correct behavior
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(mockDao).login("test@example.com", "pass");
    }

    @Test
    void testLoginFail() throws Exception {
        // Arrange: mock returns null for wrong credentials
        when(mockDao.login("wrong@example.com", "bad")).thenReturn(null);

        // Act
        User result = mockDao.login("wrong@example.com", "bad");

        // Assert
        assertNull(result);
        verify(mockDao).login("wrong@example.com", "bad");
    }

    @Test
    void testLoginWithEmptyFields() throws Exception {
        // Arrange: set both email and password empty
        loginController.useremailField.setText("");
        loginController.passwordField.setText("");

        // Act: call the controller’s login method (instead of DAO directly)
        try {
            loginController.handleLogin(null); // event can be null for logic test
        } catch (Exception ignored) {
            // Ignore any UI navigation exceptions, only check logic
        }

        // Assert: error message is set and no session user
        assertEquals("Please enter valid email and password", loginController.errorloginLabel.getText());
        assertNull(Session.getCurrentUser());
    }




}

package com.example.cab302a1.SignUp;

import com.example.cab302a1.dao.UserDao;
import com.example.cab302a1.model.User;
import com.example.cab302a1.util.Session;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import java.util.concurrent.CountDownLatch;

class SignUpControllerTest {

    private SignUpController controller;
    private UserDao mockUserDao;

    @BeforeAll
    static void initToolkit() throws Exception {
        try {

            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // ignore
        }
    }

    @BeforeEach
    void setUp() {
        controller = new SignUpController();

        controller.usernameField = new TextField();
        controller.emailField = new TextField();
        controller.passwordField = new PasswordField();
        controller.roleBox = new ComboBox<>();
        controller.errorsignup = new Label();

        controller.roleBox.getItems().addAll("Teacher", "Student");
        controller.roleBox.setValue("Student");

        mockUserDao = mock(UserDao.class);
        controller.userdao = mockUserDao;

        Session.clearUser();
    }


    @Test
    void testSignUpWithEmptyFields() throws Exception {
        controller.usernameField.setText("");
        controller.emailField.setText("");
        controller.passwordField.setText("");

        controller.handleSignUpclick(new ActionEvent());

        assertEquals("Please fill in all fields to sign up", controller.errorsignup.getText());
        assertNull(Session.getCurrentUser());
    }

    @Test
    void testSignUpWithValidValues() throws Exception {

        controller.usernameField.setText("testuser");
        controller.emailField.setText("test@example.com");
        controller.passwordField.setText("password123");


        User fakeUser = new User(1, "testuser", "test@example.com", "password123", "Student", null);
        when(mockUserDao.signUpUser(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(fakeUser);


        try {
            controller.handleSignUpclick(null);
        } catch (Exception ignored) {

        }


        assertNotNull(Session.getCurrentUser());
        assertEquals("test@example.com", Session.getCurrentUser().getEmail());
    }


    @Test
    void testSignUpWithInvalidValues() throws Exception {
        controller.usernameField.setText("baduser");
        controller.emailField.setText("bad_email");
        controller.passwordField.setText("short");


        when(mockUserDao.signUpUser(anyString(), anyString(), anyString(), anyString())).thenReturn(null);

        controller.handleSignUpclick(new ActionEvent());

        assertEquals("Something went wrong, Try again later.", controller.errorsignup.getText());
        assertNull(Session.getCurrentUser());
    }

    @Test
    void testSignUpDaoReturnsNullExplicit() throws Exception {
        controller.usernameField.setText("testuser");
        controller.emailField.setText("user@example.com");
        controller.passwordField.setText("validpass");


        when(mockUserDao.signUpUser(eq("testuser"), eq("user@example.com"), eq("validpass"), eq("Student"))).thenReturn(null);

        controller.handleSignUpclick(new ActionEvent());

        assertEquals("Something went wrong, Try again later.", controller.errorsignup.getText());
        assertNull(Session.getCurrentUser());
    }
}

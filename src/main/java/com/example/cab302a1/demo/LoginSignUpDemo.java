package com.example.cab302a1.demo;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.dao.UserDao;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Demo application to test the improved Login and SignUp UI.
 * This demonstrates the new design implementation while preserving all functionality.
 * 
 * @author CAB302 Assignment Team
 * @version 2.0 (UI Improvements)
 */
public class LoginSignUpDemo extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("=== Login/SignUp UI Demo Starting ===");
        
        // Initialize database connection
        initializeDatabase();
        
        // Load the improved Login page
        FXMLLoader fxmlLoader = new FXMLLoader(
            LoginSignUpDemo.class.getResource("/com/example/cab302a1/Login/Login-view.fxml")
        );
        
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        
        // Load CSS stylesheet for login page
        try {
            String cssPath = LoginSignUpDemo.class.getResource("/com/example/cab302a1/Login/Login.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
            System.out.println("✅ Login CSS loaded successfully: " + cssPath);
        } catch (Exception e) {
            System.err.println("❌ Could not load Login.css: " + e.getMessage());
        }
        
        // Configure stage
        stage.setTitle("Interactive Quiz Creator - Login Demo");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMinWidth(600);
        stage.setMinHeight(500);
        stage.centerOnScreen();
        stage.show();
        
        // Print usage instructions
        printUsageInstructions();
    }
    
    /**
     * Initialize database connection and print available test users
     */
    private void initializeDatabase() {
        try {
            DBconnection.migrate();
            try (var conn = DBconnection.getConnection()) {
                System.out.println("✅ Connected to database: " + conn.getCatalog());
                
                UserDao userDao = new UserDao();
                System.out.println("\n📋 Available test users:");
                userDao.printAllUsers();
                
            }
        } catch (Exception e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Print usage instructions for testing
     */
    private void printUsageInstructions() {
        System.out.println("\n🎯 === TESTING INSTRUCTIONS ===");
        System.out.println("\n📋 TEST LOGIN:");
        System.out.println("   Student: demo@example.com / demo");
        System.out.println("   Teacher: admin@example.com / 1234");
        
        System.out.println("\n📋 TEST SIGNUP:");
        System.out.println("   Try: Username: testuser, Email: test@example.com, Password: password123");
        System.out.println("   Role: Student or Teacher");
        
        System.out.println("\n🎨 UI FEATURES TO TEST:");
        System.out.println("   ✅ Prototype-matching design");
        System.out.println("   ✅ Rounded input fields with proper styling");
        System.out.println("   ✅ Color scheme compliance (#005BA1, #A5D8FF, #DBEAFE)");
        System.out.println("   ✅ Hover effects on buttons and links");
        System.out.println("   ✅ Focus states on input fields");
        System.out.println("   ✅ Error message display");
        System.out.println("   ✅ Navigation between Login and SignUp");
        
        System.out.println("\n🔧 FUNCTIONALITY TO VERIFY:");
        System.out.println("   ✅ Existing login functionality preserved");
        System.out.println("   ✅ Database integration unchanged");
        System.out.println("   ✅ Session management works");
        System.out.println("   ✅ User roles (Teacher/Student) working");
        System.out.println("   ✅ SignUp with separate username/email fields");
        System.out.println("   ✅ Email validation");
        System.out.println("   ✅ Proper error handling");
        
        System.out.println("\n📖 Full testing guide: docs/Login-SignUp-UI-Testing-Guide.md");
        System.out.println("=====================================\n");
    }

    public static void main(String[] args) {
        System.out.println("🚀 Starting Login/SignUp UI Demo...");
        System.out.println("📄 This demo showcases the improved UI that matches the prototype designs");
        System.out.println("🔄 while preserving all existing functionality.\n");
        
        launch(args);
    }
}

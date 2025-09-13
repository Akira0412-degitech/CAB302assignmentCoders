# Navbar Component

A reusable left-sidebar navigation component for the Interactive Quiz Creator JavaFX application with integrated smart navigation system.

## Overview

The Navbar component provides a consistent navigation experience across all pages of the application. It follows the design system specifications with exact color codes and modern styling. **Now includes NavigationManager integration for smart EXIT button functionality that returns users to their previous page.**

## File Structure

```
src/main/resources/com/example/cab302a1/components/
├── Navbar.fxml              # FXML layout definition
├── Navbar.css               # Component-specific styles
└── README.md               # This documentation

src/main/java/com/example/cab302a1/components/
├── NavbarController.java    # Controller with action handlers
└── NavigationManager.java   # Smart navigation system (NEW)
```

## Features

- **Responsive Design**: Fixed 200px width with proper spacing
- **Design System Compliance**: Uses exact colors from the style guide
- **Interactive States**: Hover, focus, active, and pressed states
- **Accessibility**: Proper ARIA labels and keyboard navigation
- **Visual Hierarchy**: Distinguished EXIT button in coral red
- **Active State Management**: Visual indication of current page
- **🆕 Smart Navigation**: EXIT → Cancel returns to previous page automatically
- **🆕 Navigation History**: Tracks user's page navigation for proper back navigation
- **🆕 Team Integration Ready**: Easy setup for new pages to work with EXIT functionality

## Color Palette Used

- **Deep Blue** (#005BA1): Primary text, borders, active states
- **Light Blue** (#A5D8FF): Button backgrounds, highlights
- **Soft Blue** (#F5F3FF): Navbar background
- **Coral Red** (#E03131): EXIT button background
- **White** (#FFFFFF): Secondary button backgrounds, active text

## 🆕 NavigationManager Integration for Team Members

### IMPORTANT: How to Make EXIT Button Work With Your Page

The Navbar component now uses a smart NavigationManager that tracks navigation history. When users click EXIT → Cancel, they return to the **exact page they came from** (not always the navbar demo).

#### **Step 1: Basic Setup (Required for All New Pages)**

In your page controller, add this import and registration:

```java
import com.example.cab302a1.components.NavigationManager;

public class YourPageController implements Initializable {
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Your existing initialization code...
        
        // REQUIRED: Register your page with NavigationManager
        NavigationManager.PageInfo yourPageInfo = new NavigationManager.PageInfo(
            "/com/example/cab302a1/yourpackage/YourPage.fxml",    // Your FXML path
            "/com/example/cab302a1/yourpackage/YourPage.css",     // Your CSS path (optional)
            "Interactive Quiz Creator - Your Page Title",         // Window title
            1200, 700,  // Width, height
            true        // Resizable (true/false)
        );
        
        NavigationManager.getInstance().setCurrentPage(yourPageInfo);
        
        System.out.println("Your page registered with NavigationManager");
    }
}
```

#### **Step 2: Navigation Between Pages (Recommended)**

When navigating TO your page from another page:

```java
// Instead of manually loading FXML, use NavigationManager:
public void navigateToYourPage(Stage stage) throws IOException {
    NavigationManager.PageInfo yourPageInfo = new NavigationManager.PageInfo(
        "/com/example/cab302a1/yourpackage/YourPage.fxml",
        "/com/example/cab302a1/yourpackage/YourPage.css", 
        "Interactive Quiz Creator - Your Page",
        1200, 700, true
    );
    
    // This automatically saves current page to history
    NavigationManager.getInstance().navigateTo(stage, yourPageInfo);
}
```

#### **Step 3: Predefined Page Constants (Recommended)**

For frequently used pages, add constants to NavigationManager.Pages:

```java
// Add this to NavigationManager.java → Pages class:
public static final PageInfo YOUR_PAGE = new PageInfo(
    "/com/example/cab302a1/yourpackage/YourPage.fxml",
    "/com/example/cab302a1/yourpackage/YourPage.css",
    "Interactive Quiz Creator - Your Page",
    1200, 700, true
);

// Then use it easily:
NavigationManager.getInstance().navigateTo(stage, NavigationManager.Pages.YOUR_PAGE);
```

#### **Quick Integration Examples**

**Example 1: Student Dashboard Integration**
```java
public class StudentDashboardController implements Initializable {
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Register with NavigationManager
        NavigationManager.PageInfo dashboardInfo = new NavigationManager.PageInfo(
            "/com/example/cab302a1/dashboard/StudentDashboard.fxml",
            "/com/example/cab302a1/dashboard/StudentDashboard.css",
            "Interactive Quiz Creator - Student Dashboard",
            1200, 700, true
        );
        
        NavigationManager.getInstance().setCurrentPage(dashboardInfo);
        
        // Now EXIT → Cancel will return to this dashboard!
    }
}
```

**Example 2: Answer Review Integration**
```java
public class AnswerReviewController implements Initializable {
    
    public static void showAnswerReview(Stage stage, int quizId) throws IOException {
        NavigationManager.PageInfo reviewInfo = new NavigationManager.PageInfo(
            "/com/example/cab302a1/review/AnswerReview.fxml",
            "/com/example/cab302a1/review/AnswerReview.css",
            "Interactive Quiz Creator - Answer Review",
            1200, 700, true
        );
        
        // Navigate with history tracking
        NavigationManager.getInstance().navigateTo(stage, reviewInfo);
        
        // User can now: Review → EXIT → Cancel → Back to Review!
    }
}
```

#### **What Happens Now:**

1. **User on Your Page** → Click EXIT → Logout Confirmation
2. **Click Cancel** → **Returns to Your Page** (not navbar demo)
3. **Automatic History** → Works for any navigation depth
4. **No Extra Code** → Just register your page once

#### **Benefits for Your Team:**

- ✅ **Automatic**: Works with any page that registers itself
- ✅ **User-Friendly**: Users return to where they expect
- ✅ **Consistent**: Same behavior across all pages
- ✅ **Easy**: Just one method call in your initialize()
- ✅ **Flexible**: Supports different page sizes and configurations

---

## Standard Integration Guide

### Method 1: Using fx:include (Recommended)

Add the navbar to any existing FXML layout:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?import javafx.scene.layout.BorderPane?>

<BorderPane xmlns="http://javafx.com/javafx/21" xmlns:fx="http://javafx.com/fxml/1">
    <left>
        <fx:include source="components/Navbar.fxml"/>
    </left>
    <center>
        <!-- Your main content here -->
    </center>
</BorderPane>
```

### Method 2: Programmatic Loading

Load the component programmatically in your controller:

```java
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class YourController {
    
    @FXML
    private BorderPane mainPane;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("components/Navbar.fxml"));
            VBox navbar = loader.load();
            NavbarController navbarController = loader.getController();
            
            mainPane.setLeft(navbar);
            
            // Set active navigation if needed
            navbarController.setActiveNavigation("Home");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

## Navigation Buttons

1. **Home** - Primary navigation (Light blue with border)
2. **Review** - Secondary navigation (White with border)
3. **Timetable** - Secondary navigation (White with border)
4. **EXIT** - Smart logout with confirmation (Coral red background)
   - Shows logout confirmation dialog
   - **Cancel** returns to previous page automatically
   - **Logout** navigates to login page and clears session

## Controller Methods

### Public Methods

- `setActiveNavigation(String buttonName)` - Set active button programmatically
- `getNavbarContainer()` - Access the main container for custom styling

### Action Handlers

- `handleHomeAction()` - Home button click handler
- `handleReviewAction()` - Review button click handler
- `handleTimetableAction()` - Timetable button click handler
- `handleExitAction()` - EXIT button click handler (now uses NavigationManager)

## Customization

### Styling

The component uses CSS classes that can be overridden:

- `.navbar-container` - Main container
- `.navbar-title` - App title/logo area
- `.navbar-button` - Base button styling
- `.navbar-button-primary` - Home button styling
- `.navbar-button-secondary` - Review/Timetable buttons
- `.navbar-button-exit` - EXIT button styling
- `.navbar-button-active` - Active/selected button state

### Adding New Navigation Items

1. Add button to `Navbar.fxml`:
```xml
<Button fx:id="newBtn" 
        styleClass="navbar-button, navbar-button-secondary" 
        maxWidth="1.7976931348623157E308" 
        mnemonicParsing="false" 
        text="New Page"
        onAction="#handleNewAction">
    <VBox.margin>
        <Insets bottom="16.0" />
    </VBox.margin>
</Button>
```

2. Add handler to `NavbarController.java`:
```java
@FXML
private Button newBtn;

@FXML
private void handleNewAction(ActionEvent event) {
    System.out.println("Navigation: New button clicked");
    setActiveButton(newBtn);
    navigateToPage("New");
}
```

3. Update `setActiveNavigation()` method to include the new button.

## Implementation Notes

- Component is fully self-contained with its own CSS
- **NavigationManager integration** provides smart navigation history
- **EXIT button** shows logout confirmation with proper back navigation
- Console logging is used for demonstration (replace with actual navigation)
- Active button state is managed automatically
- Accessibility features are built-in
- **Automatic compatibility** with team member pages (when properly registered)

## Testing

The component can be tested by:

1. Including it in an existing view
2. Clicking navigation buttons to see console output
3. Observing visual state changes
4. Testing keyboard navigation
5. **Testing EXIT → Cancel flow** to verify NavigationManager integration

### NavigationManager Testing

Test the smart navigation system:

1. **Register your page** with NavigationManager in initialize()
2. **Navigate to your page** from another page
3. **Click EXIT button** → Should show logout confirmation
4. **Click Cancel** → Should return to your page (not navbar demo)
5. **Check console output** for NavigationManager messages

## Troubleshooting NavigationManager Integration

### Common Issues and Solutions

**Problem: Cancel button goes to navbar demo instead of my page**
```java
// Solution: Make sure you register your page in initialize()
@Override
public void initialize(URL location, ResourceBundle resources) {
    // Add this:
    NavigationManager.PageInfo myPageInfo = new NavigationManager.PageInfo(
        "/com/example/cab302a1/mypackage/MyPage.fxml", // Correct path
        "/com/example/cab302a1/mypackage/MyPage.css",
        "My Page Title",
        1200, 700, true
    );
    NavigationManager.getInstance().setCurrentPage(myPageInfo);
}
```

**Problem: NavigationManager not found / import error**
```java
// Solution: Add correct import
import com.example.cab302a1.components.NavigationManager;

// Make sure module-info.java exports components package:
// exports com.example.cab302a1.components;
```

**Problem: Page doesn't navigate properly**
```java
// Solution: Use NavigationManager for navigation instead of manual FXML loading
// Replace this:
FXMLLoader loader = new FXMLLoader(getClass().getResource("MyPage.fxml"));
Scene scene = new Scene(loader.load());
stage.setScene(scene);

// With this:
NavigationManager.getInstance().navigateTo(stage, myPageInfo);
```

**Problem: History gets cleared unexpectedly**
```java
// Solution: Only call clearHistory() on actual logout
// Don't call it during normal navigation:
NavigationManager.getInstance().clearHistory(); // Only for logout!
```

### Debug Output

Enable NavigationManager debug output by watching console for:
- "Added to history: [PageInfo]"
- "Navigated back to: [PageInfo]"
- "Navigation history cleared"
- "Current page set to: [PageInfo]"

## Browser/Platform Compatibility

- Tested with JavaFX 21
- Compatible with Windows, macOS, and Linux
- Follows JavaFX best practices for cross-platform consistency
- NavigationManager tested across different page types and navigation patterns

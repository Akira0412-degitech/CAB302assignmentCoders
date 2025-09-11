# Navbar Component

A reusable left-sidebar navigation component for the Interactive Quiz Creator JavaFX application.

## Overview

The Navbar component provides a consistent navigation experience across all pages of the application. It follows the design system specifications with exact color codes and modern styling.

## File Structure

```
src/main/resources/com/example/cab302a1/components/
├── Navbar.fxml          # FXML layout definition
├── Navbar.css           # Component-specific styles
└── README.md           # This documentation

src/main/java/com/example/cab302a1/components/
└── NavbarController.java # Controller with action handlers
```

## Features

- **Responsive Design**: Fixed 200px width with proper spacing
- **Design System Compliance**: Uses exact colors from the style guide
- **Interactive States**: Hover, focus, active, and pressed states
- **Accessibility**: Proper ARIA labels and keyboard navigation
- **Visual Hierarchy**: Distinguished EXIT button in coral red
- **Active State Management**: Visual indication of current page

## Color Palette Used

- **Deep Blue** (#005BA1): Primary text, borders, active states
- **Light Blue** (#A5D8FF): Button backgrounds, highlights
- **Soft Blue** (#F5F3FF): Navbar background
- **Coral Red** (#E03131): EXIT button background
- **White** (#FFFFFF): Secondary button backgrounds, active text

## Integration Guide

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
4. **EXIT** - Danger action (Coral red background)

## Controller Methods

### Public Methods

- `setActiveNavigation(String buttonName)` - Set active button programmatically
- `getNavbarContainer()` - Access the main container for custom styling

### Action Handlers

- `handleHomeAction()` - Home button click handler
- `handleReviewAction()` - Review button click handler
- `handleTimetableAction()` - Timetable button click handler
- `handleExitAction()` - EXIT button click handler

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
- Console logging is used for demonstration (replace with actual navigation)
- EXIT button includes placeholder for confirmation dialog
- Active button state is managed automatically
- Accessibility features are built-in

## Testing

The component can be tested by:

1. Including it in an existing view
2. Clicking navigation buttons to see console output
3. Observing visual state changes
4. Testing keyboard navigation

## Browser/Platform Compatibility

- Tested with JavaFX 21
- Compatible with Windows, macOS, and Linux
- Follows JavaFX best practices for cross-platform consistency

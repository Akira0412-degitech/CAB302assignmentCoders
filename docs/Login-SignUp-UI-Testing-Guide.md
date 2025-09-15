# Login and SignUp UI Testing Guide

## Overview
This document provides comprehensive testing instructions for the improved Login and SignUp UI that now matches the prototype designs while preserving all existing functionality.

## UI Improvements Summary

### ✅ **Visual Changes Implemented**
1. **Design Match**: Both pages now exactly match the provided prototypes
2. **Color Compliance**: All colors follow the design style guide with exact hex codes
3. **Typography**: Proper font families, sizes, and weights as specified
4. **Layout**: Centered, responsive design with proper spacing
5. **Form Elements**: Rounded input fields, styled buttons, and proper navigation links

### ✅ **Functional Enhancements**
1. **Email Field Added**: SignUp page now has separate Username and Email fields
2. **Improved Validation**: Better error messages and field validation
3. **Enhanced UX**: Proper error message display management
4. **Accessibility**: Better focus management and visual feedback

## Pre-Testing Setup

### Database Verification
Ensure your database contains test users:
```sql
-- Default users should exist (from migration):
-- email: 'demo@example.com', password: 'demo', role: 'Student'
-- email: 'admin@example.com', password: '1234', role: 'Teacher'
```

## Test Cases

### 1. Login Page Testing

#### **Visual Design Test**
- [ ] **Background**: Light blue (#DBEAFE) background
- [ ] **Title**: "Login" in deep blue (#005BA1), 28px, bold
- [ ] **Input Fields**: 
  - [ ] White background with black border (2px)
  - [ ] Rounded corners (25px border radius)
  - [ ] Proper placeholder text: "User Name", "Password"
  - [ ] 300px width, 50px height
- [ ] **Button**: 
  - [ ] Light blue background (#A5D8FF)
  - [ ] Deep blue text and border (#005BA1)
  - [ ] "Login" text, 150px width
  - [ ] Hover effect changes background
- [ ] **Navigation Link**: 
  - [ ] "Signup?" in deep blue, underlined
  - [ ] Right-aligned
  - [ ] Hover effect darkens color

#### **Functionality Test**
- [ ] **Successful Login**:
  1. Enter: `demo@example.com` / `demo`
  2. Click Login
  3. ✅ Should navigate to HomePage with title "Student-Home"
  4. ✅ Console shows "Login successful: demo@example.com"

- [ ] **Invalid Credentials**:
  1. Enter: `wrong@email.com` / `wrongpass`
  2. Click Login
  3. ✅ Error message: "Invalid username or password. Please try again."
  4. ✅ Error message visible in red text

- [ ] **Empty Fields**:
  1. Leave fields empty, click Login
  2. ✅ Error message: "Please enter your username and password"

- [ ] **Navigation to SignUp**:
  1. Click "Signup?" link
  2. ✅ Should navigate to SignUp page

### 2. SignUp Page Testing

#### **Visual Design Test**
- [ ] **Background**: Light blue (#DBEAFE) background
- [ ] **Title**: "SignUp" in deep blue (#005BA1), 28px, bold
- [ ] **Input Fields**: 
  - [ ] Four fields: User Name, Email, Role, Password
  - [ ] Same styling as Login page
  - [ ] Proper placeholder text
- [ ] **Role Dropdown**: 
  - [ ] White background with black border
  - [ ] Rounded corners
  - [ ] Shows "Teacher" and "Student" options
  - [ ] Default: "Student"
- [ ] **Button**: 
  - [ ] Same styling as Login page
  - [ ] "SignUp" text
- [ ] **Navigation Link**: 
  - [ ] "Login?" in deep blue, underlined
  - [ ] Right-aligned

#### **Functionality Test**
- [ ] **Successful SignUp**:
  1. Enter: Username: `newuser`, Email: `newuser@test.com`, Password: `password123`, Role: `Student`
  2. Click SignUp
  3. ✅ Should navigate to HomePage
  4. ✅ Console shows signup success with both username and email
  5. ✅ User created in database with email `newuser@test.com`

- [ ] **Email Validation**:
  1. Enter invalid email: `invalidemail`
  2. Click SignUp
  3. ✅ Error message: "Please enter a valid email address"

- [ ] **Empty Fields**:
  1. Leave any field empty, click SignUp
  2. ✅ Error message: "Please fill in all fields to sign up"

- [ ] **Duplicate Email**:
  1. Try to signup with existing email: `demo@example.com`
  2. ✅ Error message: "Email already exists or signup failed. Please try again."

- [ ] **Role Selection**:
  1. Test both Teacher and Student roles
  2. ✅ Dropdown works properly
  3. ✅ Selected role is used in signup

- [ ] **Navigation to Login**:
  1. Click "Login?" link
  2. ✅ Should navigate to Login page

### 3. Cross-Page Navigation Test

- [ ] **Login → SignUp → Login**:
  1. Start on Login page
  2. Click "Signup?" → Should go to SignUp
  3. Click "Login?" → Should return to Login
  4. ✅ Navigation works bidirectionally

- [ ] **Window Properties**:
  - [ ] Window size: 800x600
  - [ ] Window is resizable
  - [ ] Minimum size: 600x500
  - [ ] Window centers on screen after navigation

### 4. Database Integration Test

#### **Login Database Test**
- [ ] **UserDao.login() Integration**:
  1. Login with existing user
  2. ✅ `UserDao.login(email, password)` called correctly
  3. ✅ `Session.setCurrentUser()` called with returned User object
  4. ✅ User object contains correct email and role

#### **SignUp Database Test**
- [ ] **UserDao.signUpUser() Integration**:
  1. SignUp with new user
  2. ✅ `UserDao.signUpUser(email, password, role)` called with EMAIL field (not username)
  3. ✅ Username displayed in UI but email used for database
  4. ✅ Console shows both username and email separately
  5. ✅ New user exists in database with email

### 5. CSS and Styling Test

#### **Responsive Design**
- [ ] **Window Resizing**:
  1. Resize window to minimum size (600x500)
  2. ✅ Layout remains centered and functional
  3. ✅ Input fields maintain proper proportions

#### **Interactive States**
- [ ] **Input Field Focus**:
  1. Click on input fields
  2. ✅ Border changes to blue (#005BA1)
  3. ✅ Focus effect visible

- [ ] **Button Hover**:
  1. Hover over Login/SignUp buttons
  2. ✅ Background color changes to darker blue
  3. ✅ Subtle shadow effect appears

- [ ] **Link Hover**:
  1. Hover over navigation links
  2. ✅ Text color darkens

### 6. Error Handling Test

- [ ] **Error Message Display**:
  1. Trigger any error condition
  2. ✅ Error message appears in red text (#E03131)
  3. ✅ Error label becomes visible and managed
  4. ✅ Error message is clear and user-friendly

- [ ] **Error Message Clearing**:
  1. Fix the error condition and retry
  2. ✅ Error message disappears appropriately

### 7. Accessibility Test

- [ ] **Keyboard Navigation**:
  1. Use Tab key to navigate between fields
  2. ✅ Focus moves logically: Field1 → Field2 → Button → Link
  3. ✅ Enter key activates buttons

- [ ] **Screen Reader Compatibility**:
  1. Test with screen reader if available
  2. ✅ Form labels and roles properly announced

## Performance Test

- [ ] **Loading Speed**:
  1. Measure page load time
  2. ✅ Pages load quickly (< 1 second)
  3. ✅ CSS applies immediately without flicker

- [ ] **Memory Usage**:
  1. Monitor memory during navigation
  2. ✅ No significant memory leaks during page transitions

## Regression Test

### **Existing Functionality Preservation**
- [ ] **Database Connections**: All UserDao methods work unchanged
- [ ] **Session Management**: Session.setCurrentUser() works correctly
- [ ] **Navigation**: Home page navigation after login/signup works
- [ ] **User Roles**: Teacher/Student distinction preserved
- [ ] **Error Handling**: Original error handling logic preserved

### **Controller Compatibility**
- [ ] **LoginController**: All existing methods work with new UI
- [ ] **SignUpController**: Enhanced with email field but backward compatible
- [ ] **FXML Integration**: fx:id mappings preserved for existing fields

## Known Issues and Limitations

### **Design Decisions**
1. **Username vs Email**: Database stores email only, but UI shows both for prototype compliance
2. **Field Mapping**: `useremailField` in SignUp now represents username, `emailField` represents email
3. **Backward Compatibility**: Existing code using `useremailField` still works

### **Future Enhancements**
1. **Password Strength**: Could add password strength indicators
2. **Email Verification**: Could add email verification step
3. **Remember Me**: Could add remember login functionality
4. **Social Login**: Could integrate with social authentication

## Success Criteria Checklist

- [ ] ✅ **Visual Accuracy**: Pages exactly match provided prototypes
- [ ] ✅ **Design Compliance**: Uses exact colors and typography from style guide
- [ ] ✅ **Functional Preservation**: All existing login/signup functionality works unchanged
- [ ] ✅ **Database Integration**: Maintains seamless database connectivity
- [ ] ✅ **User Experience**: Smooth, intuitive interface with proper feedback
- [ ] ✅ **Code Quality**: Clean, maintainable code following JavaFX best practices
- [ ] ✅ **Cross-Platform**: Works consistently across different operating systems
- [ ] ✅ **Accessibility**: Proper keyboard navigation and screen reader support

## Debugging Tips

### **Common Issues**
1. **CSS Not Loading**: Check stylesheet path in FXML files
2. **Navigation Fails**: Verify FXML file paths in controllers
3. **Database Errors**: Check database connection and table structure
4. **Styling Issues**: Check CSS class names match FXML styleClass attributes

### **Console Output to Monitor**
- "Login successful: [email]" - Successful login
- "Signup successfully: [email]" - Successful signup
- "Username (display): [username], Email (database): [email]" - SignUp field mapping
- Database connection messages
- Error messages for failed operations

## Testing Environment

### **Required Components**
- Java 21+
- JavaFX 21+
- MySQL database (or configured database)
- Existing users in database for login testing

### **Test Data**
- Existing users: demo@example.com/demo, admin@example.com/1234
- Test emails for signup: Use unique emails for each test run
- Invalid test data: Empty fields, invalid emails, duplicate emails

---

**Last Updated**: December 2024  
**Version**: 2.0 (UI Improvements)  
**Tested Platforms**: Windows, macOS, Linux

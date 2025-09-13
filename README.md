# Interactive Quiz Creator

A JavaFX-based desktop application designed to support teaching and learning through interactive quiz creation, delivery, and performance tracking.

## Project Overview

Interactive Quiz Creator is a comprehensive educational tool that enables educators and tutors to build, manage, and deliver quizzes to students. The application provides a complete learning ecosystem with quiz creation tools, performance tracking, automated feedback, and review systems.

### Purpose

1. **Create & Take Quizzes** - Build comprehensive question banks and deliver interactive quizzes
2. **Review Incorrect Answers** - Provide targeted practice and remediation
3. **Plan Schedules** - Lightweight calendar integration for quiz and review scheduling

## User Roles & Permissions

### **Tutor/Educator**
- Create, edit, and upload questions (text/images/math)
- Build and publish quizzes with customizable settings
- View student performance analytics
- Manage review schedules and assignments
- Access comprehensive reporting tools

### **Student**
- Take assigned quizzes
- View results and feedback
- Access review materials for incorrect answers
- Track personal progress and performance

## Core Features

### A. Question & Quiz Management
- **Question Bank**: Create items with text, images, and mathematical content
- **Tagging System**: Organize questions by topic and difficulty level
- **Quiz Builder**: Select questions and create customized quizzes
- **Publishing Options**: Assign to individual students or entire classes
- **Flexible Settings**: Configure time limits, deadlines, and difficulty levels

### B. Performance Tracking & Review
- **Automatic Error Tracking**: Monitor incorrect answers per student and question
- **Analytics Dashboard**: View class-wide performance and individual progress
- **Review Sets**: Generate targeted practice sessions for incorrect answers
- **Retry Functionality**: Allow students to retake problematic questions
- **Detailed Reporting**: Track attempt history and time spent

### C. Schedule Planning
- **Calendar Integration**: Simple event management for quizzes and reviews
- **Auto-Event Creation**: Automatic scheduling when publishing quizzes
- **Review Assignments**: Schedule targeted review sessions

## Technical Requirements

### Prerequisites
- Java 11 or higher
- JavaFX SDK
- IDE with JavaFX support (IntelliJ IDEA, Eclipse, etc.)

### Database
- User information storage
- Quiz and question bank management
- Performance tracking and analytics
- Attempt history and progress data

### Supported Question Types
- Multiple Choice (4 options, 1 correct answer)
- True/False
- Short Answer
- *Additional types planned for future releases*

## Key Application Screens

### For Tutors
- **Dashboard**: Recent publishes, reviews, and performance charts
- **Question Bank**: Create, edit, and manage questions with tagging
- **Quiz Setup**: Select questions and configure quiz parameters
- **Analytics Report**: View performance by student/topic with review options

### For Students
- **Dashboard**: Today's assignments and recent scores
- **Quiz Interface**: Clean, intuitive quiz-taking experience
- **Review Screen**: Targeted practice for incorrect answers
- **Progress Tracking**: Personal performance analytics

### Shared Features
- **Authentication**: Secure login/signup with role-based access
- **Calendar View**: Schedule overview for quizzes and reviews

## Development Team

- **Jebeom Yeon** - Project Coordination & Development
- **Luqmaan Baichoo** - Planner Page Development
- **Chun-Huan Lee** - Core UI/UX Development
- **Mitchell Tran** - Review Page Development
- **Akira Hasuo** - Backend Developer & Database Design

## Database Setup

To initialize the project database, use the provided `db/init.sql` script.

On Windows:
  ```bash
  Get-Content .\db\init.sql | mysql -u root -p
  ```
  or
  ```bash
  Get-Content .\db\init.sql -Raw | & "your-local-path-to-mysql.exe" -u root -p
  ex:  Get-Content .\db\init.sql -Raw | & "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p    
  ```
On macOS / Linux:
  ```bash
  mysql -u root -p < db/init.sql
  ```

This makes Database called ```cab302_quizapp``` where user ```appuser@localhost``` password ```AppPass#2025``` are used to login.

## Component

See more detail in [NavBar-README](docs/NavBar-README.md)

## Quiz Result Page Integration Guide

See more detail in [QuizResultIntegration-README](docs/QuizResultIntegration-README.md)

---

## Current Development Status

**Checkpoint 1 (Week 2 - 5) - Project Setup & Planning**
- [x] Team formation and role assignment
- [x] Project concept finalization
- [x] Initial UI/UX mockups created
- [x] Technical requirements analysis
- [x] Repository setup

**Checkpoint 2 (Week 6 - 7) - Project Setup & Progress**
- [x] Database design implementation
- [x] Core authentication system
- [x] Project Management Setup
- [X] Feature Research
- [X] Medium fidelity prototypes
- [X] User Story Creation

---

**CAB302 - Software Development Project**  
*Teaching and Learning Theme*  
Queensland University of Technology

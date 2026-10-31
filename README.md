# QualityHub – Full-Stack QA Automation Platform

QualityHub is a full-stack task management application built to demonstrate software development, API testing, and browser-based UI automation.

The project combines a React frontend, Spring Boot REST API, and automated testing framework using REST Assured, Playwright, and JUnit 5. Allure is integrated to generate visual test execution reports.

## Tech Stack

**Frontend**
- React
- Vite
- JavaScript

**Backend**
- Java 21
- Spring Boot
- JDBC
- H2 Database
- PostgreSQL driver support

**Test Automation**
- REST Assured
- Playwright
- JUnit 5
- Maven
- Allure Reports

## Project Structure

```text
QualityHub/
├── .github/
│   └── workflows/
├── automation/
│   ├── src/test/
│   │   ├── java/com/qualityhub/
│   │   │   ├── api/
│   │   │   └── ui/
│   │   └── resources/
│   └── pom.xml
├── backend/
│   ├── src/
│   └── pom.xml
├── frontend/
│   ├── src/
│   ├── package.json
│   └── vite.config.js
└── README.md
```

## Application Features

- Create and manage tasks
- Assign task priorities: LOW, MEDIUM, HIGH
- Update task statuses: TODO, IN_PROGRESS, DONE
- Search and filter tasks
- View task statistics
- Validate required task fields

## Automated Testing

The automation framework includes API and UI tests.

**API Testing – REST Assured**
- Verify successful retrieval of tasks
- Verify 404 responses for invalid task IDs

**UI Testing – Playwright**
- Verify dashboard loading
- Verify task creation
- Verify empty-title validation
- Verify task status updates

## Test Results

Latest local execution:

| Test Suite | Tests | Passed | Failed |
|---|---:|---:|---:|
| API Tests | 2 | 2 | 0 |
| UI Tests | 4 | 4 | 0 |
| **Total** | **6** | **6** | **0** |

Allure reporting is used to generate visual test execution reports.

## Running the Application

### 1. Start the Backend

```bash
cd backend
mvn spring-boot:run
```

Backend API:

`http://localhost:8080/api/tasks`

### 2. Start the Frontend

Open another terminal:

```bash
cd frontend
npm install
npm run dev
```

Frontend:

`http://localhost:5173`

## Running Automated Tests

Make sure the backend and frontend are running before executing the automation suite.

Open another terminal:

```bash
cd automation
mvn clean test
```

## Generate Allure Report

```bash
mvn allure:report
```

To view the report, serve the generated folder locally:

```bash
python -m http.server 8000 --directory target/site/allure-maven-plugin
```

Then open:

`http://localhost:8000`

## Purpose

This project was developed as a practical demonstration of full-stack application development, REST API validation, browser automation, automated test reporting, and quality assurance practices.
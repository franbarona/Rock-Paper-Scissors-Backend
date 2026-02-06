# Rock Paper Scissors - Backend

Backend API for Rock Paper Scissors game built with Spring Boot and SQLite.

## Features

- User authentication with JWT tokens
- Game logic and move validation
- Game statistics and history tracking
- Persistent data storage with SQLite
- Comprehensive error handling
- RESTful API with standardized responses

## Tech Stack

- Java 21
- Spring Boot 3.5.10
- SQLite
- Maven 3.9
- JWT Authentication
- SLF4J Logging
- JUnit & Mockito (Testing)

## Prerequisites

- Java 21 or higher
- Maven 3.9+
- Git

## Getting Started

### Installation

```bash
# Clone the repository
git clone 

# Navigate to backend directory
cd backend

# Install dependencies
./mvnw clean install
```

### Configure environment variables

Copy the example file:

```bash
cp .env.example .env
```

Then create and edit `.env` with your values.

Copy the example file or create it manually:

```bash
cp .env.example .env
```

Update `.env` with your values. **Generate a secure JWT_SECRET**

### Running the Application

```bash
# Run the Spring Boot application
./mvnw spring-boot:run

# The server will start on http://localhost:8080
```

### Running Tests

```bash
# Run all tests
./mvnw test

# Run a specific test class
./mvnw test -Dtest=AuthServiceTest
```

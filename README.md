# Rock-Paper-Scissors-Backend
Backend API for Rock Paper Scissors game built with Spring Boot and SQLite.

## Technologies
- **Java 17**
- **Maven 3.9**
- **SQLite** (automatically included)

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/franbarona/Rock-Paper-Scissors-Backend.git
cd rps-backend
```

### 2. Configure environment variables

Copy the example file:

```bash
cp .env.example .env
```

Then create and edit `.env` with your values. **Generate a secure JWT_SECRET:**

### 3. Build the project

```bash
./mvnw clean install -U
```

### 4. Run the application

```bash
./mvnw spring-boot:run
```

The API will be available at: **http://localhost:8080**
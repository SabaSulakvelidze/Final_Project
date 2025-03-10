# Music Management Service

A **Spring Boot application** for managing music records, built using **Spring Data JPA**, **Spring MVC**, and **Jakarta EE**.

This application enables CRUD operations on music records and offers pagination, filtering, and exception handling.

---

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Setup and Installation](#setup-and-installation)
- [Usage](#usage)
- [Testing](#testing)
- [API Endpoints](#api-endpoints)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

---

## Features

- **Create, Read, Update, Delete (CRUD)** operations for music, album and playlist entities.
- **Pagination and Filtering** for fetching music records using `Specification`.
- **Custom Exception Handling** with meaningful error messages.
- **Transaction Management** for database consistency.
- Unit-tested with **JUnit 5** and **Mockito**.
  
---

## Technologies Used

**Backend:**
- Java 21
- Spring Boot
- Spring Data JPA
- Spring MVC
- Jakarta EE

**Database:**
- Hibernate (as JPA implementation)
- PostgreSQL (for testing and development)

**Build Tool:**
- Maven

**Testing:**
- JUnit 5
- Mockito

---

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 21**
- **Maven** (version 3.8+)
- **Git**

---

## Setup and Installation

### Clone the Repository

```bash
git clone https://github.com/SabaSulakvelidze/Final_Project.git
```

### Build the Project

```bash
mvn clean install
```

### Run the Application

```bash
mvn spring-boot:run
```

The application starts on port **8080** by default. You can access it at:  
[http://localhost:8080](http://localhost:8080)

---

## Usage

### Configuring the Database

To switch to a different database (e.g., PostgreSQL, MySQL), update the `application.yml` file:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/Spotify_Project
spring.datasource.username=yourUsername
spring.datasource.password=yourPassword
spring.jpa.hibernate.ddl-auto=create/update
```

---

## Testing

The application is comprehensively tested using **JUnit 5** and **Mockito**.

### Run All Tests

```bash
mvn test
```

---

## API Endpoints

### 1. **Sign Up a New User**
**POST** `/auth/signUp`  
Registers a new user. Admin users must be added manually.

- **Request Body**:
  ```json
  {
      "username": "user@example.com",
      "password": "securePassword123",
      "userRole": "LISTENER"
  }
  ```
- **Responses**:
   - `201 Created`: User successfully created.
   - `403 Forbidden`: When attempting to create an admin user.

---

### 2. **Verify Email**
**POST** `/auth/verify`  
Verifies a user's email address with a verification code, which will be sent to indicated email.

- **Query Params**:  
  `userEmail`, `verificationCode`.
- **Response**: Success confirmation (`200 OK`).

---

### 3. **Log In**
**POST** `/auth/signIn`  
Logs in a user with their username and password.

- **Query Params**:  
  `username` (email/username), `password` (user's password).
- **Response**: Success message or token (`200 OK`).

---

### 4. **Resend Verification Code**
**GET** `/auth/resendVerificationCode`  
Resends a verification code to the user's email.

- **Query Param**:  
  `userEmail`.
- **Response**: Confirmation (`200 OK`).

### bonus
**GET** `/stats/getStatistics`
This endpoint is created to test scheduler without changing configurations.

---

## Project Structure

Below is the typical structure of the project:

1. **Controller Layer** (`controller/`):
    - This layer contains the REST controllers where incoming HTTP requests are processed, mapped to appropriate service methods, and responses are generated.
    - Example: `MusicController` could handle endpoints like `/api/music`.

2. **Service Layer** (`service/`):
    - This layer contains the business logic of the application.
    - The `MusicService` processes the core functionality such as saving, updating, and retrieving music records while interacting with the repository layer.

3. **Repository Layer** (`repository/`):
    - This layer is responsible for database interactions.
    - Using Spring Data JPA, interfaces like `MusicRepository` provide methods for accessing the `MusicEntity` data from the database.

4. **Model Layer** (`model/entity/`):
    - This layer contains the **entities**, which map to database tables using JPA annotations.
    - Example: `MusicEntity` defines the structure and relationships of the `Music` table.

5. **Exception Handling** (`exception/`):
    - This directory contains custom exceptions and centralized handling for errors, such as a `CustomException` for handling specific error scenarios with HTTP status codes.

6. **Test Directory** (`src/test/java/`):
    - Contains unit tests for various layers of the application, primarily focusing on the **service layer** functionality.
    - Uses **JUnit 5** and **Mockito** for mocking dependencies and writing isolated tests.


## Contributing

Contributions are welcome! To contribute:

1. Fork the repository.
2. Create a feature branch (`git checkout -b feature-name`).
3. Commit your changes (`git commit -m "Add new feature"`).
4. Push to the branch (`git push origin feature-name`).
5. Open a pull request.

---

## License

This project is licensed under the **MIT License**. See the `LICENSE` file for details.

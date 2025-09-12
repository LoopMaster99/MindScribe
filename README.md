# MindScribe: A Secure Journaling API

**MindScribe** is a robust and secure backend service for a journaling application, built with Java and the Spring Boot framework. It provides a complete set of RESTful APIs for user management and CRUD operations on journal entries, secured with modern authentication practices.

## ✨ Features

*   **Secure User Authentication:** Implemented a robust authentication and authorization system using Spring Security and a custom UserDetailsService.
*   **User Management:** Endpoints for user registration and login.
*   **Full CRUD Functionality:** Create, Read, Update, and Delete operations for journal entries.
*   **RESTful API Design:** A well-structured API for easy integration with any frontend.
*   **Configuration Management:** Environment-specific configurations using Spring Profiles (`dev` and `prod`).
*   **CI/CD Pipeline:** Automated build and testing pipeline using GitHub Actions.

## 🛠️ Technologies Used

*   **Backend:** Java, Spring Boot
*   **Security:** Spring Security, JWT
*   **Database:** Spring Data, MongoDB
*   **Build Tool:** Apache Maven
*   **Testing:** JUnit 5, Mockito
*   **Code Quality:** SonarQube (for static analysis), JaCoCo (for coverage)
*   **CI/CD:** GitHub Actions

## 🚀 Getting Started

### Prerequisites

*   Java JDK 17 or later
*   Apache Maven
*   A running instance of MongoDB

### Installation & Setup

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/LoopMaster99/MindScribe
    cd MindScribe
    ```

2.  **Configure the application:**
    Open `src/main/resources/application-dev.yml` and update the MongoDB connection details.
    ```yaml
    spring:
      data:
        mongodb:
          uri: mongodb://localhost:27017/journal_db

    ```

3.  **Build the project:**
    ```sh
    mvn clean install
    ```

4.  **Run the application:**
    The application will start on `http://localhost:8080`. Use the `dev` profile to connect to your local database.
    ```sh
    mvn spring-boot:run -Dspring-boot.run.profiles=dev
    ```

## 🧪 Testing & Code Quality

This project uses JUnit 5 and Mockito for unit testing and JaCoCo for measuring code coverage.

1.  **Run the test suite:**
    ```sh
    mvn test
    ```

2.  **View Coverage Report:**
    After running the tests, the code coverage report can be found in `target/site/jacoco/index.html`. Your project also includes a pre-generated report in the `htmlReport/` directory.

## 📄 API Endpoints

The API is structured into public, user-specific, journal, and admin routes. Access to user and journal routes requires authentication.

| Method             | Endpoint                   | Description                                             |
|:-------------------|:---------------------------|:--------------------------------------------------------|
| **Public Routes**  |                            |                                                         |
| `GET`              | `/public/health-check`     | Checks if the application is running.                   |
| `POST`             | `/public/create-user`      | Registers a new standard user.                          |
| **User Routes**    |                            |                                                         |
| `PUT`              | `/user`                    | Updates the authenticated user's username and password. |
| `DELETE`           | `/user`                    | Deletes the authenticated user's account.               |
| **Journal Routes** |                            |                                                         |
| `GET`              | `/journal`                 | Gets all journal entries for the authenticated user.    |
| `POST`             | `/journal`                 | Creates a new journal entry for the authenticated user. |
| `GET`              | `/journal/id/{myId}`       | Gets a specific journal entry by its ID.                |
| `PUT`              | `/journal/id/{id}`         | Updates a specific journal entry by its ID.             |
| `DELETE`           | `/journal/id/{myId}`       | Deletes a specific journal entry by its ID.             |
| **Admin Routes**   |                            |                                                         |
| `GET`              | `/admin/all-users`         | Retrieves a list of all users in the system.            |
| `POST`             | `/admin/create-admin-user` | Creates a new user                                      |
# MindScribe: A Secure Journaling API

**MindScribe** is a robust and secure backend service for a journaling application, built with Java and the Spring Boot framework. It provides a complete set of RESTful APIs for user management and CRUD operations on journal entries, secured with modern authentication practices.

## ✨ Features

- **Secure User Authentication:** Implemented a robust authentication and authorization system using Spring Security and a custom UserDetailsService.
- **User Management:** Endpoints for user registration, updates, and deletion.
- **Full CRUD Functionality:** Create, Read, Update, and Delete operations for journal entries.
- **RESTful API Design:** A well-structured API for easy integration with any frontend.
- **Sentiment Analysis:** Weekly email summaries of the user's most frequent sentiment.
- **Weather Integration:** Greeting endpoint that includes the current weather.
- **Redis Caching:** Caching of weather data to improve performance.
- **Configuration Management:** Environment-specific configurations using application properties.
- **CI/CD Pipeline:** Automated build and testing pipeline using GitHub Actions.

## 🛠️ Technologies Used

- **Backend:** Java, Spring Boot
- **Security:** Spring Security
- **Database:** Spring Data, MongoDB
- **Caching:** Redis
- **Build Tool:** Apache Maven
- **Testing:** JUnit 5, Mockito
- **Code Quality:** SonarQube (for static analysis)
- **CI/CD:** GitHub Actions

## 🚀 Getting Started

### Prerequisites

- Java JDK 17 or later
- Apache Maven
- A running instance of MongoDB
- A running instance of Redis

### Installation & Setup

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/your-username/journal-app
    cd journal-app
    ```

2.  **Configure the application:**
    Open `src/main/resources/application.properties` and update the MongoDB and Redis connection details, as well as the weather API key.
    ```properties
    spring.data.mongodb.uri=mongodb://localhost:27017/journal_db
    spring.redis.host=localhost
    spring.redis.port=6379
    weather.api.key=your_weather_api_key
    ```

3.  **Build the project:**
    ```sh
    mvn clean install
    ```

4.  **Run the application:**
    The application will start on `http://localhost:8080`.
    ```sh
    mvn spring-boot:run
    ```

## 🧪 Testing & Code Quality

This project uses JUnit 5 and Mockito for unit testing and JaCoCo for measuring code coverage.

1.  **Run the test suite:**
    ```sh
    mvn test
    ```

2.  **View Coverage Report:**
    After running the tests, the code coverage report can be found in `target/site/jacoco/index.html`.

## 🔧 Recent Code Improvements

The codebase has been enhanced with the following improvements for better security, maintainability, and production-readiness:

### Security Enhancements
- **JWT Secret Externalized**: Moved hardcoded JWT secret to environment variables for better security
- **Debug Header Removed**: Eliminated production debug headers that exposed sensitive information
- **Password Protection**: Added `@JsonIgnore` and `@ToString.Exclude` to prevent password exposure in logs/responses
- **Enhanced Authentication**: Improved JWT filter with proper null checks and exception handling

### Data Validation
- **Input Validation**: Added Jakarta validation annotations (`@NotBlank`, `@Email`, `@Size`) to all entities
- **Email Validation**: Enforced proper email format validation for user registration

### Code Quality
- **Constructor Injection**: Replaced field injection with constructor injection across all services for better testability
- **Custom Exceptions**: Created `EmailSendException` for more specific error handling
- **Consistent Logging**: Replaced `System.out` with SLF4J logger throughout the codebase
- **Null Safety**: Added null checks in critical service methods to prevent NullPointerExceptions

### Performance & Reliability
- **Custom Repository Methods**: Added query methods for date-based and sentiment-based filtering
- **Error Handling**: Implemented comprehensive try-catch blocks in WeatherService and RedisService
- **Redis Null Checks**: Added null validation before cache deserialization to prevent errors

### Architecture
- **Repository Annotations**: Added `@Repository` to custom repository implementations
- **Code Cleanup**: Removed legacy test files (Y_*, Z_* prefixed classes)
- **Health Monitoring**: Added `/actuator/health` endpoint to monitor MongoDB and Redis connectivity

### Configuration
- **Configuration Template**: Created `application.properties.example` for easy environment setup
- **Configurable JWT**: Made JWT expiration time configurable via properties

## 📄 API Endpoints

The API is structured into public, user-specific, journal, and admin routes. Access to user and journal routes requires authentication.

| Method             | Endpoint                   | Description                                             |
|:-------------------|:---------------------------|:--------------------------------------------------------|
| **Public Routes**  |                            |                                                         |
| `GET`              | `/public/health-check`     | Checks if the application is running.                   |
| `POST`             | `/public/create-user`      | Registers a new standard user.                          |
| **User Routes**    |                            |                                                         |
| `GET`              | `/user`                    | Returns a greeting with the current weather.            |
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
| `POST`             | `/admin/create-admin-user` | Creates a new user with admin privileges.               |
| `GET`              | `/admin/clear-app-cache`   | Clears the application cache.                           |

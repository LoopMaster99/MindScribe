# MindScribe: A Secure Journaling API

**MindScribe** is a robust and secure backend service for a journaling application, built with Java and Spring Boot. It provides RESTful APIs for user management, journal entry CRUD operations, sentiment analysis, and weather integration, all secured with JWT-based authentication.

## ✨ Features

- **JWT Authentication**: Secure token-based authentication with configurable expiration
- **User Management**: Registration, login, profile updates, and account deletion
- **Journal Entry CRUD**: Full create, read, update, and delete operations for journal entries
- **Sentiment Analysis**: Weekly email summaries based on user's most frequent sentiment
- **Weather Integration**: Real-time weather data in user greetings
- **Redis Caching**: Performance optimization through weather data caching
- **Kafka Messaging**: Asynchronous sentiment email delivery via Kafka
- **Health Monitoring**: Endpoint to check MongoDB and Redis connectivity
- **Input Validation**: Jakarta validation for all user inputs
- **Scheduled Tasks**: Automated weekly sentiment analysis and cache refresh

## 🛠️ Technologies Used

- **Backend Framework**: Spring Boot 3.4.7, Java 17
- **Security**: Spring Security with JWT (JSON Web Tokens)
- **Database**: MongoDB with Spring Data MongoDB
- **Caching**: Redis
- **Messaging**: Apache Kafka
- **Email**: Spring Mail (JavaMailSender)
- **Build Tool**: Apache Maven
- **Testing**: JUnit 5, Mockito
- **Code Quality**: SonarQube, Lombok
- **Authentication**: Custom UserDetailsService with BCrypt password encoding
- **CI/CD**: GitHub Actions

## 🚀 Getting Started

### Prerequisites

- **Java JDK 17** or later
- **Apache Maven** 3.6+
- **MongoDB** (running instance or MongoDB Atlas)
- **Redis** (running instance)
- **Apache Kafka** (optional, for sentiment emails)
- **Weather API Key** from [WeatherStack](https://weatherstack.com/) or similar

### Installation & Setup

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/LoopMaster99/MindScribe.git
    cd MindScribe
    ```

2.  **Configure the application:**
    
    Create `src/main/resources/application.yml` based on the provided template:
    
    ```yaml
    # JWT Configuration (REQUIRED)
    jwt:
      secret: your-secret-key-must-be-at-least-32-characters-long
      expiration: 3600000  # 1 hour in milliseconds
    
    # Weather API
    weather:
      api:
        key: your_weather_api_key_here
    
    # MongoDB
    spring:
      data:
        mongodb:
          uri: mongodb://localhost:27017/journalDB
      
      # Redis
      redis:
        host: localhost
        port: 6379
      
      # Mail Configuration
      mail:
        host: smtp.gmail.com
        port: 587
        username: your_email@gmail.com
        password: your_app_password
        properties:
          mail:
            smtp:
              auth: true
              starttls:
                enable: true
      
      # Kafka
      kafka:
        bootstrap-servers: localhost:9092
    
    # Server
    server:
      port: 8080
    ```
    
    > **Note**: See `application.properties.example` for a complete configuration template.

3.  **Build the project:**
    ```sh
    mvn clean install
    ```

4.  **Run the application:**
    ```sh
    mvn spring-boot:run
    ```
    
    The application will start on `http://localhost:8080`

## 🧪 Testing & Code Quality

This project uses JUnit 5 and Mockito for unit testing.

1.  **Run the test suite:**
    ```sh
    mvn test
    ```

2.  **Run with coverage:**
    ```sh
    mvn clean test jacoco:report
    ```
    
    View the coverage report at `target/site/jacoco/index.html`

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

### Public Routes (No Authentication Required)

| Method | Endpoint              | Description                                    |
|:-------|:----------------------|:-----------------------------------------------|
| `GET`  | `/public/health-check`| Simple health check - returns "Ok"             |
| `POST` | `/public/signup`      | Register a new user                            |
| `POST` | `/public/login`       | Login and receive JWT token                    |

### User Routes (Authentication Required)

| Method   | Endpoint | Description                                           |
|:---------|:---------|:------------------------------------------------------|
| `GET`    | `/user`  | Get greeting with current weather for user's location |
| `PUT`    | `/user`  | Update authenticated user's profile                   |
| `DELETE` | `/user`  | Delete authenticated user's account                   |

### Journal Routes (Authentication Required)

| Method   | Endpoint             | Description                                  |
|:---------|:---------------------|:---------------------------------------------|
| `GET`    | `/journal`           | Get all journal entries for authenticated user |
| `POST`   | `/journal`           | Create a new journal entry                   |
| `GET`    | `/journal/id/{myId}` | Get a specific journal entry by ID           |
| `PUT`    | `/journal/id/{id}`   | Update a specific journal entry by ID        |
| `DELETE` | `/journal/id/{myId}` | Delete a specific journal entry by ID        |

### Admin Routes (Admin Role Required)

| Method | Endpoint                   | Description                          |
|:-------|:---------------------------|:-------------------------------------|
| `GET`  | `/admin/all-users`         | Retrieve list of all users           |
| `POST` | `/admin/create-admin-user` | Create a new user with admin role    |
| `GET`  | `/admin/clear-app-cache`   | Clear application configuration cache |

### Health & Monitoring

| Method | Endpoint           | Description                                    |
|:-------|:-------------------|:-----------------------------------------------|
| `GET`  | `/actuator/health` | Check MongoDB and Redis connectivity status    |

## 🔐 Authentication

All authenticated endpoints require a JWT token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

To obtain a token:
1. Register via `/public/signup`
2. Login via `/public/login` to receive your JWT token
3. Include the token in subsequent requests

## 📦 Project Structure

```
src/main/java/com/developmentprep/journalApp/
├── api/              # External API response models
├── cache/            # Application caching logic
├── config/           # Spring configuration (Security, Redis)
├── constants/        # Application constants
├── controller/       # REST API controllers
├── entity/           # MongoDB entities (User, JournalEntry)
├── enums/            # Enumerations (Sentiment)
├── exception/        # Custom exceptions
├── filter/           # JWT authentication filter
├── model/            # Data transfer objects
├── repository/       # MongoDB repositories
├── scheduler/        # Scheduled tasks (sentiment analysis)
├── service/          # Business logic layer
└── utils/            # Utility classes (JWT)
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📝 License

This project is open source and available under the MIT License.

## 👤 Author

**Krishna Bansal**
- GitHub: [@LoopMaster99](https://github.com/LoopMaster99)
- Repository: [MindScribe](https://github.com/LoopMaster99/MindScribe)

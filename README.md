# Movie Director Service

A Spring Boot application that provides an API to fetch movie directors with a minimum number of movies directed, sourced from an external API.

## Features

- Fetch movies from an external API.
- Calculate and filter directors based on the number of movies directed.
- RESTful API endpoint to query directors meeting a specific threshold.
- Structured project with controllers, services, and models.
- Lombok and SLF4J integrated for cleaner code and logging.

## Technologies Used

- **Spring Boot 3.4.2**: Framework for building Java applications.
- **Lombok**: Reduces boilerplate code by generating getters, setters, and logging.
- **SLF4J & Logback**: For application logging.
- **Maven**: Dependency management and project build tool.

## Prerequisites

- **Java 17**: Ensure Java 17 is installed on your machine.
- **Maven**: For building and running the application.
- **Git**: For version control.

## Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/<your-username>/MovieDirectorService.git
cd MovieDirectorService
```

### 2. Build the Project
```bash
mvn clean install
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

### 4. Access the API
Once the application is running, access the following endpoint:

#### **Endpoint:** `/api/directors`
- **Method:** GET
- **Parameters:**
    - `threshold` (int): Minimum number of movies directed by a director.

Example:
```
http://localhost:8080/api/directors?threshold=3
```

### 5. Test the Application
Run the tests:
```bash
mvn test
```

## Project Structure

```
src/main/java/com/example/moviedirectorservice
├── controller
│   └── DirectorController.java       # Handles incoming API requests.
├── service
│   └── DirectorService.java          # Business logic for processing movie data.
├── model
│   ├── Movie.java                    # Represents a movie entity.
│   └── MovieResponse.java            # Represents the API response structure.
├── config
│   └── RestTemplateConfig.java       # Configures RestTemplate for HTTP requests.
└── application
    └── MovieDirectorServiceApplication.java  # Application entry point.
```

## Configuration

The application uses an `application.yml` file for configuration.

#### **application.yml**
```yaml
server:
  port: 8080

spring:
  application:
    name: movie-director-service

movie-api:
  base-url: https://eron-movies.wiremockapi.cloud/api/movies/search
```

## Logging

- **Debug Logs**: Logs detailed information about API calls and processing.
- **Info Logs**: Highlights application progress and results.

Logs can be customized in the `application.yml` file:
```yaml
logging:
  level:
    com.example.moviedirectorservice: DEBUG
```

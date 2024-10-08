# Assignment Project Documentation

## Overview

This project is a Spring Boot application designed to manage users, transactions, and Bitcoin pricing. The application is built using Java 21, Spring Boot, PostgreSQL, and Redis, providing a robust backend service. This documentation provides setup instructions, environment details, and references for developers working with this project.

## Prerequisites

Before you begin, ensure you have the following software installed:

- **JDK 21.0.4** or later
- **Apache Maven 3.9.8** or later
- **PostgreSQL 16+**
- **Redis**
- **Docker** (optional, for containerized database setup)

## Initial Setup

### 1. Clone the Repository

Start by cloning the repository to your local machine:

```bash
git clone https://github.com/ChunHisangLee/assignment.git
cd assignment
```

### 2. Configure the Database

The application uses PostgreSQL as the primary database and Redis for caching. You need to configure the database connections in the application.yml or application.properties file.
#### PostgreSQL Configuration in application.yml:

```yaml
server:
  port: 8080

spring:
  profiles:
    active: default

# Default profile (local development and testing)
---
spring:
  config:
    activate:
      on-profile: default
  datasource:
    url: jdbc:postgresql://localhost:5432/postgres
    username: postgres
    password: Ab123456
    driver-class-name: org.postgresql.Driver
    hikari:
      auto-commit: true
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
  data:
    redis:
      host: localhost
      port: 6379
      password: Ab123456

logging:
  level:
    com.example.assignment: INFO

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui-custom.html

initial:
  price: 100

app:
  jwtSecret: Xb34fJd9kPbvmJc84mDkV9b3Xb4fJd9kPbvmJc84mDkV9b3Xb34fJd9kPbvmJc84
  jwtExpirationMs: 3600000

security:
  authentication:
    enabled: false

# Docker profile (for running in containers)
---
spring:
  config:
    activate:
      on-profile: docker
  datasource:
    url: jdbc:postgresql://db:5432/postgres
    username: postgres
    password: Ab123456
    driver-class-name: org.postgresql.Driver
    hikari:
      auto-commit: true
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
  data:
    redis:
      host: redis
      port: 6379
      password: Ab123456

logging:
  level:
    com.example.assignment: INFO

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui-custom.html

initial:
  price: 100

app:
  jwtSecret: Xb34fJd9kPbvmJc84mDkV9b3Xb4fJd9kPbvmJc84mDkV9b3Xb34fJd9kPbvmJc84
  jwtExpirationMs: 3600000

security:
  authentication:
    enabled: false
```

### 3. Create the PostgreSQL Database

If you haven't already, create the database in PostgreSQL:

```sql
CREATE DATABASE postgres;
```
Initialize the database with the following table scripts:
* [schema.sql](src/main/resources/SQL/schema.sql)

### 4. Build and Run the Application

You can run the application using Maven:

```bash
mvn spring-boot:run
```

Or build the project and run the JAR file:

```bash
mvn clean package
java -jar target/assignment-0.0.1-SNAPSHOT.jar
```

### 5. Access the Application

You can access the application and its API documentation via the following link:

* Swagger UI: http://localhost:8080/swagger-ui/index.html

### 6. Docker Setup (Optional)

If you prefer using Docker for PostgreSQL and Redis, use the following commands:
```bash
docker run --name assignment-db -e POSTGRES_DB=postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=Ab123456 -p 5432:5432 -d postgres
docker run --name assignment-redis -p 6379:6379 -d redis
```

### 7. Docker Compose Setup

You can also use Docker Compose to run the entire stack (Spring Boot application, PostgreSQL, and Redis) together. Create a docker-compose.yml file with the following content:

```yaml
version: '3.8'

services:
  app:
    image: chunhsianglee/assignment:latest
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/postgres
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=Ab123456
      - SPRING_REDIS_HOST=redis
      - SPRING_REDIS_PORT=6379
      - APP_JWTSECRET=Xb34fJd9kPbvmJc84mDkV9b3Xb4fJd9kPbvmJc84mDkV9b3Xb34fJd9kPbvmJc84
      - APP_JWTEXPIRATIONMS=3600000
    depends_on:
      - db
      - redis

  db:
    image: postgres:16
    environment:
      POSTGRES_DB: postgres
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: Ab123456
    ports:
      - "5432:5432"
    healthcheck:
      test: ["CMD", "pg_isready", "-U", "postgres"]
      interval: 30s
      timeout: 10s
      retries: 5

  redis:
    image: redis:latest
    ports:
      - "6379:6379"
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
      interval: 30s
      timeout: 10s
      retries: 5

networks:
  default:
    name: assignment-network
```

To build and run the stack using Docker Compose, run:

```bash
docker-compose up --build
```

### 8. Docker Image Information

The Docker image for this project is available on Docker Hub:

- Docker Hub Username: chunhsianglee
- Docker Repository: assignment
- Image Tag: latest

You can pull the image directly from Docker Hub using:

```bash
docker pull chunhsianglee/assignment:latest
```

## API Documentation

The API documentation is automatically generated by Swagger. You can access it through the following link:

* Swagger UI: http://localhost:8080/swagger-ui/index.html

This documentation provides detailed information about the available API endpoints, methods, and responses.

## Application Configuration

### The application includes the following additional configuration:

JWT Settings:
* jwtSecret: A secret key for signing JWTs.
* jwtExpirationMs: The JWT expiration time is set to 3600000 milliseconds (1 hour).
* Initial Price: The initial Bitcoin price is set to 100.
* Security: Authentication is disabled for test (security.authentication.enabled: false).

## Development Environment

The application has been developed and tested with the following tools:

* JDK: Version 21.0.4
* Apache Maven: [version 3.9.8](https://maven.apache.org/download.cgi)
* IDE: [IntelliJ IDEA Ultimate 2024.2](https://www.jetbrains.com/idea/download/?section=windows)
  for development.

## Further Reading and References

For more information, consider exploring the following resources:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
  : A comprehensive guide to the Maven project management tool.

*  [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.4/maven-plugin/reference/html/)
   : Detailed documentation on the Spring Boot Maven Plugin.

*  [Spring Security Documentation](https://docs.spring.io/spring-security/reference/index.html)
   : Detailed documentation on the Spring Security.

## License

This project is licensed under the MIT License. See the LICENSE file for more details.

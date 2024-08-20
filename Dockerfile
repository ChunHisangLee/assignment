# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven build artifact from the target directory to the container
COPY target/assignment-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application will run on
EXPOSE 8080

# Set environment variables (can be overridden in docker-compose.yml)
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/postgres \
    SPRING_DATASOURCE_USERNAME=postgres \
    SPRING_DATASOURCE_PASSWORD=Ab123456 \
    SPRING_REDIS_HOST=redis \
    SPRING_REDIS_PORT=6379 \
    INITIAL_PRICE=100 \
    APP_JWT_SECRET=Xb34fJd9kPbvmJc84mDkV9b3Xb4fJd9kPbvmJc84mDkV9b3Xb34fJd9kPbvmJc84 \
    APP_JWT_EXPIRATION_MS=3600000 \
    SECURITY_AUTHENTICATION_ENABLED=false

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]

# Use Eclipse Temurin Java 17 (replaces deprecated openjdk)
FROM eclipse-temurin:17-jre-jammy

# Set working directory
WORKDIR /app

# Copy jar file
COPY target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run application
CMD ["java", "-jar", "app.jar"]

FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy jar
COPY target/*.jar app.jar

# Expose app port
EXPOSE 8080

# Run app
ENTRYPOINT ["java", "-jar", "app.jar"]

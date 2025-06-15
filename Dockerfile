# Use official OpenJDK 8 Alpine image
FROM openjdk:8-jdk-alpine

# Add a volume for temporary files
VOLUME /tmp

# Create app directory
WORKDIR /app

# Copy JAR file
ARG JAR_FILE=target/account-card-service-1.0.0.jar
COPY ${JAR_FILE} app.jar

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=default

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
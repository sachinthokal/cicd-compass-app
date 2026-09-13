# --- Stage 1: Build the application ---
FROM maven:3.9.16-eclipse-temurin-25-alpine AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file and download dependencies to leverage Docker cache
COPY pom.xml .

# Download dependencies (this will cache them for future builds)
RUN mvn dependency:go-offline

# Copy the source code into the container
COPY src ./src

# Build the application (skip tests for faster build)
RUN mvn clean package -DskipTests

# --- Stage 2: Run the application ---
FROM eclipse-temurin:25-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Create a system group and user, then change ownership
RUN addgroup -S appgroup && adduser -S appuser -G appgroup && chown -R appuser:appgroup /app

# Copy the built jar file from the builder stage
ARG VERSION=1.0.0
COPY --chown=appuser:appgroup --from=builder /app/target/cicd-compass-app-${VERSION}.jar app.jar

# Expose the port (replace with your app's port if different, e.g., 8080)
USER appuser
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
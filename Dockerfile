# --- Stage 1: Build the application ---
FROM maven:3.9.16-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests


# --- Stage 2: Run the application ---
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S appgroup \
    && adduser -S appuser -G appgroup \
    && chown -R appuser:appgroup /app

ARG VERSION=1.0.0

COPY --chown=appuser:appgroup \
    --from=builder \
    /app/target/cicd-compass-app-${VERSION}.jar \
    app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
# Build
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests

RUN echo "Build completed!"

RUN echo "Running in prod profile!"

# Deploy
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/OrderService.jar ./app.jar
EXPOSE 8080
CMD java -jar ./app.jar --spring.profiles.active=prod

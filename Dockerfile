# Step 1: Compilation
FROM maven:3.9-openjdk-21 AS build
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Execution
FROM eclipse-temurin:21-jdk-alpine
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]

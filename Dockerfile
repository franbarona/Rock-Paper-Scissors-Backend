# Step 1: Compilation
FROM maven:3.9-eclipse-temurin-21-alpine AS build
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Execution
FROM eclipse-temurin:21-jre-alpine
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
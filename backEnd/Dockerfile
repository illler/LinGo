# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /lingo
COPY pom.xml .
COPY src ./src
RUN mvn clean install

# Stage 2: Run the application
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /lingo/target/backEnd-0.0.1-SNAPSHOT.jar ./lingo.jar
EXPOSE 8080
CMD ["java", "-jar", "lingo.jar"]
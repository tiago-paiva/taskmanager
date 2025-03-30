FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app
COPY src ./src
COPY pom.xml .

RUN mvn clean install

FROM openjdk:21

COPY --from=build app/target/taskmanager-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
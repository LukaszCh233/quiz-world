#BUILD
FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /app

COPY ./pom.xml .
COPY ./src ./src

RUN mvn clean package -DskipTests

#RUN
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/quiz_world-0.0.1-SNAPSHOT.jar quiz_world-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "/app/quiz_world-0.0.1-SNAPSHOT.jar" ]

FROM openjdk:19
WORKDIR /app
COPY target/quiz_world-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD ["java", "-jar","quiz_world-0.0.1-SNAPSHOT.jar"]

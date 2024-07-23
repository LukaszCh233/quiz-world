# build
FROM openjdk:19-jdk-slim AS build

# Install Maven
RUN apt-get update && \
    apt-get install -y maven

# Create catalog
WORKDIR /app

# Copy the project files to your catalog
COPY pom.xml .
COPY src ./src

# Build an application
RUN mvn clean package -DskipTests

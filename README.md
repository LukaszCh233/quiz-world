# quiz_world 
Quiz World it's My project in Java and Spring Boot where We have two moduls.
Application provide two options for Admin and for User.
Admin who can manage the application and User who uses the application.

**Quiz** - modul with quizzes where We have many functions like, create, modifications, delete, solve quizzes and check your score and other people.

**Words** - modul where We have word sets and We can learn new words. Many functions like create private word sets or for everyone, modifications, delete, solve word sets.

# Technologies used:
- **Java 17**: The programming language used for development.
- **Spring Boot**: Framework for building the application.
    - **Spring Data JPA**: For database interactions.
    - **Spring Security**: For securing the application.
    - **Spring Boot Web**: For building web applications.
    - **Spring Boot Starter Test**: For testing the application.
- **Lombok**: To reduce boilerplate code by generating getters, setters, etc.
- **Hibernate**: ORM framework used with Spring Data JPA.
- **JSON Web Token (JWT)**: For authentication and authorization.
- **PostgreSQL**: The relational database management system used.
- **JUnit**: For unit testing.
- **Spring Data REST WebMVC**: For creating RESTful APIs.
- **Maven**
- **IntelliJ IDEA**
- **Postman**

## Prerequisites
Before you begin, ensure you have the following installed on your machine:
- **Postman**: https://www.postman.com/downloads/
- **Docker**: [Install Docker](https://docs.docker.com/get-docker/)
- **Docker Compose**: [Install Docker Compose](https://docs.docker.com/compose/install/)

## How to Run

### 1. Clone the Repository

First, clone the repository to your local machine:

```bash
git clone https://github.com/LukaszCh233/quiz-world.git
cd quiz-world
```

### 2. Build and Start the Containers

```bash
docker-compose up --build
```




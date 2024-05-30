# quiz_World 
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
- **Log4j**: For logging application events.
- **PostgreSQL**: The relational database management system used.
- **JUnit**: For unit testing.
- **Mockito **: For testing.
- **Spring Data REST WebMVC**: For creating RESTful APIs.
- **Maven**
- **IntelliJ IDEA**
- **Postman**

## Prerequisites
Before you begin, ensure you have the following installed on your machine:
- **Java 17**: [Download and Install Java](https://adoptopenjdk.net/)
- **postgreSQL**: https://www.postgresql.org/download/
- **Maven**: https://maven.apache.org/download.cgi
- **Git**: https://git-scm.com/downloads
- **Postman**: https://www.postman.com/downloads/

## Configure Database
```bash
spring.application.name=quiz_World
spring.datasource.url=jdbc:postgresql://localhost:5432/quiz_world
spring.datasource.username=your_username
spring.datasource.password=your_password
server.port=8080

```
1. Open pgAdmin 4
2. Login to the administrator user of postgreSQL
3. Use your username and password from configuration
4. Create database quiz_World


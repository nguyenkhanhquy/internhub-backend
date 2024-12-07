# Internhub Backend

Internhub is a platform that connects students with companies for internships. This project is the backend part of the Internhub platform. It is built using Java, Spring Boot, and MySQL. The project provides RESTful API endpoints for the frontend to interact with the database.

## Table of Contents

1. [Use Case Diagram](#use-case-diagram)
2. [Class Diagram](#class-diagram)
3. [Prerequisites](#prerequisites)
4. [Installation](#installation)
5. [Technology Stack](#technology-stack)
6. [Documentation](#documentation)
7. [Author](#author)
8. [References](#references)

## Use Case Diagram

![Use Case Diagram](/assets/UseCaseDiagram.png)

## Class Diagram

![Class Diagram](/assets/ClassDiagram.png)

## Prerequisites

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/) 21 or later
- [Apache Maven](https://maven.apache.org/download.cgi) 3.9.9 or later
- [Spring Boot](https://spring.io/projects/spring-boot) 3.4.0 or later
- [MySQL](https://dev.mysql.com/downloads/mysql/) 8.0.40 or later

## Installation

1. Clone the project from the repository:

    ```sh
    git clone https://github.com/nguyenkhanhquy/internhub-backend.git
    ```

2. Open the project in your favorite IDE (IntelliJ IDEA is recommended).

3. Create `.env.local` file

   ```plaintext
   internhub-backend/
     |-- ...
     |-- assets/
     |-- src/
     |-- .env.local 👈
   ```

    Add the following environment variables to the `.env.local` file:
    
    ```plaintext
   DATASOURCE_URL= # Your MySQL database URL (e.g. jdbc:mysql://localhost:3306/internhub)
   DATASOURCE_USERNAME= # Your MySQL database username (e.g. root)
   DATASOURCE_PASSWORD= # Your MySQL database password (e.g. 5@fuex7)
   DATASOURCE_DRIVER= # Your MySQL database driver (e.g. com.mysql.cj.jdbc.Driver)
   MAIL_USERNAME= # Your email address (e.g. mail@email.com)
   MAIL_PASSWORD= # Your email password (e.g. 4245ssAf@)
   SIGNER_KEY= # Your JWT signer key (e.g. FD5oCuoXQvckL9HTzN1eXLio02OKQLMvH6XNr33bffi8N7SkZg5aSQ3H8PNVEhq7)
   VALID_DURATION= # Your JWT valid duration (e.g. 3) *in hours
   REFRESHABLE_DURATION= # Your JWT refreshable duration (e.g. 72) *in hours
   FRONTEND_URL= # Your frontend URL (e.g. http://localhost:3000)
   CLOUDINARY_CLOUD_NAME= # Your Cloudinary cloud name (e.g. internhub)
   CLOUDINARY_API_KEY= # Your Cloudinary API key (e.g. 123456789012345)
   CLOUDINARY_API_SECRET= # Your Cloudinary API secret (e.g. 123456789012345)
   ```

4. Run the project using the following command:

    ```sh
    mvn spring-boot:run
    ```

5. Open your browser and navigate to `http://localhost:8080/api/v1/swagger-ui/index.html` to view the Swagger documentation.

6. You can now test the API endpoints using Swagger.

7. Stop the project by pressing `Ctrl + C` in the terminal.

**Optional:**

- You can use [Postman](https://www.postman.com/downloads) to test the API endpoints
- You can use [MySQL Workbench](https://dev.mysql.com/downloads/workbench) to view the database
- You can deploy the project to Heroku by following the instructions [here](https://devcenter.heroku.com/articles/getting-started-with-java).

## Technology Stack

- Programming Language: Java
- Frameworks/Libraries: Spring Framework, Spring Boot, Spring Data JPA, Hibernate
- Database: MySQL
- IDE: IntelliJ IDEA
- Build Tool: Maven
- Testing: JUnit
- API Testing: Postman, Swagger, Jmeter
- Deployment: Heroku
- Cloud Storage: Cloudinary
- Documentation: Swagger
- Version Control: Git

## Documentation

- `http://localhost:8080/api/v1/swagger-ui/index.html` - Swagger documentation

## Author

- Author Name: `Nguyễn Khánh Quy`
- Email: <nguyenkhanhquy123@gmail.com>

## References

- [Java Documentation](https://docs.oracle.com/en/java/)
- [Maven Documentation](https://maven.apache.org/guides/index.html)
- [Spring Framework Documentation](https://spring.io/projects/spring-framework)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [Hibernate Documentation](https://hibernate.org/orm/documentation/5.5/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [IntelliJ IDEA Documentation](https://www.jetbrains.com/idea/documentation/)
- [Heroku Documentation](https://devcenter.heroku.com/categories/reference)
- [Cloudinary Documentation](https://cloudinary.com/documentation)
- [Swagger Documentation](https://swagger.io/docs/)
- [Git Documentation](https://git-scm.com/doc)

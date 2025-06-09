# Internhub Backend

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/technologies/downloads/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.0-green.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0.40-blue.svg)](https://dev.mysql.com/downloads/mysql/)
[![Maven](https://img.shields.io/badge/Maven-3.9.9-red.svg)](https://maven.apache.org/download.cgi)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](/LICENSE)

## 📋 Overview

Internhub is a platform connecting students with companies for internships. This project is the backend part of the Internhub platform, built using Java, Spring Boot, and MySQL. The project provides RESTful API endpoints for the frontend to interact with the database.

## 🔧 System Requirements

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/) 21 or higher
- [Apache Maven](https://maven.apache.org/download.cgi) 3.9.9 or higher
- [Spring Boot](https://spring.io/projects/spring-boot) 3.4.0 or higher
- [MySQL](https://dev.mysql.com/downloads/mysql/) 8.0.40 or higher

## 🚀 Installation Guide

### 1. Clone the project from repository

```sh
git clone https://github.com/nguyenkhanhquy/internhub-backend.git
cd internhub-backend
```

### 2. Open the project in your preferred IDE (IntelliJ IDEA recommended)

### 3. Create a `.env.local` file

Ensure the `.env.local` file is placed in the root directory of the project:

```plaintext
internhub-backend/
  |-- ...
  |-- assets/
  |-- src/
  |-- .env.local 👈
  |-- ...
```

Add the following environment variables to the `.env.local` file:

```plaintext
# Database Configuration
DATASOURCE_URL=jdbc:mysql://localhost:3306/internhub_db?createDatabaseIfNotExist=true
DATASOURCE_USERNAME=your_mysql_username
DATASOURCE_PASSWORD=your_mysql_password
DATASOURCE_DRIVER=com.mysql.cj.jdbc.Driver

# Email Configuration
MAIL_USERNAME=your_email_address
MAIL_PASSWORD=your_email_password

# JWT Configuration
SIGNER_KEY=your_jwt_signer_key
VALID_DURATION=your_valid_duration
REFRESHABLE_DURATION=your_refreshable_duration

# Frontend URL
FRONTEND_URL=http://localhost:3000

# Cloudinary Configuration
CLOUDINARY_CLOUD_NAME=your_cloudinary_cloud_name
CLOUDINARY_API_KEY=your_cloudinary_api_key
CLOUDINARY_API_SECRET=your_cloudinary_api_secret

# Admin Credentials
ADMIN_EMAIL=your_admin_email
ADMIN_PASSWORD=your_admin_password

# Redis Configuration
REDIS_HOST=your_redis_host
REDIS_PORT=your_redis_port
REDIS_USERNAME==your_redis_username
REDIS_PASSWORD=your_redis_password
```

### 4. Run the project

```sh
.\scripts\run.ps1
```

### 5. Open your browser and access the Swagger documentation

```plaintext
http://localhost:8080/api/v1/swagger-ui/index.html
```

### 6. Start testing the API endpoints using Swagger

### 7. Stop the project

Press `Ctrl + C` in the terminal.

## 📦 Available Commands

```sh
# Run the application
mvn spring-boot:run

# Clean and package the application
mvn clean package

# Run tests
mvn test

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=docker

# Run with Docker
docker-compose up -d

# Run with PowPowerShell script (Windows only)
.\scripts\run.ps1
```

## ⚙️ Technology Stack

- **Programming Language**: Java
- **Framework/Libraries**:
  - Spring Framework (Application framework)
  - Spring Boot (Auto-configuration)
  - Spring Data JPA (Data access)
- **Database**: MySQL
- **Build Tool**: Maven
- **Testing**: JUnit, Mockito
- **API Testing**: Swagger, Postman, JMeter
- **Cloud Storage**: Cloudinary
- **Deployment**: Heroku
- **IDE**: IntelliJ IDEA
- **Version Control**: Git

## 📁 Directory Structure

```plaintext
src/
  |-- main/
      |-- java/com/internhub/backend/
          |-- config/        # Configuration classes
          |-- controller/    # REST controllers
          |-- dto/           # Data transfer objects
          |-- entity/        # JPA entities
          |-- exception/     # Custom exceptions
          |-- mapper/        # Object mappers
          |-- repository/    # Data access interfaces
          |-- service/       # Business logic
          |-- task/          # Scheduled tasks
          |-- util/          # Utility classes
      |-- resources/
          |-- application.yaml  # Application properties
          |-- static/          # Static resources
          |-- templates/       # Template files
  |-- test/                  # Test classes
```

## 📊 System Architecture

### Use Case Diagram

![Khach](/docs/diagrams/Khach.png)

![SinhVien](/docs/diagrams/SinhVien.png)

![GiangVien](/docs/diagrams/GiangVien.png)

![DoanhNghiep](/docs/diagrams/DoanhNghiep.png)

![KhoaCNTT](/docs/diagrams/KhoaCNTT.png)

### Class Diagram

![Class Diagram](/docs/diagrams/ClassDiagram.png)

## 👨‍💻 Development Team

| Full Name | Student ID | GitHub |
|-----------|------------|--------|
| Nguyen Khanh Quy | 21110282 | [@nguyenkhanhquy](https://github.com/nguyenkhanhquy) |
| Dinh Trung Nguyen | 21110259 | [@NguyenDink](https://github.com/NguyenDink) |

## 📄 License

This project is licensed under the [MIT License](/LICENSE).

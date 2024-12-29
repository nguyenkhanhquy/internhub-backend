# Use the openjdk:21-jdk-slim as the base image
FROM openjdk:21-jdk-slim

# Update package lists and install maven
RUN apt-get update && apt-get install -y --no-install-recommends maven && apt-get clean && rm -rf /var/lib/apt/lists/*

# Set the working directory
WORKDIR /app

# Copy the pom.xml file and resolve dependencies
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy the source code to the working directory
COPY src ./src

# Build the project
RUN mvn clean package -DskipTests

# Expose port 8081
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "target/internhub-backend.jar"]

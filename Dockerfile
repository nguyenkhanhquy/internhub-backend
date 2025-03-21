# Stage 1: Build the JAR file using Maven
# Sử dụng image Maven để build ứng dụng, dựa trên Amazon Corretto 21 (phiên bản tối ưu của OpenJDK do AWS duy trì)
FROM maven:3.9.9-amazoncorretto-21-debian AS build

# Set working directory
# Thiết lập thư mục làm việc bên trong container
WORKDIR /app

# Copy pom.xml
# Chỉ copy pom.xml trước để tận dụng caching của Docker, giúp tránh tải lại dependencies nếu code không thay đổi
COPY ./pom.xml ./

# Download dependencies
# Tải trước tất cả dependencies để tối ưu quá trình build
RUN mvn dependency:go-offline -B

# Copy source code
# Sao chép toàn bộ source code vào container
COPY ./src ./src

# Build the application
# Build ứng dụng, bỏ qua test để tăng tốc độ
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage (Chạy ứng dụng Java)
# Sử dụng OpenJDK 21 Slim để chạy ứng dụng
FROM openjdk:21-jdk-slim

# Set working directory
# Thiết lập thư mục làm việc bên trong container
WORKDIR /app

# Copy the JAR file from the build stage
# Sao chép file JAR đã build từ giai đoạn build sang container runtime
COPY --from=build /app/target/*.jar backend.jar

# Environment variables with default values
# Thiết lập biến môi trường cho ứng dụng
ENV SPRING_PROFILES_ACTIVE=docker

# Expose the port the app runs on
# Mở cổng 8080 để ứng dụng có thể nhận request
EXPOSE 8080

# Start the application
# Khởi chạy ứng dụng khi container được start
# Dùng ENTRYPOINT thay vì CMD để container có thể nhận thêm tham số nếu cần
ENTRYPOINT ["java", "-jar", "backend.jar"]

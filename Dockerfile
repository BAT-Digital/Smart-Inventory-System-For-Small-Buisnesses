# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jdk-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml first (to leverage Docker layer caching)
COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .

# Copy the source code
COPY src ./src

# Install Maven and build the application
RUN ./mvnw clean install -DskipTests

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["./mvnw", "spring-boot:run"]
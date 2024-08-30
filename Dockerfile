# Use an official Maven image to build the project
FROM maven:3.8.6-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the Maven project files to the container
COPY pom.xml .
COPY src ./src

# Run Maven to build the project and create the JAR file
RUN mvn clean install

# Use an official OpenJDK image to run the application
FROM openjdk:17-jdk

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage to the runtime stage
COPY --from=build /app/target/payslip-0.0.1-SNAPSHOT.jar payslip.jar 

# Set environment variables for MySQL connection
ENV MYSQL_HOST=localhost
ENV MYSQL_PORT=3306
ENV MYSQL_DATABASE=payslip
ENV MYSQL_USER=root
ENV MYSQL_PASSWORD=root

# Run the application
ENTRYPOINT ["java", "-jar", "your-app.jar"]

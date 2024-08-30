# Use an official Maven image with OpenJDK 17 to build the project
# The Maven image includes Maven and JDK, which is needed to compile the Java project


FROM maven:3.8.3-openjdk-17 AS build

# Set the working directory inside the container to /app
# This directory is where we will copy our Maven project files and perform the build
WORKDIR /app

# Copy the Maven project descriptor file (pom.xml) to the working directory
# This file contains the project configuration and dependencies
COPY pom.xml .

# Copy the source code (src directory) to the working directory
# This directory contains the Java source files to be compiled
COPY src ./src

# Run Maven to clean any previous builds and install dependencies
# This command will compile the project and package it into a JAR file
RUN mvn clean install

# Use a lightweight OpenJDK 17 image to run the application
# This stage uses a separate image to reduce the final image size
FROM adoptopenjdk:11-jre-hotspot

# Set the working directory inside the container to /app
# This is where the JAR file will be copied and run from
WORKDIR /app

# Copy the JAR file generated in the build stage to the runtime stage
# The JAR file is located in /app/target/ from the build stage
COPY --from=build /app/target/payslip-0.0.1-SNAPSHOT.jar payslip.jar 

# Set environment variables for MySQL connection
# These are used to configure the application's database connection
ENV MYSQL_HOST=localhost
ENV MYSQL_PORT=3306
ENV MYSQL_DATABASE=payslip
ENV MYSQL_USER=root
ENV MYSQL_PASSWORD=root

# Specify the command to run the application when the container starts
# This command runs the JAR file using the Java runtime
ENTRYPOINT ["java", "-jar", "payslip.jar"]


# # Use an official Maven image to build the project
# FROM maven:3.8.6-openjdk-17 AS build

# # Set the working directory
# WORKDIR /app

# # Copy the Maven project files to the container
# COPY pom.xml .
# COPY src ./src

# # Run Maven to build the project and create the JAR file
# RUN mvn clean install

# # Use an official OpenJDK image to run the application
# FROM openjdk:17.0.1-jdk-slim

# # Set the working directory
# WORKDIR /app

# # Copy the JAR file from the build stage to the runtime stage
# COPY --from=build /app/target/payslip-0.0.1-SNAPSHOT.jar payslip.jar 

# # Set environment variables for MySQL connection
# ENV MYSQL_HOST=localhost
# ENV MYSQL_PORT=3306
# ENV MYSQL_DATABASE=payslip
# ENV MYSQL_USER=root
# ENV MYSQL_PASSWORD=root

# # Run the application
# ENTRYPOINT ["java", "-jar", "your-app.jar"]

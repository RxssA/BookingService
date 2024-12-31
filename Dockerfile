# Use an OpenJDK image as the base
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the project JAR file into the container
COPY target/BookingService-0.0.1-SNAPSHOT.jar /app/booking-service.jar


# Expose the port the service runs on
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "booking-service.jar"]

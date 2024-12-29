# Start with a base image containing Java runtime
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app
# Copy the application's build artifact (JAR file) to the container
# Adjust `target/myapp.jar` to match the path to your JAR file after building the project
COPY target/back-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your application runs on (adjust as needed)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

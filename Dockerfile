FROM openjdk:17-oracle
# Make port 8080 available to the world outside this container
EXPOSE 5002

# The application's jar file
ARG JAR_FILE=build/libs/*.jar


# Add the application's jar to the container
ADD ${JAR_FILE} app.jar


# Run the jar file
ENTRYPOINT ["java", "-jar", "/app.jar"]
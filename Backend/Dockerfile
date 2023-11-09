# Use the official OpenJDK as a parent image
FROM adoptopenjdk/openjdk17:alpine-jre

# Set environment variables
ENV APP_NAME=player
ENV APP_VERSION=0.0.1-SNAPSHOT
ENV SPRING_PROFILES_ACTIVE=production

# Create a directory for your application
RUN mkdir /app

# Copy the JAR file into the container at /app
COPY target/$APP_NAME-$APP_VERSION.jar /app/$APP_NAME.jar

# Specify the port number the container should expose
EXPOSE 8080

# Define the command to run your application
CMD ["java", "-jar", "/app/$APP_NAME.jar"]

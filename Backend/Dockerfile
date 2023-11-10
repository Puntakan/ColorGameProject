#
# Build stage
#
FROM maven:3.8.3-openjdk-17 AS build
COPY . .
RUN mvn clean install -Tests

#
# Package stage
#
FROM openjdk:17-jdk-alpine
COPY --from=build /target/player-0.0.1-SNAPSHOT.jar player.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","player.jar"]

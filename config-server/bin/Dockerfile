# Use an existing docker image as a base
FROM openjdk:8-jdk-alpine

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
# Tell the image what do do when it starts as container
ENTRYPOINT ["java", "-jar", "/app.jar"]

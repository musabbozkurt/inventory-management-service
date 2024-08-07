### Build stage ###

# Use an official Maven image as the base image
FROM maven:3.9.8-eclipse-temurin-21 AS maven-builder

ARG PROJECT_NAME=inventory-management-service
ARG PROJECT_DIR=/home/projects/${PROJECT_NAME}

COPY .. ${PROJECT_DIR}

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn -f ${PROJECT_DIR}/pom.xml dependency:go-offline

RUN mvn -f ${PROJECT_DIR}/pom.xml clean install -DskipTests

### Package stage ###

# Use an official OpenJDK image as the base image
FROM openjdk:21-jdk-slim

ENV PROJECT_NAME=inventory-management-service
ENV PROJECT_JAR_FILE_NAME=${PROJECT_NAME}-0.0.1-SNAPSHOT
ENV PROJECT_DIR=/home/projects/${PROJECT_NAME}

# Define the location of the New Relic agent JAR
ARG NEW_RELIC_AGENT_JAR_PATH
ENV NEW_RELIC_AGENT_JAR_PATH_ENV=$NEW_RELIC_AGENT_JAR_PATH

# Copy the New Relic agent JAR into the Docker image
COPY ${NEW_RELIC_AGENT_JAR_PATH_ENV} ${PROJECT_DIR}/${NEW_RELIC_AGENT_JAR_PATH_ENV}

# Copy the application JAR into the Docker image
COPY --from=maven-builder ${PROJECT_DIR}/target/${PROJECT_JAR_FILE_NAME}.jar ${PROJECT_DIR}/${PROJECT_JAR_FILE_NAME}.jar

WORKDIR ${PROJECT_DIR}

# Set the entrypoint to run the application with the New Relic Java agent
ENTRYPOINT java -javaagent:${NEW_RELIC_AGENT_JAR_PATH_ENV} -jar ${PROJECT_JAR_FILE_NAME}.jar

FROM openjdk:21-jdk-slim

# Uncomment the following command to define the location of the New Relic agent JAR
# ARG NEW_RELIC_AGENT_JAR=opt/newrelic/newrelic.jar

# Define the location of the application JAR
ARG JAR_FILE=target/*.jar

# Uncomment the following command to copy the New Relic agent JAR into the Docker image
# COPY ${NEW_RELIC_AGENT_JAR} /opt/newrelic/newrelic.jar

# Copy the application JAR into the Docker image
COPY ${JAR_FILE} inventory-management-service.jar

# Set the entrypoint to run the application
ENTRYPOINT ["java","-jar","/inventory-management-service.jar"]

# Use the following ENTRYPOINT command instead of the above ENTRYPOINT command
# to set the entrypoint to run the application with the New Relic Java agent
# ENTRYPOINT ["java", "-javaagent:/opt/newrelic/newrelic.jar", "-jar", "/inventory-management-service.jar"]

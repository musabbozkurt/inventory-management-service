FROM openjdk:21-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} inventory-management-service.jar
ENTRYPOINT ["java","-jar","/inventory-management-service.jar"]

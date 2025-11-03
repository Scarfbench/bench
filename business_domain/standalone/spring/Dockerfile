FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/standalone.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
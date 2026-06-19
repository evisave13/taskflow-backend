# Java 17 base image
FROM amazoncorretto:17

# Working directory
WORKDIR /app

# JAR copy karo
COPY target/taskflow-backend-0.0.1-SNAPSHOT.jar app.jar

# Port expose karo
EXPOSE 8080

# Run karo
ENTRYPOINT ["java", "-jar", "app.jar"]
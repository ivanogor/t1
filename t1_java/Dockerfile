FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/t1-java-demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
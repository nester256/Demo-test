# Стадия сборки
FROM maven:3.8.4-openjdk-17 as builder

WORKDIR /app
COPY . /app

RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true

# Стадия запуска
FROM adoptopenjdk/openjdk17:alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar /app/your-application.jar

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "/app/your-application.jar"]

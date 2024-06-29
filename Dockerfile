FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /app

COPY mvnw /app/mvnw
COPY .mvn /app/.mvn

RUN chmod +x /app/mvnw

RUN sed -i 's/\r$//' /app/mvnw

RUN ls -la /app && cat /app/mvnw && pwd

COPY pom.xml /app/pom.xml

COPY src /app/src

RUN mkdir -p /root/.m2 && chmod -R 777 /root/.m2

RUN unset MAVEN_CONFIG && /bin/sh /app/mvnw clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/*.jar /app/app.jar

COPY src/main/resources/articles.json /app/articles.json
COPY src/main/resources/tips.json /app/tips.json

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
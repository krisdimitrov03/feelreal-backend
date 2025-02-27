FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

COPY mvnw ./
COPY .mvn .mvn

RUN chmod +x mvnw && sed -i 's/\r$//' mvnw

COPY pom.xml ./
RUN mvn dependency:go-offline -B

COPY src ./src

RUN mkdir -p /root/.m2 && chmod -R 777 /root/.m2

RUN MAVEN_CONFIG="" /bin/sh ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

COPY src/main/resources/articles.json .
COPY src/main/resources/tips.json .

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

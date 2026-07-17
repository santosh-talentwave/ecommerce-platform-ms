FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/app.jar app.jar
EXPOSE 9091
ENTRYPOINT ["java","-jar","/app/app.jar"]



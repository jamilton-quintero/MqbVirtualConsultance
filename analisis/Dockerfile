FROM gradle:7.3.0-jdk8 AS build
WORKDIR /app
COPY . /app
RUN gradle build

FROM openjdk:8-jre-slim-buster
EXPOSE 8095
RUN mkdir -p /app/
COPY indicesLucene /app/indicesLucene
ADD build/libs/analisis-0.0.1-SNAPSHOT.jar /app/analisis-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/app/analisis-0.0.1-SNAPSHOT.jar"]

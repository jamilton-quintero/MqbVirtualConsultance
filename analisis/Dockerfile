# Usar una imagen base con JDK 8 y Gradle
FROM gradle:7.3.0-jdk8 AS build

# Establecer un directorio de trabajo
WORKDIR /app

# Copiar archivos de tu proyecto al directorio de trabajo
COPY . /app

# Ejecutar Gradle para construir el proyecto
RUN gradle clean build

# Crear una nueva imagen basada en OpenJDK 8
FROM openjdk:8-jre-slim-buster

# Exponer el puerto que utilizará la aplicación
EXPOSE 80

# Crear el directorio /app/ e incluir los índices de Lucene
RUN mkdir -p /app/
COPY --from=build /app/indicesLucene /app/indicesLucene

# Copiar el archivo JAR construido desde la etapa anterior
COPY --from=build /app/build/libs/analisis-0.0.1-SNAPSHOT.jar /app/analisis-0.0.1-SNAPSHOT.jar

# Establecer el punto de entrada para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/analisis-0.0.1-SNAPSHOT.jar"]

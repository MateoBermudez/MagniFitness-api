# Usar una imagen base de OpenJDK para construir y ejecutar la aplicación
FROM openjdk:17 AS build

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar los archivos del proyecto al contenedor
COPY . .

# Dar permisos de ejecución al script mvnw
RUN chmod +x mvnw mvnw.cmd

# Construir la aplicación usando Maven Wrapper
RUN ./mvnw clean package -DskipTests || ./mvnw.cmd clean package -DskipTests

# Usar una imagen base de OpenJDK para ejecutar la aplicación
FROM openjdk:17

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR generado desde la etapa de construcción
COPY --from=build /app/target/user-microservice-0.0.1-SNAPSHOT.jar /app/user-microservice.jar

# Exponer el puerto 8080
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/user-microservice.jar"]
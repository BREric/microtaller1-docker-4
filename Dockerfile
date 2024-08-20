FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/retosMicroservicios-0.0.1-SNAPSHOT.jar retosMicroservicios-0.0.1-SNAPSHOT.jar
EXPOSE 8082
CMD ["java", "-jar", "retosMicroservicios-0.0.1-SNAPSHOT.jar"]

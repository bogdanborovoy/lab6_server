FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/server-1.0-SNAPSHOT-jar-with-dependencies.jar server.jar
ENTRYPOINT ["java", "-jar", "server.jar"]
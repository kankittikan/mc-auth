FROM openjdk:17-jdk-slim
COPY target/mc-auth-0.0.1-SNAPSHOT.jar run.jar

EXPOSE 7071
ENTRYPOINT ["java","-jar","./run.jar"]
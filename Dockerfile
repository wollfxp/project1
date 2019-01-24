FROM openjdk:11-jre-slim
VOLUME /tmp
COPY build/libs/project1-0.1.0.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
FROM openjdk:10-jre
VOLUME /tmp
COPY target/project1-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
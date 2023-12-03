FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/enapiwek.jar.original app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
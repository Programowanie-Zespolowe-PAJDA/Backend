FROM maven:3.8.5-eclipse-temurin-17-alpine
WORKDIR /app
COPY pom.xml .
RUN mvn package
COPY . .
EXPOSE 8080
ENTRYPOINT ["java","-jar","target/enapiwek.jar","--spring.profiles.active=prod"]
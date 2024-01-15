FROM maven:3.8.5-eclipse-temurin-17-alpine
WORKDIR /app
COPY pom.xml .
COPY . .
RUN mvn package
EXPOSE 8080
ENTRYPOINT ["java","-jar","target/enapiwek.jar","--spring.profiles.active=prod"]
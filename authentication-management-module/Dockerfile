FROM maven:3.8.1-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY . .
RUN mvn clean package

FROM openjdk:20-jdk-slim
EXPOSE 8080
WORKDIR /app
COPY --from=build /app/target/authentication-management-module-1.0.1.jar ./authentication-management-module-1.0.1.jar
CMD ["java", "-jar", "authentication-management-module-1.0.1.jar", "--spring.profiles.active=prod"]
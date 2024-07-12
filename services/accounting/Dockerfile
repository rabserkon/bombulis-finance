FROM maven:3.8.4-openjdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
COPY . .
RUN mvn clean package
FROM openjdk:11-jre-slim
EXPOSE 8080
WORKDIR /app
COPY --from=build /app/target/accounting-1.0.0.jar  ./accounting-1.0.0.jar
CMD ["java", "-jar", "accounting-1.0.0.jar", "--spring.profiles.active=prod"]

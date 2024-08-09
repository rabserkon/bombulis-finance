FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY . .
RUN mvn clean package -DskipTests
FROM openjdk:17-jdk-oracle
EXPOSE 8080
WORKDIR /app
COPY --from=build /app/target/finance-report-0.0.1.jar ./finance-report-0.0.1.jar
CMD ["java", "-jar", "finance-report-0.0.1.jar", "--spring.profiles.active=prod"]

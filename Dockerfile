FROM maven:3.9.11-eclipse-temurin-25 AS build
WORKDIR /app

COPY pom.xml ./
COPY src src
RUN mvn -DskipTests package

FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=build /app/target/guide-coffee-api-0.0.1-SNAPSHOT.jar app.jar

RUN groupadd --system spring && useradd --system --gid spring spring \
    && chown -R spring:spring /app

USER spring

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

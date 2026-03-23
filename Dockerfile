FROM gradle:8-jdk21 AS build
WORKDIR /app

COPY gradle gradle
COPY gradlew gradlew
COPY build.gradle settings.gradle ./
COPY src src

RUN chmod +x gradlew && ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=unraid

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dserver.port=8080", "-jar", "/app/app.jar"]

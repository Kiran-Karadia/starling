FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY build.gradle settings.gradle gradlew /app/

COPY gradle /app/gradle

RUN chmod +x ./gradlew

COPY . /app

RUN ./gradlew build --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*-all.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
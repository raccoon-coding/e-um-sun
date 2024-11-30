FROM openjdk:17 AS build
WORKDIR /backend

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

RUN microdnf install findutils
RUN chmod +x ./gradlew
RUN ./gradlew clean build

FROM openjdk:17
WORKDIR /backend
COPY src src
COPY --from=build /backend/build/libs/*.jar /backend/myapp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/backend/myapp.jar"]

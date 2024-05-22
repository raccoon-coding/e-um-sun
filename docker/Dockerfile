FROM openjdk:17 AS build
WORKDIR /backend

COPY backend/gradlew .
COPY backend/gradle gradle
COPY backend/build.gradle .
COPY backend/settings.gradle .
COPY backend/src src

RUN microdnf install findutils
RUN chmod +x ./gradlew
RUN ./gradlew clean build

FROM openjdk:17
WORKDIR /backend
COPY backend/src src
COPY --from=build /backend/build/libs/*.jar /backend/myapp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/backend/myapp.jar"]
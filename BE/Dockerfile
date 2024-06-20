# Stage 1: Build
FROM eclipse-temurin:17-jdk-alpine as builder
WORKDIR /opt/app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN sed -i 's/\r$//' mvnw
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean install

# Stage 2: Create runtime image using jlink
FROM eclipse-temurin:17-jdk-alpine as jlink
WORKDIR /opt/jlink
COPY --from=builder /opt/app/target/*.jar /opt/app/app.jar
RUN jlink --add-modules ALL-MODULE-PATH --output /opt/jre --strip-debug --no-header-files --no-man-pages --compress=2

# Stage 3: Runtime
FROM alpine:3.18
WORKDIR /opt/app
EXPOSE 8080
COPY --from=jlink /opt/jre /opt/jre
COPY --from=builder /opt/app/target/*.jar /opt/app/app.jar
ENTRYPOINT ["/opt/jre/bin/java", "-jar", "/opt/app/app.jar"]

FROM maven:3.9.1-eclipse-temurin-17-alpine@sha256:2bda441f6ad8c4d3185ac7331be3f14528c49c51435e11acf94dce5402a98497 AS builder
COPY . /project
WORKDIR /project
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17.0.7_7-jre-alpine@sha256:7cbe01fd3d515407f1fda1e68068831aa6ae4b6930d76cdaa43736dc810bbd1b
RUN apk add dumb-init
RUN addgroup --system juser && adduser -S -s /bin/false -G juser juser
COPY --from=builder /project/target/spaceship-0.0.1.jar /app/
WORKDIR /app
RUN chown -R juser:juser /app
USER juser
ENTRYPOINT ["dumb-init", "java", "-jar", "spaceship-0.0.1.jar"]
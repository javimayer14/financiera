FROM maven:3.6.3-openjdk-11-slim AS build-deps
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

FROM maven:3.6.3-openjdk-11-slim AS dev
COPY --from=build-deps /root/.m2 /root/.m2
EXPOSE 8080
WORKDIR /app
VOLUME /app
ENTRYPOINT ["mvn","spring-boot:run"]

FROM maven:3.6.3-openjdk-11-slim AS build
COPY --from=build-deps /app /app
COPY --from=build-deps /root/.m2 /root/.m2
COPY src/ /app/src/
WORKDIR /app
RUN mvn package -DskipTests

FROM adoptopenjdk/openjdk11:alpine-jre as prod
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY --from=build /app/${JAR_FILE} app.jar
ENV DBHOST=localhost
ENV DBPORT=3306
ENV DBNAME=exchange
ENV DBUSER=root
ENV DBPASS=exchange2020
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
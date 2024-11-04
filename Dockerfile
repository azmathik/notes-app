#Stage 1

FROM maven:3.9.9-amazoncorretto-17-alpine as stage1

ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"

WORKDIR /opt/app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY ./src ./src

RUN mvn clean install

#Stage 2

FROM amazoncorretto:17-alpine3.17

WORKDIR /opt/demo

COPY --from=stage1 /opt/app/target/notes-app.jar /opt/app

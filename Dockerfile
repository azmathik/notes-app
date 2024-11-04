#Stage 1
FROM maven:3.9.9-amazoncorretto-17-alpine AS stage1

ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"

WORKDIR /opt/app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY ./src ./src

RUN mvn clean install -DskipTests

#Stage 2

FROM amazoncorretto:17-alpine3.17

WORKDIR /opt/app

COPY --from=stage1 /opt/app/target/notes-app.jar /opt/app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/opt/app/notes-app.jar"]

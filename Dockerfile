FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

RUN apk add --no-cache openssl

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN openssl genpkey -algorithm RSA -out ./src/main/resources/app.key -pkeyopt rsa_keygen_bits:2048
RUN openssl rsa -pubout -in ./src/main/resources/app.key -out ./src/main/resources/app.public 

RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/iris-api-0.0.1-SNAPSHOT.jar /app/iris-api.jar

RUN apk add --no-cache bash

COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

EXPOSE 8080

CMD /wait-for-it.sh mysql:3306 -- java -jar /app/iris-api.jar
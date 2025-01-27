#
# ecomapp dockerfile
# https://github.com/paul-prasanta
#
FROM openjdk:17-jdk-alpine AS build
COPY src /home/app/src
COPY pom.xml /home/app
COPY .mvn /home/app/.mvn
COPY mvnw /home/app/
# Disable Unit tests during packaging
RUN /home/app/mvnw -f /home/app/pom.xml clean package -DskipTests
EXPOSE 8080
ENTRYPOINT ["java","-jar","/home/app/target/EcomApp-1.0.jar"]
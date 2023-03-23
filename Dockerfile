FROM openjdk:8-jdk-alpine
LABEL maintainer="ahmedbaz1024"
WORKDIR /usr/local/bin/
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} gateway-server.jar
EXPOSE 8765
CMD ["java","-Dspring.profiles.active=dev","-jar","gateway-server.jar"]

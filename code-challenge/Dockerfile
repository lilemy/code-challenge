FROM openjdk:17

COPY code-challenge-0.0.1.jar /code-challenge-0.0.1.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/code-challenge-0.0.1.jar","--spring.profiles.active=prod"]
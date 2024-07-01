FROM openjdk:21

COPY /target/jobparser-0.0.1-SNAPSHOT.jar /jobParser.jar
COPY /.env /app/.env
WORKDIR /app

CMD ["java", "-jar", "/jobParser.jar", "--fill_db=true"]
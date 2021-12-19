FROM openjdk:11-jre-slim
COPY /build/libs/factor-app*SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xms800M", "-Xmx1800M", "-jar", "/app/app.jar"]

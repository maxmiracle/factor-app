FROM openjdk:11-jre-slim
COPY /build/libs/factor-app*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

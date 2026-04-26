FROM openjdk:25-ea

WORKDIR /app

COPY target/accounts.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
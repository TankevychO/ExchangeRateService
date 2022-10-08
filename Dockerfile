FROM openjdk:11

COPY target/TestTaskOverOnix-0.0.1-SNAPSHOT.jar TestTaskOverOnix.jar

ENTRYPOINT ["java", "-jar", "TestTaskOverOnix.jar"]

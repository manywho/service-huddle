FROM maven:onbuild-alpine

EXPOSE 8080

CMD ["java", "-Xmx400m", "-jar", "/usr/src/app/target/huddle-1.0-SNAPSHOT.jar"]
FROM openjdk:23-slim
WORKDIR /app
COPY target/course_mapping_be-0.0.1-SNAPSHOT.jar .
EXPOSE 9099
ENTRYPOINT ["java","-jar","course_mapping_be-0.0.1-SNAPSHOT.jar"]

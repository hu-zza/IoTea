FROM amazoncorretto:17-alpine
WORKDIR /opt/app
COPY iotea/target/iotea-*.jar IoTea.jar
CMD ["java", "-jar", "IoTea.jar"]
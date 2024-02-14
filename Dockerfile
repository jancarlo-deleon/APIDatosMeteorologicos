FROM openjdk:17-jdk
COPY target/datosmeteorologicos.jar .
ENTRYPOINT ["java","-jar","datosmeteorologicos.jar"]

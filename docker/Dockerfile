FROM dockerfile/java:oracle-java8
ADD categolj2-backend.jar /opt/categolj2-backend/
EXPOSE 8080
WORKDIR /opt/categolj2-backend/
CMD ["java", "-Xms512m", "-Xmx1g", "-jar", "categolj2-backend.jar"]

FROM dockerfile/java:oracle-java8
MAINTAINER Toshiaki Maki <makingx at gmail.com>

ADD categolj2-backend.jar /opt/categolj2-backend/
EXPOSE 8080 8443
WORKDIR /opt/categolj2-backend/
VOLUME ["/tmp"]
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-Xms512m", "-Xmx1g", "-jar", "categolj2-backend.jar"]

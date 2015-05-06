FROM java:8
MAINTAINER Toshiaki Maki <makingx at gmail.com>

EXPOSE 80 443
WORKDIR /opt/categolj2-backend/
VOLUME ["/tmp"]
RUN mkdir -p /var/log/categolj2
ADD categolj2-backend.jar /opt/categolj2-backend/
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "categolj2-backend.jar", "--server.port=443", "--server-http.port=80"]

FROM openjdk:11-jre-slim

# preparing the image with timezone and minimal debugging tools
RUN apt-get update && \
    apt-get install -y tzdata curl less && \
    apt autoremove -y && apt clean all -y && \
    rm -rf /var/lib/apt/lists/*

# set tz
RUN cp /usr/share/zoneinfo/Europe/Berlin /etc/localtime
RUN echo "Europe/Berlin" >  /etc/timezone

ENV APP_HOME /opt/sample-api

RUN mkdir -p $APP_HOME/config
COPY src/main/resources/application*.yml $APP_HOME/config/

COPY build/libs/sample-api.jar $APP_HOME

WORKDIR $APP_HOME
EXPOSE 8080

CMD java -Dspring.profiles.active=$PROFILES -jar sample-api.jar
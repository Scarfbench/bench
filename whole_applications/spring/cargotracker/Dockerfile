FROM openjdk:17-jdk-slim

ENV JAVA_OPTS=""
ENV PROJECT_HOME=/opt/app
ENV SVC_NAME=cargo-tracker

WORKDIR $PROJECT_HOME
ADD target/$SVC_NAME.jar service.jar
# update the timestamp in order to invalidate the docker cache
RUN sh -c 'touch /service.jar'

# Create user, chown, and chmod.
# OpenShift requires that a numeric user is used in the USER declaration instead of the user name
RUN chmod -R u+x $PROJECT_HOME \
    && chgrp -R 0 $PROJECT_HOME \
    && chmod -R g=u $PROJECT_HOME
    USER 2000

EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar ./service.jar" ]
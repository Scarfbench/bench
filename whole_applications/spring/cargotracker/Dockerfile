FROM openjdk:17-jdk-slim

# set smoke test env variables
ENV CARGO_TRACKER_BASE_URL=http://localhost:8080/cargo-tracker
ENV CARGO_TRACKER_HOME_URI=/index.xhtml

# Install Python (add python3-venv for virtual environment support)
RUN apt-get update && apt-get install -y python3 python3-venv python3-pip && rm -rf /var/lib/apt/lists/*

# Create and activate virtual environment for Python dependencies (PEP 668 safe)
RUN python3 -m venv /opt/venv
ENV PATH="/opt/venv/bin:$PATH"
# Shared browsers path so Chromium is cached once
ENV PLAYWRIGHT_BROWSERS_PATH=/ms-playwright
RUN mkdir -p /ms-playwright && chmod 755 /ms-playwright

# Install Playwright and Chromium dependencies inside venv
RUN pip install --no-cache-dir --upgrade pip setuptools wheel \
 && pip install --no-cache-dir playwright==1.47.0 \
 && playwright install --with-deps chromium

ENV JAVA_OPTS=""
ENV PROJECT_HOME=/opt/app
ENV SVC_NAME=cargo-tracker

WORKDIR $PROJECT_HOME
ADD target/$SVC_NAME.jar service.jar
ADD smoke.py smoke.py
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
# Simple single-stage build (larger image, but straightforward)
FROM maven:3.9.9-eclipse-temurin-17
WORKDIR /app

# Copy build definition first for dependency layer caching
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests dependency:go-offline

# Copy sources and build
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests package

ENV VERBOSE=1

# Copy Python smoke test utility (optional use inside container)
RUN apt-get update -qq \
	&& apt-get install -y -qq --no-install-recommends python3 \
	&& rm -rf /var/lib/apt/lists/*
COPY smoke.py ./smoke.py
RUN chmod +x ./smoke.py

EXPOSE 8080

# Run the assembled Quarkus jar from the quarkus-app directory
ENTRYPOINT ["sh","-c","java -jar target/quarkus-app/quarkus-run.jar"]

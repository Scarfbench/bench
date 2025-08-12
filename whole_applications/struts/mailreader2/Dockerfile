# Multi-stage Dockerfile for Struts 2 Mail Reader Application

# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml first to leverage Docker layer caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the WAR file
RUN mvn clean package -DskipTests

# Stage 2: Runtime with Jetty
FROM jetty:11-jre17-eclipse-temurin

# Set Jetty user
USER root

# Copy the WAR file from builder stage
COPY --from=builder /app/target/mailreader2-2.0.0.war /var/lib/jetty/webapps/mailreader2.war

# Configure Jetty to run on port 9347
RUN echo "jetty.http.port=9347" > /var/lib/jetty/start.d/http-port.ini

# Set proper ownership
RUN chown -R jetty:jetty /var/lib/jetty/webapps/ /var/lib/jetty/start.d/

# Switch back to jetty user
USER jetty

# Expose port 9347
EXPOSE 9347

# Jetty will automatically deploy the WAR file
# The application will be available at http://localhost:9347/mailreader2

FROM openjdk:11-jre-slim

# Install Tomcat
RUN apt-get update && \
    apt-get install -y wget && \
    wget https://archive.apache.org/dist/tomcat/tomcat-9/v9.0.70/bin/apache-tomcat-9.0.70.tar.gz && \
    tar -xzf apache-tomcat-9.0.70.tar.gz && \
    mv apache-tomcat-9.0.70 /opt/tomcat && \
    rm apache-tomcat-9.0.70.tar.gz

# Set environment variables
ENV CATALINA_HOME=/opt/tomcat
ENV PATH=$CATALINA_HOME/bin:$PATH

# Create logs directory
RUN mkdir -p /app/logs

# Copy the WAR file
COPY target/billpayment.war $CATALINA_HOME/webapps/

# Expose port
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]

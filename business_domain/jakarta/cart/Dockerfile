# Multi-stage build: Build stage
FROM eclipse-temurin:11-jdk AS builder

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy source code and Maven wrapper
COPY . .

# Make Maven wrapper executable
RUN chmod +x mvnw

# Build the project
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:11-jdk

# Install curl and unzip
RUN apt-get update && apt-get install -y curl unzip xmlstarlet && rm -rf /var/lib/apt/lists/*

# Set GlassFish version
ENV GLASSFISH_VERSION=7.0.16

# Download and install GlassFish
RUN curl -L -o /tmp/glassfish.zip https://github.com/eclipse-ee4j/glassfish/releases/download/${GLASSFISH_VERSION}/glassfish-${GLASSFISH_VERSION}.zip && \
    unzip /tmp/glassfish.zip -d /opt && \
    rm /tmp/glassfish.zip && \
    mv /opt/glassfish7 /opt/glassfish

# Set environment variables
ENV GLASSFISH_HOME=/opt/glassfish
ENV PATH=$PATH:$GLASSFISH_HOME/bin

# Configure the default domain to use port 8080 (it already exists)
# We don't need to create a new domain, just use the existing one

# Expose ports (default domain1 ports)
EXPOSE 8080 4848 8181

# Copy the WAR file from builder stage
COPY --from=builder /app/cart-ear/target/cart-ear.ear $GLASSFISH_HOME/glassfish/domains/domain1/autodeploy/

# Inject JVM options directly into domain.xml
RUN xmlstarlet ed -L \
    -s "/domain/config/java-config" -t elem -n jvmopt1 -v "--add-modules=jdk.jartool" \
    -s "/domain/config/java-config" -t elem -n jvmopt2 -v "--add-exports=jdk.jartool/jdk.security.jarsigner=ALL-UNNAMED" \
    $GLASSFISH_HOME/glassfish/domains/domain1/config/domain.xml

# Start GlassFish
CMD ["asadmin", "start-domain", "--verbose", "domain1"]

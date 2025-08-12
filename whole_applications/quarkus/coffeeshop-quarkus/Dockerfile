# ---------- Build (optimized cache) ----------
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copy only POM first so dependency download is cached
COPY pom.xml ./
RUN mvn -B -ntp -DskipTests dependency:go-offline

# Copy source (and anything needed for the build)
COPY src ./src
# If you have generated sources/config used at build time, copy them here too.
# COPY infra/some-build-resources ./infra/some-build-resources

RUN mvn -B -DskipTests package

# ---------- Run ----------
FROM eclipse-temurin:17-jre-jammy
# (same run stage as above)
RUN useradd -u 1001 -r -g root -s /sbin/nologin quarkus \
 && mkdir -p /opt/quarkus && chown -R 1001:root /opt/quarkus
USER 1001
WORKDIR /opt/quarkus
COPY --from=build /workspace/target/quarkus-app/lib/ ./lib/
COPY --from=build /workspace/target/quarkus-app/*.jar ./
COPY --from=build /workspace/target/quarkus-app/app/ ./app/
COPY --from=build /workspace/target/quarkus-app/quarkus/ ./quarkus/
EXPOSE 8080
ENV JAVA_OPTS="-XX:MaxRAMPercentage=80 -Dquarkus.http.host=0.0.0.0"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS $JAVA_ARGS -jar quarkus-run.jar"]

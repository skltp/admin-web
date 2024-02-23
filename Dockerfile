FROM maven:3-eclipse-temurin-11-alpine AS maven

WORKDIR /opt/build

RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=bind,source=src,target=src \
    mvn install -Pjar


FROM eclipse-temurin:11-jre-alpine AS admin-web
ENV LOGGING_CONFIG=/opt/app/log4j2.xml \
    BASE_DIR=/opt/app \
    USER=ind-app

WORKDIR ${BASE_DIR}

COPY --from=maven /opt/build/target/*.jar ${BASE_DIR}/app.jar
COPY log4j2.xml ${LOGGING_CONFIG}
RUN adduser -HD -u 1000 -h ${BASE_DIR} ${USER}
USER ${USER}
CMD java -XX:MaxRAMPercentage=75 ${JAVA_OPTS} -jar /opt/app/app.jar
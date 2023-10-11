FROM maven:3-ibm-semeru-11-focal AS maven

WORKDIR /opt/build

RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=bind,source=src,target=src \
    mvn install -Pjar


FROM ibm-semeru-runtimes:open-11-jre AS admin-web
ENV LOGGING_CONFIG=/opt/app/log4j2.xml
COPY --from=maven /opt/build/target/*.jar /opt/app/app.jar
COPY log4j2.xml ${LOGGING_CONFIG}
RUN useradd ind-app -MU
USER ind-app
CMD ["java", "-jar", "/opt/app/app.jar"]
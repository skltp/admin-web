#FROM tomcat:9-jdk11-openjdk
FROM bitnami/tomcat:9.0

ENV TOMCAT_BASE_DIR=/opt/bitnami/tomcat \
    TOMCAT_INSTALL_DEFAULT_WEBAPPS=no
ENV APP_WAR=$TOMCAT_BASE_DIR/webapps/admin-web.war \
    LOG4J_CONFIGURATION_FILE=$TOMCAT_BASE_DIR/conf/admin-web-log4j.xml

COPY target/skltp-admin-web-*.war $APP_WAR

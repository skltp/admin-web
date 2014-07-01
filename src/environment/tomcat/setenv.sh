#export CATALINA_OPTS="$CATALINA_OPTS -Xms256m -Xmx512m -XX:MaxPermSize=256m"

export CATALINA_OPTS="$CATALINA_OPTS -Dspring.config.location=file:/usr/local/skltp-admin-web/skltp-admin-web-config-override.properties"

# DEV profile - NOT FOR PRODUCTION
#export CATALINA_OPTS="$CATALINA_OPTS -Dspring.profiles.active=dev"
#export CATALINA_OPTS="$CATALINA_OPTS -Xms256m -Xmx512m -XX:MaxPermSize=256m"

export CATALINA_OPTS="$CATALINA_OPTS -Dspring.config.location=file:/usr/local/jms-admin-web/application.properties"

# DEV profile - NOT FOR PRODUCTION
#export CATALINA_OPTS="$CATALINA_OPTS -Dspring.profiles.active=dev"
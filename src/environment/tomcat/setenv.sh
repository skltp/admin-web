#export CATALINA_OPTS="$CATALINA_OPTS -Xms256m -Xmx512m -XX:MaxPermSize=256m"

export CATALINA_OPTS="$CATALINA_OPTS -Dapp.conf.dir=/Users/hakan/opt/apache/apache-tomcat-8.0.18/conf/admin-web"

# DEV profile - NOT FOR PRODUCTION
#export CATALINA_OPTS="$CATALINA_OPTS -Dspring.profiles.active=dev"
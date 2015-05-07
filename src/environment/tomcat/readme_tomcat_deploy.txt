Deploying to Tomcat
====================
1. Put the setenv.sh file in $TOMCAT_HOME/bin and make sure it is executable
2. Configure the path to the config dir in setenv.sh
3. Put the override property file (skltp-admin-web-config-override.properties) in the location configured in setenv.sh
4. Configure values in the override property file
5. Restart Tomcat (to load the config in setenv.sh)
6. Deploy the app by putting the war-file in $TOMCAT_HOME/webapps
7. Test the app
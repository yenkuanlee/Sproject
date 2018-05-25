javac -cp sqlite.jar:servlet-api.jar:json.jar test.java
sudo mv test.class /opt/tomcat7/webapps/ROOT/WEB-INF/classes/
sudo /opt/tomcat7/bin/shutdown.sh
sudo /opt/tomcat7/bin/startup.sh

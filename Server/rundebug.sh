mvn clean install
clear
cd target
java -Djava.net.preferIPv4Stack=true -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar RMIBasicServer-1.0-SNAPSHOT.jar $1 $2 $3 $4 $5
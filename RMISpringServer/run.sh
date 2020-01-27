mvn clean install
clear
mvn  -q -pl . spring-boot:run -Dspring-boot.run.arguments=$1
mvn clean compile test package -Prunnable-war sonar:sonar
java -jar ./target/java-1.0.0-BUILD-SNAPSHOT-war-exec.jar > ./runLog.log &

tail -f ./runLog.log

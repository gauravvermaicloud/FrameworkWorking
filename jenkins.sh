export JENKINS_HOME=$(pwd)/jenkins
echo  'Using Jenkins Home' $JENKINS_HOME
java -jar $SOFTWARE_HOME/jenkins/jenkins.war --httpPort=8085



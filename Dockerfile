FROM jboss/wildfly


USER root

WORKDIR /tmp
COPY infrastructure/postgresql-42.2.5.jar ./
COPY infrastructure/jboss-command.sh ./
COPY infrastructure/set-cheetunit-property.cli ./
COPY infrastructure/install-module.cli ./
# search and replace
RUN sed -i -e 's/\r$//' ./jboss-command.sh
RUN chmod +x ./jboss-command.sh
RUN ./jboss-command.sh &&  rm -rf $JBOSS_HOME/standalone/configuration/standalone_xml_history/


# Create Wildfly admin user
RUN $JBOSS_HOME/bin/add-user.sh admin admin --silent

COPY ./cheetunit-examples/hello-world-example/target/hello-world-example-1.0.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/
COPY ./cheetunit-examples/greeter-example/target/greeter-example-1.0.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/
COPY ./cheetunit-examples/college-example/target/college-example-1.0.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

# docker build -t demo -f infrastructure/Dockerfile .
# patrick@holzerp-linux:~/dev/gepardec/CheetUnit$ docker run -p 8080:8080 -d --name jboss-cheetunit demo
# patrick@holzerp-linux:~/dev/gepardec/CheetUnit$ docker-compose up -d

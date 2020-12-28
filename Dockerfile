FROM jboss/wildfly:21.0.1.Final

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

# deploy war's
COPY ./cheetunit-examples/*/target/*.war /opt/jboss/wildfly/standalone/deployments/

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

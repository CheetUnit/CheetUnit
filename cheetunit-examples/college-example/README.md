## Technical hints

### Prepare Postgres DB

```
docker run -it --rm -p 5432:5432 -e POSTGRES_DB=cheetunit -e POSTGRES_USER=cheetunit -e POSTGRES_PASSWORD=cheetunit postgres:12.5
```

```
mvn flyway:migrate
```

### Configure Wildfly

Extract `src/test/resources/misc/wildfly/postgres_driver.zip` into `$WILDFLY_HOME/modules`

Add a datasource into `$WILDFLY_HOME/standalone/configuration/standalone.xml` under `<subsystem xmlns="urn:jboss:domain:datasources...">/<datasoures>`

```
<datasource jndi-name="java:jboss/datasources/CollegeExampleDS" pool-name="CollegeExampleDS" enabled="true">
    <connection-url>jdbc:postgresql://localhost:5432/cheetunit</connection-url>
    <driver>postgresql</driver>
    <security>
        <user-name>cheetunit</user-name>
        <password>cheetunit</password>
    </security>
</datasource>
```

Add a postgres driver into `WILDFLY_HOME/standalone/configuration/standalone.xml` under `<subsystem xmlns="urn:jboss:domain:datasources...">/<drivers>`

```
<driver name="postgresql" module="org.postgresql">
    <driver-class>org.postgresql.Driver</driver-class>
</driver>
```



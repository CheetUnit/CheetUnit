# CheetUnit

Process ongoing, further information will follow soon. 

## Execute Integration Tests
### Local Setup

TODO 

### Docker
Run `mvn clean install` from the top-level directory to build all artifacts.

Setup JBoss and postgresql with the following commands:
```shell
echo "=> Create network"
docker network create cheetunit
echo "=> Start database"
docker run -d -p 5432:5432 --net=cheetunit \
  -e POSTGRES_DB=cheetunit \
  -e POSTGRES_USER=cheetunit \
  -e POSTGRES_PASSWORD=cheetunit \
  --name postgres postgres:13.1
echo "=> Migrate database"
cd ./cheetunit-examples/college-example/
mvn flyway:migrate
cd ../..
echo "=> Start JBoss"
docker build -t cheetunit-jboss .
docker run -d -p 8080:8080 --net=cheetunit --name cheetunit-jboss cheetunit-jboss
```

Execute all integration tests with `mvn verify -DskipITs=false -DskipUnitTests=true`

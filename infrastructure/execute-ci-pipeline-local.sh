#
# Copyright 2021 Gepardec IT Services GmbH and the CheetUnit contributors
# SPDX-License-Identifier: Apache-2.0
#

# switch to parent directory
cd ..

# build project
mvn clean install

# setup infrastructure
echo "=> Create network"
docker network create cheetunit
echo "=> Start database"
docker run -d -p 5432:5432 --net=cheetunit \
  -e POSTGRES_DB=cheetunit \
  -e POSTGRES_USER=cheetunit \
  -e POSTGRES_PASSWORD=cheetunit \
  --name postgres postgres:12.5
echo "=> Migrate database"
cd ./cheetunit-examples/college-example/
mvn flyway:migrate
cd ../..
echo "=> Start JBoss"
docker build -t cheetunit-jboss .
docker run -d -p 8080:8080 --net=cheetunit --name cheetunit-jboss cheetunit-jboss

# execute integration tests
mvn verify -DskipITs=false -DskipUnitTests=true

# cleanup infrastructure
echo "=> Remove containers and network"
docker rm -f cheetunit-jboss
docker rm -f postgres
docker network rm cheetunit
docker image rm cheetunit-jboss

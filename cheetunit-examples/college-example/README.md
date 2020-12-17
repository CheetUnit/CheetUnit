#### Technical hints

**Prepare Postgres DB**

```
docker run -it --rm -p 5432:5432 -e POSTGRES_DB=cheetunit -e POSTGRES_USER=cheetunit -e POSTGRES_PASSWORD=cheetunit postgres
```

```
mvn flyway:migrate
```

**Configure Wildfly**

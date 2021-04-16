---
sort: 1
---

# Getting started

## Quick Intro

**Add Maven dependencies**

CheetUnit artifacts are in the Maven central repository. In order to use CheetUnit you have to include two dependencies to
your project.

Add the application server specific CheetUnit dependency to your pom. Currently, only wildfly is supported. This
dependency publishes the CheetUnit insider http endpoint.

```xml
<dependency>
    <groupId>io.github.cheetunit</groupId>
    <artifactId>cheetunit-wildfly</artifactId>
    <version>${cheetunit-version}</version>
</dependency>
```

Add the test specific CheetUnit dependency to your pom. This dependency provides functionality for sending the test
class and the relating invoker class to the server side where it will be executed.

```xml
<dependency>
    <groupId>io.github.cheetunit</groupId>
    <artifactId>cheetunit-test</artifactId>
    <version>${cheetunit-version}</version>
    <scope>test</scope>
</dependency>
```

**Enable CheetUnit on the server side**

Set the system property `cheetunit.enabled=true` on the server side. For example you can start your server with `-Dcheetunit.enabled=true`


_Example for Wildfly / JBoss EAP_

Set the system property in the standalone.xml:

```xml
<system-properties>
    <property name="cheetunit.enabled" value="true"/>
</system-properties>
```

Set the system property via jboss-cli:

```shell
/system-property=cheetunit.enabled:add(value=true)
```

```danger
Don't enable CheetUnit on production environment! Attackers could run code on the server side! 
We recommend to use a specific maven profile which includes the dependency.
```

**Add configuration file**

CheetUnit tests needs a property file which contains at least the following information:

* `cheetunit.host.protocol` = protocol of the CheetUnit insider endpoint, e.g. http
* `cheetunit.host.name`     = host name of server, e.g. localhost
* `cheetunit.host.port`     = port of the application, e.g. 8080
* `cheetunit.path`          = path of the CheetUnit insider endpoint, e.g. hello/rest

The example values points to the following CheetUnit insider
endpoint: `http://localhost:8080/hello/rest/cheetunit-insider`.

The default path of the property file is `src/test/resources/cheetunit.properties`

**Add Base Invoker class**

You need to add an invoker class, which is transferred to the server and executes the method call on the server side.
The `HelloWorldService` in this example is the service that will be executed on the server.

```java
public class HelloWorldServiceInvoker extends io.github.cheetunit.test.BaseServiceInvoker {

    @Inject
    private HelloWorldService service;

    public String getHelloWorld() {
        return service.getHelloWorld();
    }
}
```

**Write your test**

```java
class HelloWorldServiceIT {

    private static HelloWorldServiceInvoker service;

    @BeforeAll
    static void beforeAll() {
        service = CheetUnit.createProxy(HelloWorldServiceInvoker.class);
    }

    @Test
    void getHelloWorld() {
        String result = service.getHelloWorld();
        Assertions.assertEquals("Hello World!", result);
    }
}
```

**Execute your test**

1. Deploy your application with the CheetUnit insider endpoint.
2. Run your test

```note
Implement your service class before you write the corresponding invoker class. If you prefer test-driven development, 
you can start implementing your service class with the signature of your method under the test. Keep in mind that 
you need to redeploy (or hot swap) after every change.
```

## Configuration

The simplest way to configure CheetUnit is through a property file. The default path for the property file
is `src/test/resources/cheetunit.properties`. It is possible to have multiple property files which can be relevant for
different configuration, e.g. continuous integration environments. If you want to use a specific property file which has
not the default name, you can pass the system property `cheetunit.property.file` with the regarding property file name to
your process call. In addition, it is possible to override existing property definitions through the CLI. 

All mandatory properties describe the path to the CheetUnit insider endpoint so that the client side of CheetUnit (Unit
tests) knows where to transfer the invoker class.

* `cheetunit.host.protocol`=http
* `cheetunit.host.name`=localhost
* `cheetunit.host.port`=8080
* `cheetunit.path`=hello/rest


CheetUnit also supports a few optional properties which are very convenient for debugging or long-running tests.

* `cheetunit.httpclient.connecttimeout`=60000
* `cheetunit.httpclient.readtimeout`=60000
* `cheetunit.httpclient.writetimeout`=60000

The example values set the connect, read and write timeout of the CheetUnit's http client to 60000ms / 1 minute.

## Use Cases/Examples

```note
Full examples can be found [here](https://github.com/CheetUnit/CheetUnit/tree/main/cheetunit-examples).
```

### Hello World

CheetUnit can be used to test services, which are accessing a database (see [Transactions](https://cheetunit.github.io/CheetUnit/getting_started.html#transactions) 
below) and also for other services.

In the [Hello World Example](https://github.com/CheetUnit/CheetUnit/tree/main/cheetunit-examples/hello-world-example)
a simple [method](https://github.com/CheetUnit/CheetUnit/blob/main/cheetunit-examples/hello-world-example/src/main/java/io/github/cheetunit/examples/helloworld/service/HelloWorldService.java), 
which has no input-parameters, is tested. It returns a String which contains "Hello World!"; 

The Logger in the example proves, that the method is executed on the server side.

```java
@ApplicationScoped
public class HelloWorldService {

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldService.class);

    public String getHelloWorld() {
        LOG.info("Executing helloWorld service.");
        return "Hello World!";
    }
}
```
The associated Invoker-Class is [HelloWorldServiceInvoker](https://github.com/CheetUnit/CheetUnit/blob/main/cheetunit-examples/hello-world-example/src/test/java/io/github/cheetunit/examples/helloworld/HelloWorldServiceInvoker.java).
This is the class which is transferred to the server and executes the HelloWorldService there.

```java
public class HelloWorldServiceInvoker extends BaseServiceInvoker {

    @Inject
    private HelloWorldService service;

    public String getHelloWorld() {
        return service.getHelloWorld();
    }
}
```
When the application is deployed on the server, with CheetUnit it is very simple to write an integration test.

```java
class HelloWorldServiceIT {

    private static HelloWorldServiceInvoker service;

    @BeforeAll
    static void beforeAll() {
        service = CheetUnit.createProxy(HelloWorldServiceInvoker.class);
    }

    @Test
    void getHelloWorld() {
        String result = service.getHelloWorld();
        Assertions.assertEquals("Hello World!", result);
    }
}
```

### Method Call with parameters

In addition, we provided some examples where we pass different kind of parameters (primitives, enum, pojos) and expect a return 
object. Check out our example test [GreeterServiceIT](https://github.com/CheetUnit/CheetUnit/blob/main/cheetunit-examples/greeter-example/src/test/java/io/github/cheetunit/examples/greeter/GreeterServiceIT.java). 

```warning
Currently there is a restriction: you can't use lambdas, other functional objects or instances of anonymous inner classes as parameters for CheetUnit calls. 
We plan to support this in future versions.
```
### Transactions

By default, each CheetUnit call is executed in a separate transaction. This is required in some situations.

For example: your method call returns a JPA entity having fields that are lazy loaded. While the serialization of the entity, all fields are being loaded. This action requires an active transaction.

If your transactions are managed by a container, you do not need to do anything.
However, if you manage transactions by yourself (e.g. via `tx.begin();`, `tx.commit();`), you need to execute CheetUnit calls without transaction. This can be done via the annotating of the corresponding invoker or an invoker method:

```java
@CheetUnitNoTransactionRequired
public class PersonServiceInvoker extends BaseServiceInvoker {

    @Inject
    private PersonService service;

    public List<Person> getAllPersons() {
        List<Person> allPersons = service.getAllPersons();
        return allPersons;
    }

    public Person getPersonById(Long id) {
       return service.getPersonById(id);
    }
}
```
See our [example](https://github.com/CheetUnit/CheetUnit/blob/main/cheetunit-examples/college-example-eager/src/test/java/io/github/cheetunit/examples/college/eager/service/PersonServiceInvoker.java) for more details.

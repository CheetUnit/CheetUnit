---
sort: 1
---

# Getting started

## Quick Intro

**Add Maven dependencies**

CheetUnit artifacts are in the Maven central repository. In order to use CheetUnit you must include two dependencies to
your project.

Add the application server specific CheetUnit dependency to your pom. Currently, only wildfly is supported. This
dependency publishes the cheetunit insider http endpoint.

```xml

<dependency>
    <groupId>io.github.cheetunit</groupId>
    <artifactId>cheetunit-wildfly</artifactId>
    <version>1.0.0-RC1</version>
</dependency>
```

Add the test specific CheetUnit dependency to your pom. This dependency provides functionality for sending the test
class and the relating invoker class to the server side where it will be executed.

```xml

<dependency>
    <groupId>io.github.cheetunit</groupId>
    <artifactId>cheetunit-test</artifactId>
    <version>1.0.0-RC1</version>
    <scope>test</scope>
</dependency>
```

**Enable CheetUnit on the server side**

Set the `cheetunit.enabled` system property on the server side in the standalone.xml.

```xml

<system-properties>
    <property name="cheetunit.enabled" value="true"/>
</system-properties>
```

Alternatively you can use the CLI to set the system property:

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

## Configuration

The simplest way to configure CheetUnit is through a property file. The default path for the property file
is `src/main/resources/cheetunit.properties`. It is possible to have multiple property files which can be relevant for
different configuration, e.g. continuous integration environments. If you want to use a specific property file which has
not the default name, you can pass the system property `cheetunit.property.file` with the regarding property file name to
your process call. In addition, it is possible to override property definition ... TODO (if there are passed through the CLI )

All mandatory properties describe the path to the cheetunit insider endpoint so that the client side of CheetUnit (Unit
tests) knows where to transfer the invoker class.

* `cheetunit.host.protocol`=http
* `cheetunit.host.name`=localhost
* `cheetunit.host.port`=8080
* `cheetunit.path`=hello/rest

TODO optional properties: 
* ...

## Use Cases/Examples

```note
Full examples can be found [here](https://github.com/CheetUnit/CheetUnit/tree/main/cheetunit-examples).
```

### Hello World

Information will follow soon!

### Method Call with parameters

Information will follow soon!

### Transactions

Information will follow soon!

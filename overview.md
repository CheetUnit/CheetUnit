---
sort: 2
---


# CheetUnit overview

## How it works

![CheetUnit-Flow-Diagram](https://github.com/CheetUnit/CheetUnit/raw/gh-pages/_images/CheetUnit_Flow.jpg)

## Application on server side

<img src="https://github.com/CheetUnit/CheetUnit/raw/gh-pages/_images/CheetUnit_Application_Overview.jpg" title="Server Side Application Overview" width=400/>

## Modules

### CheetUnit-Test
CheetUnit's test module provides the test api and all necessary implementations for serializing, sending objects to the server side and deserialize the server side's result. 

### CheetUnit-Core
The core implementation provides CheetUnit's insider endpoint, which accepts the request from the client side, as well as serialization, invocation and class loading logic.

### CheetUnit-Wildfly
CheetUnit uses some CDI features which rely on specific CDI implementations. The CheetUnit wildfly module provides the support of the [Weld](https://weld.cdi-spec.org/) implementation of CDI. 


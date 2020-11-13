package com.gepardec.cheetunit.examples.helloworld;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HelloWorldService {

  public String getHelloWorld() {
    return "Hello world";
  }
}

package com.gepardec.cheetunit.examples.helloworld;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HelloWorldService {

  public String printAndGetHelloWorld() {
    System.out.println("Hello world");
    return "Hello world";
  }
}

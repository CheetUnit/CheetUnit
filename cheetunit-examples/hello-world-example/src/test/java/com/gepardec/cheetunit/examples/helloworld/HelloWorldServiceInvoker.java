package com.gepardec.cheetunit.examples.helloworld;

import javax.inject.Inject;

import com.gepardec.cheetunit.test.BaseServiceInvoker;

public class HelloWorldServiceInvoker extends BaseServiceInvoker {

  @Inject
  private HelloWorldService service;

  public String printAndGetHelloWorld() {
    return service.printAndGetHelloWorld();
  }
}

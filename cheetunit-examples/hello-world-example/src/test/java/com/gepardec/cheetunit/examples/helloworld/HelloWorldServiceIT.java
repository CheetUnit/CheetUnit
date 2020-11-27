package com.gepardec.cheetunit.examples.helloworld;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.gepardec.cheetunit.test.CheetUnit;

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
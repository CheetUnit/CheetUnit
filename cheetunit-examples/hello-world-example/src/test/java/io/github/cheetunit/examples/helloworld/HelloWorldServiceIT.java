/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.cheetunit.examples.helloworld;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.cheetunit.test.CheetUnit;

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

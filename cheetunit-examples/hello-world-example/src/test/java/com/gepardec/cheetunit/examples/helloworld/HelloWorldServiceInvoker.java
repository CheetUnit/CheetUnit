/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.examples.helloworld;

import javax.inject.Inject;

import com.gepardec.cheetunit.examples.helloworld.service.HelloWorldService;
import com.gepardec.cheetunit.test.BaseServiceInvoker;

public class HelloWorldServiceInvoker extends BaseServiceInvoker {

  @Inject
  private HelloWorldService service;

  public String getHelloWorld() {
    return service.getHelloWorld();
  }
}

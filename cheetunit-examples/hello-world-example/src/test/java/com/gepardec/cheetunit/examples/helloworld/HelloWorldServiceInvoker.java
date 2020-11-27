/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.examples.helloworld;

import com.gepardec.cheetunit.examples.helloworld.service.HelloWorldService;
import com.gepardec.cheetunit.test.BaseServiceInvoker;
import com.gepardec.cheetunit.test.CheetUnitConfig;

import javax.inject.Inject;

public class HelloWorldServiceInvoker extends BaseServiceInvoker {

  @Inject
  private HelloWorldService service;

  public String getHelloWorld() {
    return service.getHelloWorld();
  }

  @Override
  public CheetUnitConfig getConfig() {
    // TODO use properties instead of override of getConfig
    return new CheetUnitConfig("http", "localhost", "8080", "hello/rest/cheetunit-insider", getAdditionalClasses());
  }
}

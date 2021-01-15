/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.cheetunit.examples.helloworld;

import io.github.cheetunit.examples.helloworld.service.HelloWorldService;

import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class HelloWorldBean {

  @Inject
  private HelloWorldService helloWorldService;

  public String sayHello() {
    return helloWorldService.getHelloWorld();
  }
}

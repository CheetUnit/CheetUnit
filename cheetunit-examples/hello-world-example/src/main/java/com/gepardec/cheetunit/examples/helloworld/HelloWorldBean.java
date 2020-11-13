/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.examples.helloworld;

import com.gepardec.cheetunit.examples.helloworld.service.HelloWorldService;

import javax.annotation.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ManagedBean
@RequestScoped
public class HelloWorldBean {

  @Inject
  private HelloWorldService helloWorldService;

  public String sayHello() {
    return helloWorldService.getHelloWorld();
  }
}

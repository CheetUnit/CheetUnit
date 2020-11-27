/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package com.gepardec.cheetunit.examples.greeter;

import com.gepardec.cheetunit.examples.greeter.service.GreeterService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class GreetBean {

  @Inject
  private GreeterService greetService;

  private String username;

  private String greeting;

  public void greet() {
    greeting = greetService.greet(username);
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getGreeting() {
    return greeting;
  }

}

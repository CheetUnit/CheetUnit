/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.examples.greeter.service;

import com.gepardec.cheetunit.examples.greeter.pojo.GreetingValue;
import com.gepardec.cheetunit.examples.greeter.pojo.Salutation;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreeterService {

  public String greet(String name) {
    return "Hello " + name + "!";
  }

  public String greet(Salutation salutation, String name) {
    return "Hello " + salutation.getValue() + " " + name + "!";
  }

  public String greet(GreetingValue greetingValue) {
    return "Hello " + greetingValue.toString() + "!";
  }

  public String greet(String... names) {
    return "Hello " + String.join(", ", names) + "!";
  }

  public String greet(Salutation salutation, String name, String... otherNames) {
    return "Hello " + salutation.getValue() + " " + name + (otherNames.length == 0 ? "" : ", ") + String.join(", ", otherNames) + "!";
  }
}

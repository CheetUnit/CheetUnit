/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.examples.greeter;

import com.gepardec.cheetunit.examples.greeter.pojo.GreetingValue;
import com.gepardec.cheetunit.examples.greeter.pojo.Salutation;
import com.gepardec.cheetunit.examples.greeter.service.GreeterService;
import com.gepardec.cheetunit.test.BaseServiceInvoker;

import javax.inject.Inject;
import java.time.LocalDate;

public class GreeterInvoker extends BaseServiceInvoker {

  @Inject
  private GreeterService service;

  public String greet(String name) {
    return service.greet(name);
  }

  public String greet(Salutation salutation, String name) {
    return service.greet(salutation, name);
  }

  public String greet(GreetingValue greetingValue) {
    return service.greet(greetingValue);
  }

  public String greet(String... names) {
    return service.greet(names);
  }

  public String greet(Salutation salutation, String name, String... otherNames) {
    return service.greet(salutation, name, otherNames);
  }

  public LocalDate getNextDay(LocalDate aDay){
    return service.getNextDay(aDay);
  }
}

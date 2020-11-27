/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.examples.greeter.pojo;

import java.io.Serializable;

public class GreetingValue implements Serializable {
  Salutation salutation;
  String name;

  public GreetingValue(Salutation salutation, String name) {
    this.salutation = salutation;
    this.name = name;
  }

  @Override
  public String toString() {
    return salutation.getValue() + " " + name;
  }
}

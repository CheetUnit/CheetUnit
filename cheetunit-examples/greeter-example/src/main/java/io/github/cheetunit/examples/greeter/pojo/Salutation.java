/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.cheetunit.examples.greeter.pojo;

public enum Salutation {
  MISTER("Mr."),
  MISTRESS("Mrs."),
  MISS("Ms."),
  DOCTOR("Dr."),
  PROFESSOR("Prof.");

  private final String value;

  Salutation(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}

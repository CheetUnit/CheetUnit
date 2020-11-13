/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.examples.helloworld.service;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HelloWorldService {

  public String getHelloWorld() {
    return "Hello World!";
  }
}

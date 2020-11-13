/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.test;

import java.util.List;

class ServersideExecutor {

  private final String         url;
  private final List<Class<?>> classes;

  private ServersideExecutor(String url, List<Class<?>> classes) {
    this.url = url;
    this.classes = classes;
  }

  static ServersideExecutor create(String url, List<Class<?>> classes) {
    return new ServersideExecutor(url, classes);
  }

  Object execute(String methodName, Object[] args) {
    throw new RuntimeException("Not implemented yet.");
  }
}

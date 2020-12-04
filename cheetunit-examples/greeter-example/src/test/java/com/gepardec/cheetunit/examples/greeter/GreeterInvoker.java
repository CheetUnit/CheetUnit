/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.examples.greeter;

import com.gepardec.cheetunit.examples.greeter.service.GreeterServiceBaseInvoker;
import com.gepardec.cheetunit.test.BaseServiceInvoker;
import com.gepardec.cheetunit.test.CheetUnitConfig;
import com.gepardec.cheetunit.test.CheetUnitConfigProvider;

public class GreeterInvoker extends GreeterServiceBaseInvoker {

  @Override
  public CheetUnitConfig getConfig() {
    // TODO use properties instead of override of getConfig
    return new CheetUnitConfig("http", "localhost", "8080", "greeter/rest/cheetunit-insider", getAdditionalClasses());
  }
}

/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.cheetunit.examples.helloworld;

import io.github.cheetunit.examples.helloworld.service.HelloWorldService;
import io.github.cheetunit.test.BaseServiceInvoker;

import javax.inject.Inject;

public class HelloWorldServiceInvoker extends BaseServiceInvoker {

    @Inject
    private HelloWorldService service;

    public String getHelloWorld() {
        return service.getHelloWorld();
    }
}

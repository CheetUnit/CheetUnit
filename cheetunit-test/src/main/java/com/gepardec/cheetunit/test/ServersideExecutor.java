/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.test;

import com.gepardec.cheetunit.core.RequestDTO;

import java.util.List;

class ServersideExecutor {

    private final String url;
    private final List<Class<?>> classes;

    private ServersideExecutor(String url, List<Class<?>> classes) {
        this.url = url;
        this.classes = classes;
    }

    static ServersideExecutor create(String url, List<Class<?>> classes) {
        return new ServersideExecutor(url, classes);
    }

    Object execute(String methodName, Object[] args) {
        RequestDTO dto = RequestDTOFactory.create(methodName, args, classes);

        // execute rest call

        // unwrap return object


        throw new RuntimeException("Not implemented yet.");
    }

}

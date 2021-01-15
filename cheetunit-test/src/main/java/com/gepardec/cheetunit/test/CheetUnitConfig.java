/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.test;

import static com.gepardec.cheetunit.endpoint.CheetUnitEndpoint.CHEETUNIT_ENDPOINT;

/**
 * Holds all relevant configuration for cheet unit like the URL information of the insider endpoint
 * or which classes will be send to the server side
 */
public class CheetUnitConfig {

    private final String schema;
    private final String host;
    private final String port;
    private final String path;
    private final Class<?>[] additionalClasses;

    static CheetUnitConfig of(String schema, String host, String port, String path, Class<?>... additionalClasses) {
        return new CheetUnitConfig(schema, host, port, path + CHEETUNIT_ENDPOINT, additionalClasses);
    }

    private CheetUnitConfig(String schema, String host, String port, String path, Class<?>... additionalClasses) {
        this.schema = schema;
        this.host = host;
        this.port = port;
        this.path = path;
        this.additionalClasses = additionalClasses;
    }

    String getSchema() {
        return schema;
    }

    String getHost() {
        return host;
    }

    String getPort() {
        return port;
    }

    String getPath() {
        return path;
    }

    Class<?>[] getAdditionalClasses() {
        return additionalClasses;
    }
}

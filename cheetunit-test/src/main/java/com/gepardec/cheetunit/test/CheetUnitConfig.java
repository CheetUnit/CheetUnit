/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.test;

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

    public static CheetUnitConfig of(String schema, String host, String port, String path, Class<?>... additionalClasses) {
        return new CheetUnitConfig(schema, host, port, path, additionalClasses);
    }

    private CheetUnitConfig(String schema, String host, String port, String path, Class<?>... additionalClasses) {
        this.schema = schema;
        this.host = host;
        this.port = port;
        this.path = path;
        this.additionalClasses = additionalClasses;
    }

    public String getSchema() {
        return schema;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }

    public Class<?>[] getAdditionalClasses() {
        return additionalClasses;
    }
}

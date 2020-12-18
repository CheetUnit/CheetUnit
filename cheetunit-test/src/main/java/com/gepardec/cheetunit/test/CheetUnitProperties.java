/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CheetUnitProperties {

    private static final Logger LOG = LoggerFactory.getLogger(CheetUnitProperties.class);

    /**
     * Configures connect timeout for the httpClient in milliseconds
     */
    static final String CONNECT_TIMEOUT = System.getProperty("cheetunit.httpclient.connecttimeout");
    /**
     * Configures read timeout for the httpClient in milliseconds
     */
    static final String READ_TIMEOUT = System.getProperty("cheetunit.httpclient.readtimeout");
    /**
     * Configures write timeout for the httpClient in milliseconds
     */
    static final String WRITE_TIMEOUT = System.getProperty("cheetunit.httpclient.writetimeout");

    private static final String PROPERTY_FILE_NAME_KEY = "cheetunit.property.file";
    private static final String DEFAULT_PROPERTY_FILE_NAME = "cheetunit.properties";
    private static final String HOST_PROTOCOL_KEY = "cheetunit.host.protocol";
    private static final String HOST_NAME_KEY = "cheetunit.host.name";
    private static final String HOST_PORT_KEY = "cheetunit.host.port";
    private static final String PATH_KEY = "cheetunit.path";

    static String propertyFileName() {
        String fileName = System.getProperty(PROPERTY_FILE_NAME_KEY);
        if (fileName == null) {
            LOG.debug("No property file name specified. Use default file name {}.", DEFAULT_PROPERTY_FILE_NAME);
            return DEFAULT_PROPERTY_FILE_NAME;
        }
        LOG.info("Use specific property file name {}.", fileName);
        return fileName;
    }

    static String hostProtocol() {
        return getPropertyOrThrowException(HOST_PROTOCOL_KEY);
    }

    static String hostName() {
        return getPropertyOrThrowException(HOST_NAME_KEY);
    }

    static String hostPort() {
        return getPropertyOrThrowException(HOST_PORT_KEY);
    }

    static String path() {
        return getPropertyOrThrowException(PATH_KEY);
    }

    private static String getPropertyOrThrowException(String propertyKey) {
        String property = System.getProperty(propertyKey);
        if (property == null) {
            LOG.error("No property for key {} given. Have a look at the examples to see how to configure cheetunit.", propertyKey);
            throw new CheetUnitClientException(String.format("No property for key %s given.", propertyKey));
        }
        return property;
    }
}

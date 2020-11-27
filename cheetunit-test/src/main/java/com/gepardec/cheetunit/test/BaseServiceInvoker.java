/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.test;

/**
 * Base class for service invoker
 */
public abstract class BaseServiceInvoker implements CheetUnitConfigProvider {

    private static final String HOST_PROTOCOL = System.getProperty("cheetunit.host.protocol", "http");
    private static final String HOST_NAME = System.getProperty("cheetunit.host.name", "localhost");
    private static final String HOST_PORT = System.getProperty("cheetunit.host.port", "8080");
    private static final String PATH = System.getProperty("cheetunit.path", "cheetunit-insider");

    @Override
    public CheetUnitConfig getConfig() {
        return new CheetUnitConfig(HOST_PROTOCOL, HOST_NAME, HOST_PORT, PATH, getAdditionalClasses());
    }

    /**
     * Override this method to add additional classes which are always required for your application integration tests. <br />
     * The following classes must be contained in the the returned array:
     * <ul>
     *     <li>{@link BaseServiceInvoker}</li>
     *     <li>{@link CheetUnitConfigProvider}</li>
     *     <li>{@link CheetUnitConfig}</li>
     * </ul>
     *
     * @return Array of classes which should be transferred to the server.
     */
    protected Class<?>[] getAdditionalClasses() {
        return new Class[]{
                BaseServiceInvoker.class,
                CheetUnitConfigProvider.class,
                CheetUnitConfig.class
        };
    }
}

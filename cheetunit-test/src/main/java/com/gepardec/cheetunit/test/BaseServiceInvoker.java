/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.test;

/**
 * Base class for service invoker
 */
public abstract class BaseServiceInvoker implements CheetUnitConfigProvider {

    private static final String SERVER_PROTOCOL = System.getProperty("org.cheetunit.protocol", "http");
    private static final String SERVER_HOST = System.getProperty("org.cheetunit.host", "localhost");
    private static final String SERVER_PORT = System.getProperty("org.cheetunit.port", "8080");

    @Override
    public CheetUnitConfig getConfig() {
        return new CheetUnitConfig(SERVER_PROTOCOL, SERVER_HOST, SERVER_PORT, "cheetunit-insider", getAdditionalClasses());
    }

    /**
     * Override this method to add additional classes which are always required for your application integration tests. <br />
     * The class {@link BaseServiceInvoker} must be a part of the returned array.
     *
     * @return Array of classes which should be transferred to the server.
     */
    protected Class<?>[] getAdditionalClasses() {
        return new Class[]{
                BaseServiceInvoker.class
        };
    }
}

/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.cheetunit.test;

import static io.github.cheetunit.test.CheetUnitProperties.*;

/**
 * Base class for service invoker
 */
public abstract class BaseServiceInvoker implements CheetUnitConfigProvider {

    @Override
    public CheetUnitConfig getConfig() {
        return CheetUnitConfig.of(hostProtocol(), hostName(), hostPort(), path(), getAdditionalClasses());
    }

    /**
     * <p>Override this method to add additional classes which are always required for your application integration tests.</p>
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

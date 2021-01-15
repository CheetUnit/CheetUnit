/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.cheetunit.core;

import javax.enterprise.inject.spi.BeanManager;

/**
 * Provides services for cdi bean manager. There must be only one implementation of this service for corresponding CDI Provider
 */
public interface BeanManagerService {
    /**
     * This method must be called before each execution on server in order to avoid reloading of already loaded classes
     */
    void cleanup(BeanManager beanManager);

}

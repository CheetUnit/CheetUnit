/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.wildfly;

import com.gepardec.cheetunit.core.BeanManagerService;
import org.jboss.weld.bean.builtin.BeanManagerProxy;
import org.jboss.weld.resources.ClassTransformer;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WeldBeanManagerService implements BeanManagerService {
    @Override
    public void cleanup(javax.enterprise.inject.spi.BeanManager beanManager) {
        ((BeanManagerProxy) beanManager).getServices().get(ClassTransformer.class).cleanup();
    }
}

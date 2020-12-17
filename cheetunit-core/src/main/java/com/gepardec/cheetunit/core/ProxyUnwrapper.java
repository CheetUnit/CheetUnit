/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.core;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

/**
 * Provides tools that unwraps proxies to their original pojos. Some proxies e.g. Hibernate can not be serialized
 */
public class ProxyUnwrapper {

    private ProxyUnwrapper() {
        //no instantiation
    }

    private static final Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    /**
     * Unboxes object and gets its original POJO instance.
     * This method must be called before return the result of execution to the caller.
     */
    public static Object unwrap(Object proxy, Class<?> returnType){
        return mapper.map(proxy, returnType);
    }
}

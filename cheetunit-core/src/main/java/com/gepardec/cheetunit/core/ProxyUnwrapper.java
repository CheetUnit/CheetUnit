/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.core;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;

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
    public static Object unwrap(Object proxy, Class<?> returnType) {

        boolean simpleType = ClassUtils.isPrimitiveOrWrapper(returnType) || String.class.isAssignableFrom(returnType);
        boolean isJavaMath = StringUtils.startsWith(returnType.getName(), "java.math");
        boolean isJavaTime = StringUtils.startsWith(returnType.getName(), "java.time");
        boolean isJavaUtils = StringUtils.startsWith(returnType.getName(), "java.util");
        boolean isCollection = proxy instanceof Collection || proxy instanceof Map;
        if (simpleType || isJavaMath || isJavaTime || (isJavaUtils && !isCollection)) {
            return proxy;
        }

        return mapper.map(proxy, returnType);
    }
}

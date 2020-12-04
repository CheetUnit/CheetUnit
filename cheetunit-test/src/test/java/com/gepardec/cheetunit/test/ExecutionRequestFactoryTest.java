/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.test;

import com.gepardec.cheetunit.core.ExecutionRequest;
import com.gepardec.cheetunit.core.SerializedObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class ExecutionRequestFactoryTest {

    private static final Class<?>[] ARG_TYPES = {String.class, Long.class, Double.class};
    private static final Object[] ARGS = {"param1", 2L, 3D};

    @Test
    void create_assertProperties() {
        ExecutionRequest dto = ExecutionRequestFactory.create("myMethodName", ARGS, Collections.singletonList(ExecutionRequestFactory.class));

        Assertions.assertEquals("myMethodName", dto.getMethodName());

        Assertions.assertNotNull(dto.getArgs());
        Assertions.assertFalse(dto.getArgs().isEmpty());

        Assertions.assertNotNull(dto.getClassMap());
        Assertions.assertEquals(1, dto.getClassMap().size());
    }

    @Test
    void create_assertArgs() {
        ExecutionRequest dto = ExecutionRequestFactory.create("myMethodName", ARGS, Collections.singletonList(ExecutionRequestFactory.class));

        Assertions.assertNotNull(dto.getArgs());
        List<Object> result = dto.getArgs().stream().map(SerializedObject::toObject).collect(Collectors.toList());
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals("param1", result.get(0));
        Assertions.assertEquals(2L, result.get(1));
        Assertions.assertEquals(3D, result.get(2));
    }

    @Test
    void create_assertClassMap() {
        ExecutionRequest dto = ExecutionRequestFactory.create("myMethodName", ARGS, Collections.singletonList(ExecutionRequestFactory.class));

        Assertions.assertNotNull(dto.getClassMap());
        Assertions.assertEquals(1, dto.getClassMap().size());

        Map.Entry<String, String> entry = new ArrayList<>(dto.getClassMap().entrySet()).get(0);

        Assertions.assertEquals(ExecutionRequestFactory.class.getName(), entry.getKey());
    }
}

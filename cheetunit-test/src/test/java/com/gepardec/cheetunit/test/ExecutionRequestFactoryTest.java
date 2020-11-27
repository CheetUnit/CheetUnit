/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.test;

import com.gepardec.cheetunit.core.ExecutionRequest;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;

class ExecutionRequestFactoryTest {

  @Test
  void create_assertProperties() {
    ExecutionRequest dto = ExecutionRequestFactory.create("myMethodName", new Object[]{"param1", 2L, 3D}, Collections.singletonList(ExecutionRequestFactory.class));

    Assertions.assertEquals("myMethodName", dto.getMethodName());

    Assertions.assertNotNull(dto.getArgs());
    Assertions.assertFalse(dto.getArgs().isEmpty());

    Assertions.assertNotNull(dto.getClassMap());
    Assertions.assertEquals(1, dto.getClassMap().size());
  }

  @Test
  void create_assertArgs() {
    ExecutionRequest dto = ExecutionRequestFactory.create("myMethodName", new Object[]{"param1", 2L, 3D}, Collections.singletonList(ExecutionRequestFactory.class));

    Assertions.assertNotNull(dto.getArgs());
    Object[] result = SerializationUtils.deserialize(Base64.getDecoder().decode(dto.getArgs()));
    Assertions.assertEquals(3, result.length);
    Assertions.assertEquals("param1", result[0]);
    Assertions.assertEquals(2L, result[1]);
    Assertions.assertEquals(3D, result[2]);
  }

  @Test
  void create_assertClassMap() {
    ExecutionRequest dto = ExecutionRequestFactory.create("myMethodName", new Object[]{"param1", 2L, 3D}, Collections.singletonList(ExecutionRequestFactory.class));

    Assertions.assertNotNull(dto.getClassMap());
    Assertions.assertEquals(1, dto.getClassMap().size());

    Map.Entry<String, String> entry = new ArrayList<>(dto.getClassMap().entrySet()).get(0);

    Assertions.assertEquals(ExecutionRequestFactory.class.getName(), entry.getKey());
  }
}

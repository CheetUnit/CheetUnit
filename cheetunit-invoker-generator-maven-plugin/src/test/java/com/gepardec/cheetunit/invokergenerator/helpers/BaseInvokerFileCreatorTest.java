/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.invokergenerator.helpers;

import com.gepardec.cheetunit.invokergenerator.helpers.testclasses.ComplexGreeterService;
import com.gepardec.cheetunit.invokergenerator.helpers.testclasses.GreeterService;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BaseInvokerFileCreatorTest {

    @Test
    void testGreeterService() throws IOException {
        BaseInvokerFileCreator.createBaseInvoker("asdasdsada", ComplexGreeterService.class);

    }
}

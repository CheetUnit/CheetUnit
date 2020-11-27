/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SerializedObjectTest {

    @Test
    public void test(){
        SerializedObject serializedObject = SerializedObject.of("abc", String.class);
        assertEquals("abc", serializedObject.getObject());
    }

}
/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.cheetunit.core;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SerializedObjectTest {

    @Test
    public void simpleString(){
        SerializedObject serializedObject = SerializedObject.of("abc");
        assertEquals("abc", serializedObject.toObject());
    }

    @Test
    public void complexObject(){

        SimpleClass simpleObject1 = new SimpleClass();
        simpleObject1.setIntField(5);
        simpleObject1.setStringField("BlaBlubb");

        SimpleClass simpleObject2 = new SimpleClass();
        simpleObject2.setIntField(6);
        simpleObject2.setStringField("BlubbBla");

        ComplexClass complexObject = new ComplexClass();
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        complexObject.setUuids(Arrays.asList(uuid1, uuid2));
        complexObject.setSimplies(new SimpleClass[]{simpleObject1, simpleObject2});

        SerializedObject serializedObject = SerializedObject.of(complexObject);
        ComplexClass deserialized = (ComplexClass) serializedObject.toObject();

        assertEquals(2, deserialized.getUuids().size());
        assertEquals(uuid1, deserialized.getUuids().get(0));
        assertEquals(uuid2, deserialized.getUuids().get(1));

        assertEquals(2, deserialized.getSimplies().length);
        assertEquals(5, deserialized.getSimplies()[0].getIntField());
        assertEquals("BlaBlubb", deserialized.getSimplies()[0].getStringField());
        assertEquals(6, deserialized.getSimplies()[1].getIntField());
        assertEquals("BlubbBla", deserialized.getSimplies()[1].getStringField());
    }

    @Test
    void nullObject() {
        SerializedObject serializedObject = SerializedObject.of(null);
        Object result = serializedObject.toObject();
        assertNull(result);
    }
}

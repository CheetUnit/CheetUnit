/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.core;

import com.gepardec.cheetunit.core.serialization.SerializationUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SerializationUtilsTest {

    @Test
    public void serializationOfPrimitive(){
        long longVar = 42L;

        byte[] serialized = SerializationUtils.serialize(longVar);
        long deserialized = (long) SerializationUtils.deserialize(serialized);

        assertEquals(42L, deserialized);
    }

    @Test
    public void serializationOfWrapper(){
        Integer intVar = 42;

        byte[] serialized = SerializationUtils.serialize(intVar);
        Integer deserialized = (Integer) SerializationUtils.deserialize(serialized);

        assertEquals(42, deserialized);
    }

    @Test
    public void serializationOfString() {
        String str = "Hi there!";

        byte[] serialized = SerializationUtils.serialize(str);
        String deserialized = (String) SerializationUtils.deserialize(serialized);
    }

    @Test
    public void serializationOfArray (){
        String[] array = new String[]{"String 1", "String 2"};
        byte[] serialized = SerializationUtils.serialize(array);
        String[] deserialized = (String[]) SerializationUtils.deserialize(serialized);

        assertEquals(2, deserialized.length);
        assertEquals("String 1", array[0]);
        assertEquals("String 2", array[1]);
    }

    @Test
    public void serializationOfMap(){
        Map<String, Long> map = new HashMap<>();
        map.put("Answer to the Ultimate Question of Life, the Universe, and Everything", 42L);
        map.put("Just some long", 123654L);

        byte[] serialized = SerializationUtils.serialize(map);
        Map<String, Long> deserialized = (Map<String, Long>) SerializationUtils.deserialize(serialized);

        assertEquals(123654L, deserialized.get("Just some long"));
        assertEquals(42L, deserialized.get("Answer to the Ultimate Question of Life, the Universe, and Everything"));
    }

    @Test
    public void serializationOfObject(){

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

        byte[] serialized = SerializationUtils.serialize(complexObject);
        ComplexClass deserialized = (ComplexClass) SerializationUtils.deserialize(serialized);

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
    public void serializationOfObjectArray(){
        SimpleClass simpleObject1 = new SimpleClass();
        simpleObject1.setIntField(5);
        simpleObject1.setStringField("BlaBlubb");

        SimpleClass simpleObject2 = new SimpleClass();
        simpleObject2.setIntField(6);
        simpleObject2.setStringField("BlubbBla");

        byte[] serialized = SerializationUtils.serialize(new SimpleClass[] {simpleObject1, simpleObject2});
        SimpleClass[] deserialized = (SimpleClass []) SerializationUtils.deserialize(serialized);

        assertEquals(2, deserialized.length);
        assertEquals(5, deserialized[0].getIntField());
        assertEquals("BlaBlubb", deserialized[0].getStringField());
        assertEquals(6, deserialized[1].getIntField());
        assertEquals("BlubbBla", deserialized[1].getStringField());
    }

    @Test
    public void serializationOfDate(){
        Date date = new Date();
        byte[] serialized = SerializationUtils.serialize(date);
        Date deserialized = (Date) SerializationUtils.deserialize(serialized);
        assertEquals(date.getTime(), deserialized.getTime());
    }

    @Test
    public void serializationOfLocalDate(){
        LocalDate localDate = LocalDate.now();
        byte[] serialized = SerializationUtils.serialize(localDate);
        LocalDate deserialized = (LocalDate) SerializationUtils.deserialize(serialized, LocalDate.class);
        assertTrue(deserialized.compareTo(localDate) == 0);
    }

    @Test
    public void serializationOfLocalDateInNotSerializable(){
        LocalDate localDate = LocalDate.now();
        Notserializable notserializable = new Notserializable();
        notserializable.setLocalDates(new LocalDate[]{localDate});
        byte[] serialized = SerializationUtils.serialize(notserializable);

        Notserializable deserialized = (Notserializable) SerializationUtils.deserialize(serialized);
        assertTrue(deserialized.getLocalDates()[0].compareTo(localDate) == 0);
    }
}
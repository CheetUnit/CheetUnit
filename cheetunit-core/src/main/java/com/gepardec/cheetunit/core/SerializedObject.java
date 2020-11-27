/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.core;

import com.gepardec.cheetunit.core.serialization.SerializationUtils;

import java.util.Base64;

public class SerializedObject {
    private String className;
    private String serializedInstance;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSerializedInstance() {
        return serializedInstance;
    }

    public void setSerializedInstance(String serializedInstance) {
        this.serializedInstance = serializedInstance;
    }

    public static SerializedObject of(Object object, Class<?> objectClass){
        SerializedObject serializedObject = new SerializedObject();
        serializedObject.setClassName(object.getClass().getName());
        serializedObject.setSerializedInstance(Base64.getEncoder().encodeToString(SerializationUtils.serialize(object)));
        return serializedObject;
    }

    public Object extractObject(){
        if(className == null ){
            throw new IllegalStateException("Classname is not allowed to be null");
        }

        Class<?> aClass = null;
        try {
            aClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new CheetUnitException("Class " + className + " not found", e);
        }

        return SerializationUtils.deserialize(Base64.getDecoder().decode(serializedInstance), aClass);
    }
}

/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.core;

import com.gepardec.cheetunit.core.serialization.SerializationUtils;

import java.util.Base64;

/**
 * Wrapper for serialized object, that  holds the information about original object class and the serialized instance of the object in form of byte array encoded into base64 for lossless transmission over the network.
 */
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

    /**
     * Creates a serialized instance of the object.
     * @param object object to be serialized
     * @param objectClass class of the object
     * @return a serialized instance of the object
     */
    public static SerializedObject of(Object object, Class<?> objectClass){
        SerializedObject serializedObject = new SerializedObject();
        serializedObject.setClassName(objectClass.getName());
        serializedObject.setSerializedInstance(Base64.getEncoder().encodeToString(SerializationUtils.serialize(object)));
        return serializedObject;
    }

    /**
     * Deserializes a byte array encoded into base64 into the java object of className
     * @return a deserialized instance of the object
     * @throws IllegalStateException if className is null
     * @throws CheetUnitException if no class with the className can be found
     */
    public Object extractObject(){
        if(className == null ){
            throw new IllegalStateException("Classname is not allowed to be null");
        }

        Class<?> aClass;
        try {
            aClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new CheetUnitException("Class " + className + " not found", e);
        }

        return SerializationUtils.deserialize(Base64.getDecoder().decode(serializedInstance), aClass);
    }
}

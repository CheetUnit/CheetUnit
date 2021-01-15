/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.cheetunit.core;

import java.util.Base64;

/**
 * Wrapper for serialized object, that holds the serialized instance of the object
 * in form of byte array encoded into base64 for lossless transmission over the network.
 */
public class SerializedObject {

    private String serializedInstance;

    public String getSerializedInstance() {
        return serializedInstance;
    }

    public void setSerializedInstance(String serializedInstance) {
        this.serializedInstance = serializedInstance;
    }

    /**
     * Creates a serialized instance of the object.
     *
     * @param object object to be serialized
     * @return a serialized instance of the object
     */
    public static SerializedObject of(Object object) {
        SerializedObject serializedObject = new SerializedObject();
        serializedObject.setSerializedInstance(Base64.getEncoder().encodeToString(SerializationUtils.serialize(object)));
        return serializedObject;
    }

    /**
     * Deserializes a byte array encoded into base64 into the java object of className
     *
     * @return a deserialized instance of the object
     * @throws IllegalStateException if className is null
     * @throws CheetUnitServerException    if no class with the className can be found
     */
    public Object toObject() {
        return SerializationUtils.deserialize(Base64.getDecoder().decode(serializedInstance));
    }
}

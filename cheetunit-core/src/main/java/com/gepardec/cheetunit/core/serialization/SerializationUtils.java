/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.core.serialization;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.gepardec.cheetunit.core.CheetUnitException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SerializationUtils {

    private SerializationUtils(){
        // no instantiation allowed
    }

    /**
     * Serializes object into a byte array using hessian serialization. Allows to serialize instances of classes not implementing {@link java.io.Serializable}
     * @param object object to be serialized
     * @return null, of source object is null. Otherwise serialized object as byte array
     */
    public static byte[] serialize(Object object) {

        if(object == null){
            return null;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bos);
        out.getSerializerFactory().setAllowNonSerializable(true);
        out.getSerializerFactory().addFactory(SerializationConfig.getCustomSerializerFactory());

        try {
            out.startMessage();
            out.writeObject(object);
            out.completeMessage();
            out.close();
        } catch (IOException e) {
            throw new CheetUnitException("Serialization failed. Object : " + object);
        }

        return bos.toByteArray();
    }

    /**
     * Deserializes byte array into the object using hessian serialization.
     * @param bytes Serialized object as byte array
     * @return a deserialized instance of the object
     * @throws CheetUnitException if some exception occurs.
     */
    public static Object deserialize(byte[] bytes) {
        return deserialize(bytes, null);
    }

    /**
     * Deserializes byte array into the object using hessian serialization.
     * @param bytes Serialized object as byte array
     * @param aClass result class
     * @return a deserialized instance of the object
     * @throws CheetUnitException if some exception occurs.
     */
    public static Object deserialize(byte[] bytes, Class<?> aClass) {

        if (bytes == null) {
            throw new IllegalArgumentException("The byte[] must not be null");
        }

        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        Hessian2Input in = new Hessian2Input(bin);
        in.getSerializerFactory().setAllowNonSerializable(true);
        in.getSerializerFactory().addFactory(SerializationConfig.getCustomSerializerFactory());

        try {
            in.startMessage();
            Object result;
            if (aClass == null) {
                result = in.readObject();
            } else {
                return in.readObject(aClass);
            }
            in.completeMessage();
            in.close();
            bin.close();

            return result;
        } catch (IOException e) {
            throw new CheetUnitException("Deserialization failed.");
        }
    }
}

/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.cheetunit.core;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;

import java.nio.charset.StandardCharsets;

public class SerializationUtils {

    private static final XStream X_STREAM = new XStream(new DomDriver());

    static {
        X_STREAM.addPermission(AnyTypePermission.ANY);
    }

    private SerializationUtils() {
        // no instantiation allowed
    }

    /**
     * Serializes object into a byte array using xstream serialization. Allows to serialize instances of classes not implementing {@link java.io.Serializable}
     *
     * @param object object to be serialized
     * @return null, if source object is null. Otherwise serialized object as byte array
     * @see XStream
     */
    public static byte[] serialize(Object object) {
        String xmlString = X_STREAM.toXML(object);
        return xmlString.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Deserializes byte array into the object using xstream serialization.
     *
     * @param bytes Serialized object as byte array
     * @return a deserialized instance of the object
     * @see XStream
     */
    public static Object deserialize(byte[] bytes) {
        String xmlString = new String(bytes, StandardCharsets.UTF_8);
        return X_STREAM.fromXML(xmlString);
    }
}

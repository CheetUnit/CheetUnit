/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.core.serialization;

import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;

/**
 * Default deserializer that uses Java-Serialization for the classes, that can't be deserialized by hessian, like {@link java.time.LocalDate}
 */
public class DefaultJavaDeserializer extends AbstractDeserializer {
    @Override
    public Object readObject(AbstractHessianInput in) throws IOException {
        return SerializationUtils.deserialize(in.readBytes());
    }
}

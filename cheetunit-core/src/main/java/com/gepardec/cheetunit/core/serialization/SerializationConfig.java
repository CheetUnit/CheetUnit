/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.core.serialization;

import com.caucho.hessian.io.AbstractSerializerFactory;
import com.caucho.hessian.io.ExtSerializerFactory;

public class SerializationConfig {

    public static AbstractSerializerFactory getCustomSerializerFactory() {
        ExtSerializerFactory extSerializerFactory = new ExtSerializerFactory();

        extSerializerFactory.addSerializer(java.time.LocalDate.class, new LocalDateSerializer());
        extSerializerFactory.addDeserializer(java.time.LocalDate.class, new LocalDateDeserializer());

        return extSerializerFactory;
    }
}

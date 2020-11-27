/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.core.serialization;

import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.time.LocalDate;

public class LocalDateDeserializer extends AbstractDeserializer {
    @Override
    public Class<?> getType() {
        return LocalDate.class;
    }

    @Override
    public Object readObject(AbstractHessianInput in) throws IOException {
        return SerializationUtils.deserialize(in.readBytes());
    }
}

/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.core.serialization;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.AbstractSerializer;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.io.Serializable;

/**
 * Default serializer that uses Java-Serialization for the classes, that can't be serialized by hessian, like {@link java.time.LocalDate}
 */
public class DefaultJavaSerializer extends AbstractSerializer {
    @Override
    public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
        if (obj == null) {
            out.writeNull();
        } else {
            out.writeBytes(SerializationUtils.serialize((Serializable) obj));
        }
    }
}

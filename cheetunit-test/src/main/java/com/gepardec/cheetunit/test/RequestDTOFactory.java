/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.test;

import com.gepardec.cheetunit.core.RequestDTO;
import org.apache.commons.lang3.SerializationUtils;

import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * Factory for creating a new object of type {@link RequestDTO}
 */
class RequestDTOFactory {

    private RequestDTOFactory() {
        // no instantiation allowed
    }

    static RequestDTO create(String methodName, Object[] args, List<Class<?>> classes) {
        RequestDTO dto = new RequestDTO();
        dto.setPrimaryClassName(classes.get(0).getName());
        dto.setMethodName(methodName);
        dto.setArgs(Base64.getEncoder().encodeToString(SerializationUtils.serialize(args)));
        dto.setClassMap(createClassMap(classes));
        return dto;
    }


    private static Map<String, String> createClassMap(List<Class<?>> classes) {
        // TODO
        throw new RuntimeException("Not implemented yet.");
    }
}

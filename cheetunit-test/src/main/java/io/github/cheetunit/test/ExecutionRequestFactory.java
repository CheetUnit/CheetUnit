/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.cheetunit.test;

import io.github.cheetunit.core.ExecutionRequest;
import io.github.cheetunit.core.SerializedObject;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Factory for creating a new object of type {@link ExecutionRequest}
 */
class ExecutionRequestFactory {

    private ExecutionRequestFactory() {
        // no instantiation allowed
    }

    static ExecutionRequest create(String methodName, Object[] args, List<Class<?>> classes) {
        ExecutionRequest request = new ExecutionRequest();
        request.setPrimaryClassName(classes.get(0).getName());
        request.setMethodName(methodName);
        request.setArgs(Arrays.stream(args).map(SerializedObject::of).collect(Collectors.toList()));
        request.setClassMap(createClassMap(classes));
        return request;
    }


    private static Map<String, String> createClassMap(List<Class<?>> classes) {
        return classes.stream().collect(
                Collectors.toMap(
                        Class::getName,
                        ExecutionRequestFactory::toByteArrayBase64Encoded
                ));
    }

    private static String toByteArrayBase64Encoded(Class<?> clazz) {
        String resourceName = clazz.getSimpleName() + ".class";
        try {
            byte[] bytes = IOUtils.toByteArray(clazz.getResource(resourceName));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new CheetUnitClientException(e);
        }
    }
}

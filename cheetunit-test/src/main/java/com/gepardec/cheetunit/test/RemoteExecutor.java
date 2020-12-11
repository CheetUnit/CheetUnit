/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gepardec.cheetunit.core.CheetUnitException;
import com.gepardec.cheetunit.core.ExecutionRequest;
import com.gepardec.cheetunit.core.SerializedObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Remote executor for transferring all necessary artifacts to the server and executing the class under test from client side (e.g. unit test) on the server side.
 */
class RemoteExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(RemoteExecutor.class);

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final String url;
    private final List<Class<?>> classes;

    private RemoteExecutor(String url, List<Class<?>> classes) {
        this.url = url;
        this.classes = classes;
    }

    /**
     * Factory method for instantiating a new object.
     *
     * @param url     of the cheet unit endpoint
     * @param classes to be transferred to the server
     * @return instance of the class
     */
    static RemoteExecutor of(String url, List<Class<?>> classes) {
        return new RemoteExecutor(url, classes);
    }

    /**
     * Transfers all necessary information to the server side by executing a REST request. <br />
     * Uses Serialization and Base64 decoding/encoding for transferring binary data.
     *
     * @param methodName which should be executed
     * @param args       object array containing the arguments for the executing method
     * @return result of the method under test, which has been executed on the server side
     */
    Object execute(String methodName, Object[] args) {
        ExecutionRequest dto = ExecutionRequestFactory.create(methodName, args, classes);

        SerializedObject response = executeRestCall(dto);

        return unwrapResponse(response);
    }

    private Object unwrapResponse(SerializedObject response) {
        Object returnObject = response.toObject();

        if (returnObject instanceof CheetUnitException) {
            LOG.error("Something unexpected has happened on the server side.", (Exception) returnObject);
            throw ((CheetUnitException) returnObject);
        }
        if (returnObject instanceof Throwable) {
            LOG.warn("An exception has been thrown on the server side.", ((Throwable) returnObject));
            throw new RemoteExecutionException("An exception has been thrown on the server side.", ((Throwable) returnObject));
        }
        LOG.debug("Return object is {}", returnObject);
        return returnObject;
    }

    private SerializedObject executeRestCall(ExecutionRequest dto) {
        OkHttpClient httpClient = buildHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(dto);
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(json, JSON))
                    .build();
            Response response = httpClient.newCall(request).execute();
            if (response.body() == null) {
                LOG.error("Response doesn't contain any appropriate payload.\\nResponse: {}", response);
                throw new CheetUnitClientException("Response doesn't contain any appropriate payload.");
            }

            return objectMapper.readValue(response.body().string(), SerializedObject.class);
        } catch (Exception e) {
            throw new CheetUnitClientException(e);
        }
    }

    private OkHttpClient buildHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (TestProperties.CONNECT_TIMEOUT != null) {
            builder.connectTimeout(Integer.parseInt(TestProperties.CONNECT_TIMEOUT), TimeUnit.MILLISECONDS);
        }
        if (TestProperties.READ_TIMEOUT != null) {
            builder.readTimeout(Integer.parseInt(TestProperties.READ_TIMEOUT), TimeUnit.MILLISECONDS);
        }
        if (TestProperties.WRITE_TIMEOUT != null) {
            builder.writeTimeout(Integer.parseInt(TestProperties.WRITE_TIMEOUT), TimeUnit.MILLISECONDS);
        }

        return builder.build();
    }

}

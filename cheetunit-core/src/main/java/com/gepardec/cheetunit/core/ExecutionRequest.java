/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.core;

import java.util.List;
import java.util.Map;

/**
 * DTO which will be sent from the client side to the server side.
 */
public class ExecutionRequest {

    private String primaryClassName;
    private String methodName;
    private List<SerializedObject> args;
    private Map<String, String> classMap;

    public String getPrimaryClassName() {
        return primaryClassName;
    }

    public void setPrimaryClassName(String primaryClassName) {
        this.primaryClassName = primaryClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<SerializedObject> getArgs() {
        return args;
    }

    public void setArgs(List<SerializedObject> args) {
        this.args = args;
    }

    public Map<String, String> getClassMap() {
        return classMap;
    }

    public void setClassMap(Map<String, String> classMap) {
        this.classMap = classMap;
    }
}

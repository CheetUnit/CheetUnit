/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.core;

import java.util.Map;

/**
 * DTO which will be sent from the clientside to the serverside.
 */
public class RequestDTO {

  private String              primaryClassName;
  private String              methodName;
  private String              args;
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

  public String getArgs() {
    return args;
  }

  public void setArgs(String args) {
    this.args = args;
  }

  public Map<String, String> getClassMap() {
    return classMap;
  }

  public void setClassMap(Map<String, String> classMap) {
    this.classMap = classMap;
  }
}

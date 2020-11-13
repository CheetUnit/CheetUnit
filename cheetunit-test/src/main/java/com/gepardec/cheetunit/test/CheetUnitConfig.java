/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.test;

public class CheetUnitConfig {
  private final String schema;
  private final String host;
  private final String port;
  private final String rootPath;
  private final Class<?>[] additionalClasses;

  public static final CheetUnitConfig DEFAULT_LOCALHOST = new CheetUnitConfig("http", "localhost", "8080", "");

  public CheetUnitConfig(String schema, String host, String port, String rootPath, Class<?>... additionalClasses) {
    this.schema = schema;
    this.host = host;
    this.port = port;
    this.rootPath = rootPath;
    this.additionalClasses = additionalClasses;
  }

  public String getSchema() {
    return schema;
  }

  public String getHost() {
    return host;
  }

  public String getPort() {
    return port;
  }

  public String getRootPath() {
    return rootPath;
  }

  public Class<?>[] getAdditionalClasses() {
    return additionalClasses;
  }
}

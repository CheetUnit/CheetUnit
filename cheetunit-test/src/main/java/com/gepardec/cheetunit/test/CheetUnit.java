/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

public class CheetUnit {

  private static final Logger LOG = LoggerFactory.getLogger(CheetUnit.class);

  /**
   * Creates a proxy instance for the given class. <br />
   * All methods invocations on the proxy object will be executed on the server side.
   *
   * @param clazz for the proxy instance
   * @param <T>   type of new instance
   * @return proxy instance for the given class
   */
  public static <T> T createProxy(Class<T> clazz) {
    ProxyFactory factory = new ProxyFactory();
    factory.setSuperclass(clazz); // TODO Implement if clazz is an interface

    try {
      return (T) factory.create(new Class<?>[0], new Object[0], new CheatUnitMethodHandler<T>(clazz, getConfig(clazz)));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }



  private static class CheatUnitMethodHandler<T> implements MethodHandler {

    private final Class<T>        clazz;
    private final CheetUnitConfig config;

    public CheatUnitMethodHandler(Class<T> clazz, CheetUnitConfig config) {
      this.clazz = clazz;
      this.config = config;
    }

    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) {

      List<Class<?>> classList = new ArrayList<>();
      classList.add(clazz);
      classList.addAll(Arrays.asList(config.getAdditionalClasses()));

      ServersideExecutor executor = ServersideExecutor.create(
          buildURL(config.getSchema(), config.getHost(), config.getPort(), config.getRootPath()),
          classList);

      return executor.execute(thisMethod.getName(), args);
    }

    private String buildURL(String schema, String host, String port, String rootPath) {
      return schema + "://" + host + ":" + port + "/" + rootPath + "/cheetunit/execute";
    }

  }


  private static <T> CheetUnitConfig getConfig(Class<T> clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException {
    if (CheetUnitConfigProvider.class.isAssignableFrom(clazz)) {
      Constructor<?>[] constructors = clazz.getDeclaredConstructors(); // TODO handle multiple constructors
      CheetUnitConfigProvider configProvider = (CheetUnitConfigProvider) constructors[0].newInstance();
      return configProvider.getConfig();
    }
    LOG.info(String.format("No CheetUnitConfig provided by class %s. Fallback to default config.", clazz.getSimpleName()));
    return CheetUnitConfig.DEFAULT_LOCALHOST;

  }


}

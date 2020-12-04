/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.invokergenerator.helpers;

import java.util.function.Function;

public abstract class ClassUtils {
  public static String extractClassName(Class<?> aClass) {
    return aClass.getName().substring(aClass.getName().lastIndexOf(".") + 1);
  }

  public static String extractClassName(String classname) {
    return classname.substring(classname.lastIndexOf(".") + 1);
  }

  public static String getClassNameOrArray(Class<?> aClass) {
    String className = extractClassName(aClass);

    return aClass.getName().startsWith("[L") ? className.substring(0, className.length()-1) + "[]" : className;
  }

  public static String extractPackageName(Class<?> aClass) {
    return aClass.getName().substring(0, aClass.getName().lastIndexOf("."));
  }

  public static String extractPackageName(String classname) {
    return classname.substring(0, classname.lastIndexOf("."));
  }

  public static String getNameOfClassOrNameOfArrayType(String classname) {
    if(classname.startsWith("[L")) {
      return classname.substring(2);
    }
    return classname;
  }
}

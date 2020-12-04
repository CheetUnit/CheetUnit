/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.invokergenerator.helpers;

import org.apache.maven.SessionScoped;
import org.apache.maven.plugin.MojoExecutionException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public abstract class ClassFinder {

  private static final List<Class<? extends Annotation>> SCOPE_ANNOTATIONS = Arrays.asList(
      RequestScoped.class,
      SessionScoped.class,
      ApplicationScoped.class,
      Dependent.class,
      ConversationScoped.class
  );

  public static List<Class<?>> getClasses(String packageName)
      throws MojoExecutionException {
    ArrayList<Class<?>> classes;
    try {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      assert classLoader != null;
      String path = packageName.replace('.', '/');
      Enumeration<URL> resources = classLoader.getResources(path);
      List<File> dirs = new ArrayList<>();
      while (resources.hasMoreElements()) {
        URL resource = resources.nextElement();
        dirs.add(new File(resource.getFile()));
      }
      classes = new ArrayList<>();
      for (File directory : dirs) {
        classes.addAll(findClasses(directory, packageName));
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      throw new MojoExecutionException(e.getMessage());
    }

    return classes;
  }

  private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
    List<Class<?>> classes = new ArrayList<>();
    if (!directory.exists()) {
      return classes;
    }
    File[] files = directory.listFiles();
    if(files == null || files.length == 0) {
      return classes;
    }
    for (File file : files) {
      if (file.isDirectory()) {
        assert !file.getName().contains(".");
        classes.addAll(findClasses(file, packageName + "." + file.getName()));
      } else if (file.getName().endsWith(".class")) {
        classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
      }
    }
    return classes;
  }

  public static List<Class<?>> getScopeAnnotatedClasses(List<Class<?>> classes) {
    List<Class<?>> annotatedClasses = new ArrayList<>();
    classes.forEach(c -> {
      if(isAnnotedWithScope(c)) {
        annotatedClasses.add(c);
      }
    });
    return annotatedClasses;
  }

  private static boolean isAnnotedWithScope(Class<?> c) {
    for (Class<? extends Annotation> scopeAnnotation : SCOPE_ANNOTATIONS) {
      if(c.isAnnotationPresent(scopeAnnotation)) {
        return true;
      }
    }
    return false;
  }
}

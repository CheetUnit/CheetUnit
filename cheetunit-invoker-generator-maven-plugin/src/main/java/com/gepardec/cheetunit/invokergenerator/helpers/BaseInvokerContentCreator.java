/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.invokergenerator.helpers;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.gepardec.cheetunit.invokergenerator.helpers.ClassUtils.*;

//TODO: AdditionalClasses
//@Override
//protected Class<?>[] getAdditionalClasses() {
//    Class<?>[] superClasses = super.getAdditionalClasses();
//    return ArrayUtils.addAll(superClasses, GreeterServiceBaseInvoker.class);
//    }
public class BaseInvokerContentCreator {

  private static final List<String> EXCLUDED_METHODS =
      Arrays.asList("wait", "equals", "toString", "hashCode", "getClass", "notify", "notifyAll");

  private static final List<String> EXCLUDED_IMPORT_PRIMITIVES =
      Arrays.asList("void", "byte", "short", "int", "long", "float", "double", "char", "boolean");

  private static final List<String> EXCLUDED_IMPORT_PACKAGES =
      Collections.singletonList("java.lang");

  public static String getBaseInvokerContent(Class<?> aClass) {
    StringBuilder invokerContentBuilder = new StringBuilder();
    invokerContentBuilder.append(packageLine(aClass));
    invokerContentBuilder.append("\n");
    invokerContentBuilder.append(imports(aClass));
    invokerContentBuilder.append("\n");
    invokerContentBuilder.append(classLine(aClass));
    invokerContentBuilder.append("\n");
    invokerContentBuilder.append(classInjection(aClass));
    invokerContentBuilder.append("\n");
    invokerContentBuilder.append(methods(aClass));
    invokerContentBuilder.append("\n");
    invokerContentBuilder.append("}");
    invokerContentBuilder.append("\n");
    return invokerContentBuilder.toString();
  }

  private static String methods(Class<?> aClass) {
    List<String> methodTexts = new ArrayList<>();

    for (Method method : aClass.getMethods()) {
      if (!Modifier.isPrivate(method.getModifiers())) {
        if (methodNotExcluded(method)) {
          methodTexts.add(methodText(method, aClass));
        }
      }
    }

    return String.join("\n\n", methodTexts);
  }

  private static String methodText(Method method, Class<?> invokedClass) {
    StringBuilder methodTextBuilder = new StringBuilder();

    methodTextBuilder.append(indent());
    methodTextBuilder.append("public ");
    methodTextBuilder.append(resolveType(method.getGenericReturnType()));
    methodTextBuilder.append(" ");
    methodTextBuilder.append(method.getName());
    methodTextBuilder.append("(");
    methodTextBuilder.append(methodParameters(method));
    methodTextBuilder.append(") {\n");
    methodTextBuilder.append(indent()).append(indent());
    if (!"void".equals(method.getReturnType().getName())) {
      methodTextBuilder.append("return ");
    }
    methodTextBuilder.append(extractClassName(invokedClass).toLowerCase());
    methodTextBuilder.append(".");
    methodTextBuilder.append(method.getName());
    methodTextBuilder.append("(");
    methodTextBuilder.append(methodParameterNames(method));
    methodTextBuilder.append(");\n");
    methodTextBuilder.append(indent());
    methodTextBuilder.append("}");

    return methodTextBuilder.toString();
  }

  private static String resolveType(Type type) {
    if(type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      String result = extractClassName(parameterizedType.getRawType().getTypeName());

      result = result + "<";

      List<String> typeArgumentClassNames = Stream.of(parameterizedType.getActualTypeArguments())
          .map(BaseInvokerContentCreator::resolveType)
          .collect(Collectors.toList());
      result = result + String.join(", ", typeArgumentClassNames);
      result = result + ">";
      return result;

    }

    return extractClassName(type.getTypeName());
  }

  private static String methodParameterNames(Method method) {
    List<String> parameterNames = Stream.of(method.getParameters()).map(Parameter::getName).collect(Collectors.toList());

    return String.join(", ", parameterNames);

  }

  private static String methodParameters(Method method) {
    StringBuilder methodParametersBuilder = new StringBuilder();

    Parameter[] parameters = method.getParameters();

    for (int i = 0; i < parameters.length - 1; i++) {
      methodParametersBuilder.append(methodParameter(parameters[i]));
      methodParametersBuilder.append(", ");
    }

    if (!(parameters.length < 1)) {
      methodParametersBuilder.append(methodParameter(parameters[parameters.length - 1]));
    }

    return methodParametersBuilder.toString();
  }

  private static String methodParameter(Parameter parameter) {
    StringBuilder methodParameterBuilder = new StringBuilder();

    methodParameterBuilder.append(resolveType(parameter.getParameterizedType()));
    methodParameterBuilder.append(" ");
    methodParameterBuilder.append(parameter.getName());

    return methodParameterBuilder.toString();
  }

  private static boolean methodNotExcluded(Method method) {
    return !EXCLUDED_METHODS.contains(method.getName());
  }

  private static String classInjection(Class<?> aClass) {
    return indent() + "@Inject\n"
        + indent() + "protected " + extractClassName(aClass) + " "
        + extractClassName(aClass).toLowerCase() + ";\n";
  }

  private static String classLine(Class<?> aClass) {
    return "public abstract class " + extractClassName(aClass) + "BaseInvoker extends BaseServiceInvoker {\n";
  }

  private static String imports(Class<?> aClass) {
    Set<String> neededImports = getNeededImports(aClass);

    StringBuilder importsBuilder = new StringBuilder();
    neededImports.forEach(className -> importsBuilder.append("import ").append(className).append(";\n"));
    return importsBuilder.toString();
  }

  private static Set<String> getNeededImports(Class<?> aClass) {
    Set<String> neededImports = new HashSet<>();
    neededImports.add("com.gepardec.cheetunit.test.BaseServiceInvoker");
    neededImports.add("javax.inject.Inject");

    for (Method method : aClass.getMethods()) {
      neededImports.add(method.getReturnType().getName());
      for (Class<?> parameterType : method.getParameterTypes()) {
        neededImports.add(parameterType.getName());
      }
    }

    return neededImports.stream()
        .filter(classname -> !EXCLUDED_IMPORT_PRIMITIVES.contains(classname))
        .filter(classname -> !EXCLUDED_IMPORT_PACKAGES.contains(extractPackageName(classname)))
        .map(ClassUtils::getNameOfClassOrNameOfArrayType)
        .collect(Collectors.toSet());
  }

  private static String indent() {
    return "  ";
  }


  private static String packageLine(Class<?> aClass) {
    return "package " + ClassUtils.extractPackageName(aClass) + ";\n";
  }
}

/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.core;

import org.apache.commons.lang3.SerializationUtils;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CheetUnitExecutor {

    @Inject
    private BeanManager beanManager;

    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    private BeanManagerService beanManagerService;

    private CheetUnitClassloader cheetUnitClassloader;

    /**
     * Invokes requested method and returns serialized result
     */
    public String execute(ExecutionRequest executionRequest) {

        checkEnabled();

        prepareClassloader(executionRequest);
        Object primaryObject = instantiatePrimaryObject(executionRequest);

        Object result = invokeRequestedMethod(primaryObject, executionRequest);
        return Base64.getEncoder().encodeToString(SerializationUtils.serialize((Serializable) result));
    }

    private void checkEnabled() {
        boolean enabled = Boolean.getBoolean("cheetunit.enabled");
        if (!enabled) {
            throw new CheetUnitException("CheetUnit is not enabled on server. Please make sure you have the system property cheetunit.enabled set to true");
        }
    }

    private void prepareClassloader(ExecutionRequest executionRequest) {
        Map<String, byte[]> classMap = new HashMap<>();
        for (Map.Entry<String, String> classEntry : executionRequest.getClassMap().entrySet()) {
            String className = classEntry.getKey();
            String bytecodeInBase64 = classEntry.getValue();
            classMap.put(className, Base64.getDecoder().decode(bytecodeInBase64));
        }

        if (cheetUnitClassloader == null) {
            cheetUnitClassloader = CheetUnitClassloader.getNewInstance();
        }
        cheetUnitClassloader.feedClasses(classMap);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Object instantiatePrimaryObject(ExecutionRequest executionRequest) {
        Object primaryObject;
        Class<?> mainClass;
        try {
            mainClass = cheetUnitClassloader.loadClass(executionRequest.getPrimaryClassName());
            primaryObject = mainClass.newInstance();

            CreationalContext<?> creationalContext = beanManager.createCreationalContext(null);
            AnnotatedType annotatedType;
            synchronized (CheetUnitExecutor.class) {
                beanManagerService.cleanup(beanManager);
                annotatedType = beanManager.createAnnotatedType(mainClass);
                beanManager
                        .createInjectionTarget(annotatedType)
                        .inject(primaryObject, creationalContext);
            }
        } catch (Exception e) {
            throw new CheetUnitException("Instantiation error", e);
        }

        return primaryObject;
    }

    private Object invokeRequestedMethod(Object primaryObject, ExecutionRequest executionRequest) {
        Object result = null;
        boolean methodFound = false;

        List<Method> methods = Arrays.asList(primaryObject.getClass().getMethods());
        methods.addAll(Arrays.asList(primaryObject.getClass().getDeclaredMethods()));

        for (Method method : methods) {
            if (method.getName().equals(executionRequest.getMethodName())) {
                methodFound = true;
                try {
                    Object[] args = SerializationUtils.deserialize(Base64.getDecoder().decode(executionRequest.getArgs()));
                    result = method.invoke(primaryObject, args);
                } catch (Exception e) {

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    PrintStream printStream = new PrintStream(stream);
                    e.printStackTrace(printStream);
                    String stacktrace = stream.toString();

                    if (e instanceof InvocationTargetException) {
                        String messageWithStackTrace = e.getCause().getClass().getName() + ": " + e.getCause().getMessage() + System.lineSeparator() + stacktrace;
                        result = new CheetUnitException(messageWithStackTrace);
                    } else {
                        result = new CheetUnitException(stacktrace, e);
                    }
                }
            }

            if (!methodFound) {
                result = new CheetUnitException("Method " + executionRequest.getMethodName() + " is not found in " + primaryObject.getClass().getName());
            }
        }

        return result;
    }


}

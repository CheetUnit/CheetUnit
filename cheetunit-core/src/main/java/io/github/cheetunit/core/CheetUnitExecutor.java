/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.cheetunit.core;

import io.github.cheetunit.core.annotations.CheetUnitNoTransactionRequired;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Entry point class for the server side which starts the requested execution from the client side
 */
public class CheetUnitExecutor {

    @Inject
    private BeanManager beanManager;

    @Inject
    private TransactionSupport transactionSupport;

    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    private BeanManagerService beanManagerService;

    private CheetUnitClassloader cheetUnitClassloader;

    /**
     * Invokes requested method and returns serialized result
     */
    public SerializedObject execute(ExecutionRequest executionRequest) {

        try {
            verifyCheetUnitIsEnabled();

            prepareClassloader(executionRequest);
            Object primaryObject = instantiatePrimaryObject(executionRequest);
            Object result = invokeRequestedMethod(primaryObject, executionRequest);
            return SerializedObject.of(result);
        } catch (Exception e) {
            return SerializedObject.of(e);
        }
    }

    private void verifyCheetUnitIsEnabled() {
        boolean enabled = Boolean.getBoolean("cheetunit.enabled");
        if (!enabled) {
            throw new CheetUnitServerException("CheetUnit is not enabled on server. Please make sure you have the system property cheetunit.enabled set to true");
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
            primaryObject = mainClass.getDeclaredConstructor().newInstance();

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
            throw new CheetUnitServerException("Instantiation error", e);
        }

        return primaryObject;
    }

    private Object invokeRequestedMethod(Object primaryObject, ExecutionRequest executionRequest) {

        List<Method> methods = new ArrayList<>(Arrays.asList(primaryObject.getClass().getMethods()));
        methods.addAll(Arrays.asList(primaryObject.getClass().getDeclaredMethods()));

        Object[] args = deserializeArguments(executionRequest);

        for (Method method : methods) {
            if (method.getName().equals(executionRequest.getMethodName()) && methodParametersMatching(method, args)) {
                try {
                    if (mustBeExecutedInTransaction(method)) {
                        transactionSupport.beginTx();
                    }
                    Object object = method.invoke(primaryObject, args);
                    object = ProxyUnwrapper.unwrap(object, method.getReturnType());
                    if (mustBeExecutedInTransaction(method)) {
                        transactionSupport.commitTx();
                    }
                    return object;
                } catch (Exception e) {
                    if (mustBeExecutedInTransaction(method)) {
                        transactionSupport.rollbackTx();
                    }
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    PrintStream printStream = new PrintStream(stream);
                    e.printStackTrace(printStream);
                    String stacktrace = stream.toString();

                    prepareExceptionAndThrow(e, stacktrace);
                }
            }
        }

        throw new CheetUnitServerException("Method " + executionRequest.getMethodName() + " is not found in " + primaryObject.getClass().getName());
    }

    private void prepareExceptionAndThrow(Exception e, String stacktrace) {
        if (e instanceof InvocationTargetException) {
            String messageWithStackTrace = e.getCause().getClass().getName() + ": " + e.getCause().getMessage() + System.lineSeparator() + stacktrace;
            throw new CheetUnitServerException(messageWithStackTrace);
        } else {
            throw new CheetUnitServerException(stacktrace, e);
        }
    }

    private boolean mustBeExecutedInTransaction(Method method) {
        return !method.isAnnotationPresent(CheetUnitNoTransactionRequired.class) && !method.getDeclaringClass().isAnnotationPresent(CheetUnitNoTransactionRequired.class);
    }

    private Object[] deserializeArguments(ExecutionRequest executionRequest) {
        List<SerializedObject> args = executionRequest.getArgs();
        List<Object> arguments = new ArrayList<>();
        for (SerializedObject arg : args) {
            arguments.add(arg.toObject());
        }

        return arguments.toArray();
    }

    private boolean methodParametersMatching(Method method, Object[] args) {
        if(method.getParameterCount() != args.length) {
            return false;
        }

        for (int i = 0; i < method.getParameterTypes().length; i++) {
            if (!method.getParameterTypes()[i].equals(args[i].getClass())) {
                return false;
            }
        }

        return true;
    }
}

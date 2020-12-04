/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.invokergenerator.helpers;

import org.apache.maven.project.MavenProject;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaDocTag;
import org.jboss.forge.roaster.model.Parameter;
import org.jboss.forge.roaster.model.Visibility;
import org.jboss.forge.roaster.model.impl.MethodImpl;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.gepardec.cheetunit.invokergenerator.helpers.BaseInvokerContentCreator.EXCLUDED_METHODS;
import static com.gepardec.cheetunit.invokergenerator.helpers.ClassUtils.extractPackageName;

public abstract class BaseInvokerFileCreator {
    public static final String DELEGATE_FIELD_NAME = "delegate";

    public static void createBaseInvokers(String destinationFolder, List<Class<?>> classes) throws IOException {
        deleteAndCreateBaseDirectory(destinationFolder);

        for (Class<?> aClass : classes) {
            createBaseInvoker(destinationFolder, aClass);
        }
    }

    static void createBaseInvoker(String destinationFolder, Class<?> aClass) throws IOException {

//        Roaster.create(aClass);

        // TODO beautify this!!!!
        String javaClassPath = aClass.getResource(aClass.getSimpleName() + ".class").getFile()
                .replace("target/classes", "src/main/java")
                .replace("target/test-classes", "src/test/java")
                .replace(".class", ".java");

        JavaClassSource javaClassSource = Roaster.parse(JavaClassSource.class, new File(javaClassPath));
        javaClassSource.setPackage("blubb");
        javaClassSource.getImports().forEach(javaClassSource::removeImport);
        javaClassSource.removeJavaDoc();
        javaClassSource.removeAllAnnotations();
        removeAllFields(javaClassSource);
        addDelegateField(javaClassSource, aClass);
        removePrivateMethods(javaClassSource);
        javaClassSource.getMethods().forEach(m -> {
            // 1st try but inheritance doesnt work
//            setMethodJavaDoc(m);
//            m.setBody(String.format("return " + DELEGATE_FIELD_NAME + ".%s(%s);", m.getName(), formatParameters(m)));

            // 2nd try -> same issue inheritance
//            javaClassSource.removeMethod(m);
//            // another try to create a new instance which is added to the javaClassSource
//            javaClassSource.addMethod(createMethod(m, javaClassSource));
        });

        // 3rd try via reflection
        javaClassSource.getMethods().forEach(javaClassSource::removeMethod);
        Arrays.stream(aClass.getMethods())
                .filter(m -> !EXCLUDED_METHODS.contains(m.getName()))
                .forEach(reflectionMethod -> {
                    MethodImpl<JavaClassSource> method = new MethodImpl<>(javaClassSource, reflectionMethod);
                    setMethodJavaDoc(method);
                    method.setBody(String.format(
                            (method.getReturnType().getName().equals("void") ? "" : "return ")
                                    + DELEGATE_FIELD_NAME + ".%s(%s);", method.getName(), formatParameters(method)));
                    javaClassSource.addMethod(method);
                });


        // change name as last
        javaClassSource.setName(javaClassSource.getName() + "BaseInvoker");
//        javaClassSource.
        // change name as last step
        System.out.println(javaClassSource);


//        String packageDirectoryPath = createPackageFolderAndGetPath(destinationFolder, aClass);
//
//        File baseInvokerFile = createBaseInvokerJavaFile(packageDirectoryPath, aClass);
//
//        writeContentToFile(baseInvokerFile, aClass);

        // TODO implement getAdditionalClasses override
    }

    private static MethodImpl<JavaClassSource> createMethod(MethodSource<JavaClassSource> methodToCopy, JavaClassSource javaClassSource) {
        javaClassSource.addImport(methodToCopy.getReturnType().getClass());

        MethodImpl<JavaClassSource> newMethod = new MethodImpl<>(javaClassSource);
        newMethod
                .setName(methodToCopy.getName())
                .setDefault(methodToCopy.isDefault())
                .setSynchronized(methodToCopy.isSynchronized())
                .setNative(methodToCopy.isNative())
                .setReturnType(methodToCopy.getReturnType())
                .setBody(String.format("return " + DELEGATE_FIELD_NAME + ".%s(%s);", methodToCopy.getName(), formatParameters(methodToCopy)))
                .setConstructor(methodToCopy.isConstructor())
                .setAbstract(methodToCopy.isAbstract())
                .setVisibility(methodToCopy.getVisibility())
                .setFinal(methodToCopy.isFinal())
                .setStatic(methodToCopy.isStatic());

        methodToCopy.getParameters().forEach(p -> newMethod.addParameter(p.getType().getName(), p.getName()));
        methodToCopy.getThrownExceptions().forEach(newMethod::addThrows);

        setMethodJavaDoc(newMethod);
        return newMethod;
    }

    private static void substituteBodyWithDelegate(MethodSource<JavaClassSource> m) {
        m.setBody(String.format("return " + DELEGATE_FIELD_NAME + ".%s(%s);", m.getName(), formatParameters(m)));
    }

    private static void removePrivateMethods(JavaClassSource javaClassSource) {
        javaClassSource.getMethods().stream()
                .filter(m -> m.getVisibility() == Visibility.PRIVATE)
                .forEach(javaClassSource::removeMethod);
    }

    private static void addDelegateField(JavaClassSource javaClassSource, Class<?> aClass) {
        javaClassSource.addField()
                .setName(DELEGATE_FIELD_NAME)
                .setType(aClass)
                .setProtected()
                .addAnnotation(Inject.class);
    }

    private static void removeAllFields(JavaClassSource javaClassSource) {
        javaClassSource.getFields().forEach(javaClassSource::removeField);
    }

    private static void setMethodJavaDoc(MethodSource<JavaClassSource> method) {
        if (method.hasJavaDoc()) {
            method.removeJavaDoc();
        }
        method.getJavaDoc().addTagValue(new JavaDocSeeReference(method));
    }

    private static String formatParametersForJavaDoc(MethodSource<JavaClassSource> method) {
        return method.getParameters().stream().map(p -> p.getType().getName()).collect(Collectors.joining(", "));
    }

    private static String formatParameters(org.jboss.forge.roaster.model.source.MethodSource<JavaClassSource> method) {
        return method.getParameters().stream().map(Parameter::getName).collect(Collectors.joining(", "));
    }

    private static void writeContentToFile(File baseInvokerFile, Class<?> aClass) throws IOException {
        FileWriter contentWriter = new FileWriter(baseInvokerFile);
        BaseInvokerContentCreator.getBaseInvokerContent(aClass);
        contentWriter.write(BaseInvokerContentCreator.getBaseInvokerContent(aClass));
        contentWriter.close();
    }

    private static File createBaseInvokerJavaFile(String packageDirectoryPath, Class<?> aClass) throws IOException {
        File invokerJavaFile = new File(packageDirectoryPath + File.separator + aClass.getSimpleName() + "BaseInvoker.java");

        invokerJavaFile.createNewFile();

        return invokerJavaFile;
    }

    private static String createPackageFolderAndGetPath(String destinationFolder, Class<?> aClass) {
        String packagePath = getPackagePath(destinationFolder, aClass);
        File packageDir = new File(packagePath);

        if (!packageDir.exists()) {
            packageDir.mkdirs();
        }

        return packagePath;
    }

    private static String getPackagePath(String destinationFolder, Class<?> aClass) {
        String packageFolderStructure = extractPackageName(aClass).replace(".", File.separator);

        return getDestinationPath(destinationFolder) + File.separator + packageFolderStructure;
    }

    private static void deleteAndCreateBaseDirectory(String destinationFolder) {
        File dir = new File(getDestinationPath(destinationFolder));

        if (dir.exists()) {
            deleteDirectory(new File(destinationFolder));
        }
        dir.mkdirs();
    }

    private static String getDestinationPath(String destinationFolder) {
        return destinationFolder;
    }

    private static String getBaseDirectoryPath(MavenProject project) {
        return project.getBasedir().getAbsolutePath();
    }

    private static void deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directoryToBeDeleted.delete();
    }

    static class JavaDocSeeReference implements JavaDocTag {

        private final MethodSource<JavaClassSource> method;

        public JavaDocSeeReference(MethodSource<JavaClassSource> method) {
            this.method = method;
        }

        @Override
        public String getName() {
            return "@see";
        }

        @Override
        public String getValue() {
            return String.format("%s#%s(%s)", method.getOrigin().getName(), method.getName(), formatParametersForJavaDoc(method));
        }

        @Override
        public Object getInternal() {
            return null;
        }
    }

}

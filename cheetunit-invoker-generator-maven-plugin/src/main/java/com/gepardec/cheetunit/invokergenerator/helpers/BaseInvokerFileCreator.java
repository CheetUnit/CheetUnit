/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.invokergenerator.helpers;

import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static com.gepardec.cheetunit.invokergenerator.helpers.ClassUtils.extractClassName;
import static com.gepardec.cheetunit.invokergenerator.helpers.ClassUtils.extractPackageName;

public abstract class BaseInvokerFileCreator {
  public static void createBaseInvokers(String destinationFolder, List<Class<?>> classes) throws IOException {
    deleteAndCreateBaseDirectory(destinationFolder);

    for (Class<?> aClass : classes) {
      createBaseInvoker(destinationFolder, aClass);
    }
  }

  private static void createBaseInvoker(String destinationFolder, Class<?> aClass) throws IOException {
    String packageDirectoryPath = createPackageFolderAndGetPath(destinationFolder, aClass);

    File baseInvokerFile = createBaseInvokerJavaFile(packageDirectoryPath, aClass);

    writeContentToFile(baseInvokerFile, aClass);
  }

  private static void writeContentToFile(File baseInvokerFile, Class<?> aClass) throws IOException {
      FileWriter contentWriter = new FileWriter(baseInvokerFile);
      BaseInvokerContentCreator.getBaseInvokerContent(aClass);
      contentWriter.write(BaseInvokerContentCreator.getBaseInvokerContent(aClass));
      contentWriter.close();
  }

  private static File createBaseInvokerJavaFile(String packageDirectoryPath, Class<?> aClass) throws IOException {
    File invokerJavaFile = new File(packageDirectoryPath + File.separator + extractClassName(aClass) + "BaseInvoker.java");

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
    return destinationFolder
        + File.separator + "test"
        + File.separator + "java";
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


}

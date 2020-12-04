/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.invokergenerator;

import com.gepardec.cheetunit.invokergenerator.helpers.BaseInvokerFileCreator;
import org.apache.maven.SessionScoped;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.classworlds.realm.ClassRealm;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.gepardec.cheetunit.invokergenerator.helpers.ClassFinder.getClasses;

@Mojo(name = "generate-base-invokers", defaultPhase = LifecyclePhase.GENERATE_TEST_SOURCES)
public class BaseInvokerGeneratorMojo
    extends AbstractMojo {

  private static final List<Class<? extends Annotation>> SCOPE_ANNOTATIONS = Arrays.asList(
      RequestScoped.class,
      SessionScoped.class,
      ApplicationScoped.class,
      Dependent.class,
      ConversationScoped.class
      );

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  @Parameter(defaultValue = "${plugin}", readonly = true)
  private PluginDescriptor pluginDescriptor;

  @Parameter(required = true)
  private String destinationFolder;

  @Parameter(required = true, property = "packageToScan")
  private List<String> packagesToScan;

  public void execute()
      throws MojoExecutionException {
    logIntro();

    addSourceClassesToRealm();

    List<Class<?>> allSourceClasses = findAllClassesFromPackagesToScan();

    List<Class<?>> scopeAnnotatedClasses = getScopeAnnotatedClasses(allSourceClasses);

    if(!scopeAnnotatedClasses.isEmpty()) {
      createBaseInvokers(scopeAnnotatedClasses);
    }

    logEnd();
  }

  private void createBaseInvokers(List<Class<?>> scopeAnnotatedClasses) throws MojoExecutionException {
    try {
      BaseInvokerFileCreator.createBaseInvokers(destinationFolder, scopeAnnotatedClasses);
    } catch (IOException e) {
      e.printStackTrace();
      throw new MojoExecutionException(e.getMessage());
    }
  }

  private List<Class<?>> findAllClassesFromPackagesToScan() throws MojoExecutionException {
    List<Class<?>> allSourceClasses = new ArrayList<>();
    for (String packageToScan : packagesToScan) {
      allSourceClasses.addAll(getClasses(packageToScan));
    }
    return allSourceClasses;
  }

  private void logEnd() {
    getLog().info("Finished generating Base Invokers");
    logSpacer();
  }

  private void logIntro() {
    logSpacer();
    getLog().info("Start generating Base Invokers");
  }

  private void logSpacer() {
    getLog().info("------------------------------------------------------------------------");
  }

  private List<Class<?>> getScopeAnnotatedClasses(List<Class<?>> classes) {
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

  private void addSourceClassesToRealm() throws MojoExecutionException {
    try {
      File buildDirectory = new File(project.getBuild().getOutputDirectory());
      ClassRealm realm = pluginDescriptor.getClassRealm();
      realm.addURL(buildDirectory.toURI().toURL());
    } catch (MalformedURLException e) {
      e.printStackTrace();
      throw new MojoExecutionException(e.getMessage());
    }
  }
}

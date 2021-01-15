/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.core;

import java.util.HashMap;
import java.util.Map;

class CheetUnitClassloader extends ClassLoader {

    private Map<String, byte[]> knownClasses;

    CheetUnitClassloader(ClassLoader parent, Map<String, byte[]> classes) {
        super(parent);
        knownClasses = classes;
    }

    /**
     * Feed class names with the corresponding bytecodes to the class loader, such that they can be defined from theirs byte codes.
     */
    void feedClasses(Map<String, byte[]> classes) {
        if (knownClasses == null) {
            knownClasses = new HashMap<>();
        }

        knownClasses.putAll(classes);
    }

    /**
     * Loads a class by its name. It overrides the corresponding method of default class loader.
     * <ol>
     *     <li>
     *         If the class is not known to cheetunit, e.g. it comes not from a test, the loading is delegated to the default class loader.
     *     </li>
     *     <li>
     *         If the class already loaded and so can be found, the further steps are skipped.
     *     </li>
     *     <li>
     *         If the class is not loaded and known to cheetunit, it will be defined from the bytecode and loaded.
     *     </li>
     * </ol>
     */
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (!knownClasses.containsKey(name)) {
            return super.loadClass(name, resolve);
        }

        Class<?> loadedClass = findLoadedClass(name);

        if (loadedClass != null) {
            return loadedClass;
        }

        byte[] bytecode = knownClasses.get(name);

        return defineClass(name, bytecode, 0, bytecode.length);
    }

    static CheetUnitClassloader getNewInstance() {
        ClassLoader parent = CheetUnitClassloader.class.getClassLoader();
        return new CheetUnitClassloader(parent, new HashMap<>());
    }
}


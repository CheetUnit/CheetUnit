/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.rightPad;

/**
 * <p>Resolves the given property fileName in the classpath and adds all properties to the system properties.</p>
 * If a property is already set (e.g. passed as a cli parameter) the files property will be ignored.
 */
class CheetUnitPropertyResolver {

    private static final Logger LOG = LoggerFactory.getLogger(CheetUnitProperties.class);

    private final String fileName;

    CheetUnitPropertyResolver(String fileName) {
        this.fileName = fileName;
    }

    void resolveProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {

            Properties prop = loadPropertiesFromInputStream(input);

            setSystemProperties(prop);

            logEffectiveCheetUnitProperties();
        } catch (IOException ex) {
            throw new CheetUnitClientException("Something unexpected happened while loading properties", ex);
        }
    }

    private Properties loadPropertiesFromInputStream(InputStream input) throws IOException {
        Properties prop = new Properties();
        if (input == null) {
            LOG.error("Sorry, unable to find property file {} in classpath.", fileName);
            throw new CheetUnitClientException(String.format("No property file with name %s in classpath found.", fileName));
        }
        prop.load(input);
        return prop;
    }

    private void logEffectiveCheetUnitProperties() {
        LOG.info("*** Effective cheetunit properties ***");
        System.getProperties().entrySet().stream()
                .filter(entry -> entry.getKey().toString().startsWith("cheetunit"))
                .forEach(entry -> LOG.info("{} : {}", rightPad(entry.getKey().toString(), 40), entry.getValue()));
    }

    private void setSystemProperties(Properties prop) {
        prop.forEach((key, value) -> {
            if (System.getProperty(key.toString()) == null) {
                System.setProperty(key.toString(), value.toString());
            } else {
                LOG.debug("Property {} is already set, property file value {} will be ignored.", key, value);
            }
        });
    }
}

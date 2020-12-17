/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.examples.college.service;

import com.gepardec.cheetunit.examples.college.domain.Person;
import com.gepardec.cheetunit.test.BaseServiceInvoker;
import com.gepardec.cheetunit.test.CheetUnitConfig;

import javax.inject.Inject;
import java.util.List;

public class PersonServiceInvoker extends BaseServiceInvoker {

    @Inject
    private PersonService service;

    public void getAllPersons() {
        System.out.println(service.getAllPersons());
    }

    @Override
    public CheetUnitConfig getConfig() {
        // TODO use properties instead of override of getConfig
        return new CheetUnitConfig("http", "localhost", "8080", "college-example/rest/cheetunit-insider", getAdditionalClasses());
    }
}

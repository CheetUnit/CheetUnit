/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.examples.college.eager.service;

import com.gepardec.cheetunit.core.annotations.CheetUnitNoTransactionRequired;
import com.gepardec.cheetunit.examples.college.eager.domain.Person;
import com.gepardec.cheetunit.test.BaseServiceInvoker;

import javax.inject.Inject;
import java.util.List;

public class PersonServiceInvoker extends BaseServiceInvoker {

    @Inject
    private PersonService service;

    @CheetUnitNoTransactionRequired
    public List<Person> getAllPersons() {
        List<Person> allPersons = service.getAllPersons();
        return allPersons;
    }

    @CheetUnitNoTransactionRequired
    public Person getPersonById(Long id) {
       return service.getPersonById(id);
    }
}

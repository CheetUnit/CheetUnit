/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.examples.college.service;

import com.gepardec.cheetunit.examples.college.domain.Person;
import com.gepardec.cheetunit.test.CheetUnit;
import com.thoughtworks.xstream.XStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class PersonServiceIT {

    private static PersonServiceInvoker personService;

    @BeforeAll
    static void beforeAll() {
        personService = CheetUnit.createProxy(PersonServiceInvoker.class);
    }


    @Test
    void getAllPersons() {
        List<Person> persons = personService.getAllPersons();
        System.out.println(new XStream().toXML(persons));
    }
}
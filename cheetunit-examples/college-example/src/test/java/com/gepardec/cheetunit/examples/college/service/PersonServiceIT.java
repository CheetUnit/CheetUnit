/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.examples.college.service;

import com.gepardec.cheetunit.examples.college.domain.Person;
import com.gepardec.cheetunit.test.CheetUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonServiceIT {

    private static PersonServiceInvoker personService;

    @BeforeAll
    static void beforeAll() {
        personService = CheetUnit.createProxy(PersonServiceInvoker.class);
    }


    @Test
    void testGetAllPersons() {
        List<Person> persons = personService.getAllPersons();
        assertEquals(6, persons.size());
        assertEquals(1, persons.stream().filter(person -> person.getId() == -2L).map(Person::getCoursesTeached).count());
    }

    @Test
    void testGetAPerson() {
        Person person = personService.getPersonById(-4L);
        assertNotNull(person);
        assertEquals(2, person.getCoursesParticipating().size());
        assertTrue(person.getCoursesTeached().isEmpty());
    }
}

/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.examples.school.web;

import com.gepardec.cheetunit.examples.school.domain.Person;
import com.gepardec.cheetunit.examples.school.service.PersonService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class AllPersonsPageController {

    @Inject
    private PersonService personService;

    private List<Person> allPersons;

    private Person newPerson;

    @PostConstruct
    public void init() {
        reloadPersons();
        newPerson = new Person();
    }

    public void reloadPersons() {
        allPersons = personService.getAllPersons();
    }

    public List<Person> getAllPersons() {
        return allPersons;
    }

    public PersonService getPersonService() {
        return personService;
    }

    public Person getNewPerson() {
        return newPerson;
    }

    public void saveNewPerson() {
        personService.addPerson(newPerson);
        reloadPersons();
        newPerson = new Person();
    }
}

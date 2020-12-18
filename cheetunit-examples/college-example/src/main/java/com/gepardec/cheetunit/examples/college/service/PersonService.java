/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.examples.college.service;

import com.gepardec.cheetunit.examples.college.domain.Person;
import com.gepardec.cheetunit.examples.college.domain.dao.PersonDao;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

@RequestScoped
public class PersonService {

    @Inject
    private PersonDao personDao;

    public List<Person> getAllPersons() {
        return personDao.findAll();
    }

    public Person getPersonById(Long id) {
        return personDao.findById(id);
    }

    public void addPerson(Person person) {
        personDao.create(person);
    }
}

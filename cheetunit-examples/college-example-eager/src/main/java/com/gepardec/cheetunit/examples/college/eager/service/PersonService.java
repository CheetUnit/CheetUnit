    /*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.examples.college.eager.service;

import com.gepardec.cheetunit.examples.college.eager.domain.Person;
import com.gepardec.cheetunit.examples.college.eager.domain.dao.PersonDao;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;

@Dependent
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

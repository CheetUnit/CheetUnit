/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.examples.college.eager.web;

import com.gepardec.cheetunit.examples.college.eager.domain.Person;
import com.gepardec.cheetunit.examples.college.eager.service.PersonService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class PersonPageController {

    @Inject
    private PersonService personService;

    private Person person;

    private Long personId;

    public Person getPerson() {
        if(this.person == null) {
            this.person = personService.getPersonById(personId);
        }
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }
}

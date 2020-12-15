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
import javax.transaction.Transactional;
import java.util.List;

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

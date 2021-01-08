/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.examples.college.eager.web;

import com.gepardec.cheetunit.examples.college.eager.domain.Course;
import com.gepardec.cheetunit.examples.college.eager.domain.Person;
import com.gepardec.cheetunit.examples.college.eager.service.CourseService;
import com.gepardec.cheetunit.examples.college.eager.service.PersonService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Named
@RequestScoped
public class PersonPageController {

    @Inject
    private PersonService personService;

    @Inject
    private CourseService courseService;

    private Person person;

    private Long personId;

    public Person getPerson() {
        if (this.person == null) {
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

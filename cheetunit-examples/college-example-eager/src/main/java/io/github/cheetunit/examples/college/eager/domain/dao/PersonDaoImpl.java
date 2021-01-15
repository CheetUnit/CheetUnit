/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.cheetunit.examples.college.eager.domain.dao;

import io.github.cheetunit.examples.college.eager.domain.Person;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PersonDaoImpl extends AbstractDao<Person> implements PersonDao{

    @Override
    public Class<Person> getEntityClass() {
        return Person.class;
    }
}

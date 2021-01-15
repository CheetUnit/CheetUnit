/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.cheetunit.examples.college.eager.domain.dao;

import io.github.cheetunit.examples.college.eager.domain.Course;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CourseDaoImpl extends AbstractDao<Course> implements CourseDao{

    @Override
    public Class<Course> getEntityClass() {
        return Course.class;
    }
}

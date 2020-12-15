/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.examples.school.domain.dao;

import com.gepardec.cheetunit.examples.school.domain.Course;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CourseDaoImpl extends AbstractDao<Course> implements CourseDao{

    @Override
    public Class<Course> getEntityClass() {
        return Course.class;
    }
}

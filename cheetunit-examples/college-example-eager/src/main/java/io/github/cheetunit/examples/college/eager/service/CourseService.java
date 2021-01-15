/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.cheetunit.examples.college.eager.service;

import io.github.cheetunit.examples.college.eager.domain.Course;
import io.github.cheetunit.examples.college.eager.domain.dao.CourseDao;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;

@Dependent
public class CourseService {

    @Inject
    private CourseDao courseDao;

    public List<Course> getAllCourses() {
        return courseDao.findAll();
    }

    public Course getCourseById(Long id) {
        return courseDao.findById(id);
    }

    public void addCourse(Course course) {
        courseDao.create(course);
    }
}

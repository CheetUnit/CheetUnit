/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.examples.school.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Person")
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @OneToMany(mappedBy = "teacher")
    private Set<Course> coursesTeached = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "participants")
    private Set<Course> coursesParticipating = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Course> getCoursesTeached() {
        return coursesTeached;
    }

    public void addToCoursesTeached(final Course course) {
        if(getCoursesTeached().add(course)) {
            course.setTeacher(this);
        }
    }

    public void removeFromCoursesTeached(final Course course) {
        if(getCoursesTeached().remove(course)) {
            course.setTeacher(null);
        }
    }

    public Set<Course> getCoursesParticipating() {
        return coursesParticipating;
    }

    public void addToCoursesParticipating(final Course course) {
        this.coursesParticipating.add(course);
        course.getParticipants().add(this);
    }

    public void removeFromCoursesParticipating(final Course course) {
        this.coursesParticipating.remove(course);
        course.getParticipants().remove(this);
    }
}

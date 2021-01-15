/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.cheetunit.examples.college.eager.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long year;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name= "teacher_id", nullable = false)
    private Person teacher;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "course_participation",
            joinColumns = { @JoinColumn(name = "course_id")},
            inverseJoinColumns = { @JoinColumn(name = "person_id") }
    )
    private Set<Person> participants = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Person getTeacher() {
        return teacher;
    }

    public void setTeacher(Person teacher) {
        if(teacher == this.teacher) {
            return;
        }
        final Person oldTeacher = this.teacher;

        this.teacher = teacher;
        if(oldTeacher != null) {
            oldTeacher.removeFromCoursesTeached(this);
        }
        if(teacher != null) {
            teacher.addToCoursesTeached(this);
        }
    }

    public Set<Person> getParticipants() {
        return participants;
    }

    public void addParticipant(final Person participant) {
        this.participants.add(participant);
        participant.getCoursesParticipating().add(this);
    }

    public void removeParticipant(final Person participant) {
        this.participants.remove(participant);
        participant.getCoursesParticipating().remove(this);
    }
}

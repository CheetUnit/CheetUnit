/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.examples.college.domain.dao;

import com.gepardec.cheetunit.examples.college.domain.DomainException;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class BaseDao<E> implements Dao<E> {

    private Class<E> persistentClass;

    @PersistenceContext(unitName = "cheetunit")
    private EntityManager em;

    @PostConstruct
    public void init() {
        this.persistentClass = (Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected Class<E> getPersistentClass() {
        return persistentClass;
    }


    @Override
    public E findById(Long id) {
        try {
            return em.find(getPersistentClass(), id);
        } catch (Exception e) {
            throw new DomainException(e);
        }
    }

    @Override
    public List<E> findAll() {
        try {
            return em.createQuery("select e from " + getPersistentClass().getSimpleName() + " e", getPersistentClass()).getResultList();
        } catch (Exception e) {
            throw new DomainException(e);
        }
    }

    @Override
    public E update(E entity) {
        try {
            return em.merge(entity);
        } catch (Exception e) {
            throw new DomainException(e);
        }
    }

    @Override
    public E create(E entity) {
        try {
            em.persist(entity);
            return entity;
        } catch (Exception e) {
            throw new DomainException(e);
        }
    }

    @Override
    public void remove(E entity) {
        try {
            em.remove(update(entity));
        } catch (Exception e) {
            throw new DomainException(e);
        }
    }
}

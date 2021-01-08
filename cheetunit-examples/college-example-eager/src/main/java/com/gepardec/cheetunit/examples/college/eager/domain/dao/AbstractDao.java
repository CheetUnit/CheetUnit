/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.examples.college.eager.domain.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.util.List;

public abstract class AbstractDao<E extends Object> {

    @Inject
    private EntityManager entityManager;

    @Inject
    protected UserTransaction userTx;

    public abstract Class<E> getEntityClass();

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public E findById(Long id) {
        try {
            E entity;
            try {
                userTx.begin();
                entity = getEntityManager().find(getEntityClass(), id);
            } finally {
                userTx.commit();
            }
            return entity;
        } catch (Exception e) {
            try {
                userTx.rollback();
            } catch (SystemException systemException) {
                throw new RuntimeException(systemException);
            }
            throw new RuntimeException(e);
        }
    }

    public List<E> findAll() {
        try {
            List<E> entities;
            try {
                userTx.begin();
                entities = getEntityManager().createQuery("select e from " + getEntityClass().getSimpleName() + " e", getEntityClass()).getResultList();
            } finally {
                userTx.commit();
            }
            return entities;
        } catch (Exception e) {
            try {
                userTx.rollback();
            } catch (SystemException systemException) {
                throw new RuntimeException(systemException);
            }
            throw new RuntimeException(e);
        }
    }

    public E update(E entity) {
        try {
            E merge;
            try {
                userTx.begin();
                merge = getEntityManager().merge(entity);
            } finally {
                userTx.commit();
            }
            return merge;
        } catch (Exception e) {
            try {
                userTx.rollback();
            } catch (SystemException systemException) {
                throw new RuntimeException(systemException);
            }
            throw new RuntimeException(e);
        }
    }

    public E create(E entity) {
        try {
            try {
                userTx.begin();
                getEntityManager().persist(entity);
            } finally {
                userTx.commit();
            }
        } catch (Exception e) {
            try {
                userTx.rollback();
            } catch (SystemException systemException) {
                throw new RuntimeException(systemException);
            }
            throw new RuntimeException(e);
        }
        return entity;
    }

    public void remove(E entity) {
        try {
            try {
                userTx.begin();
                E merge = getEntityManager().merge(entity);
                getEntityManager().remove(merge);
            } finally {
                userTx.commit();
            }
        } catch (Exception e) {
            try {
                userTx.rollback();
            } catch (SystemException systemException) {
                throw new RuntimeException(systemException);
            }
            throw new RuntimeException(e);
        }
    }
}

/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.cheetunit.examples.college.eager.domain.dao;

import javax.persistence.EntityManager;
import java.util.List;

public interface Dao<E extends Object> {

    Class<E> getEntityClass();

    EntityManager getEntityManager();

    E findById(Long id);

    List<E> findAll();

    E update(E entity);

    E create(E entity);

    void remove(E entity);
}

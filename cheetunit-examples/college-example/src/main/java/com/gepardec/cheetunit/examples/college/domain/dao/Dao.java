/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.examples.college.domain.dao;

import java.util.List;

public interface Dao<E extends Object> {
    E findById(Long id);
    List<E> findAll();
    E update(E entity);
    E create(E entity);
    void remove(E entity);
    void detach(E entity);
}

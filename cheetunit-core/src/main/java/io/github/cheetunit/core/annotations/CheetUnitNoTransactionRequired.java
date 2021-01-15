/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.cheetunit.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CheetUnit will not open a new transaction when this annotation is used on invoker-method or invoker-class.
 *
 * Use this annotation if you manage your transactions manually. You don't need to use it, if your transactions are
 * managed by container.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface CheetUnitNoTransactionRequired {
}

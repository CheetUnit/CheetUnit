/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.cheetunit.core;

/**
 * Wraps all exception which may occur on the server side.
 */
public class CheetUnitServerException extends RuntimeException {

    public CheetUnitServerException(String message) {
        super(message);
    }

    public CheetUnitServerException(String message, Throwable cause) {
        super(message, cause);
    }
}

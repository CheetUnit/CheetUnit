/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.core;

public class CheetUnitException extends RuntimeException {

    public CheetUnitException() {
    }

    public CheetUnitException(String message) {
        super(message);
    }

    public CheetUnitException(String message, Throwable cause) {
        super(message, cause);
    }
}

/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.test;

/**
 * Wraps all exception which may occur on the client side.
 */
public class CheetUnitClientException extends RuntimeException {

    public CheetUnitClientException(String message) {
        super(message);
    }

    public CheetUnitClientException(Throwable cause) {
        super(cause);
    }

    public CheetUnitClientException(String message, Throwable cause) {
        super(message, cause);
    }
}

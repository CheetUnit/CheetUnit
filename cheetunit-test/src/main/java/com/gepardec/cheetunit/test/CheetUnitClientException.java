/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.test;

/**
 * Wraps all exception which may occur on the client side.
 */
public class CheetUnitClientException extends RuntimeException {

    CheetUnitClientException(String message) {
        super(message);
    }

    CheetUnitClientException(Throwable cause) {
        super(cause);
    }

    CheetUnitClientException(String message, Throwable cause) {
        super(message, cause);
    }
}

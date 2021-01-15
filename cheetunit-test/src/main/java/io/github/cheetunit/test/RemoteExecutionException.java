/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.cheetunit.test;

/**
 * Wraps unexpected exceptions which may occur on the server side
 */
public class RemoteExecutionException extends RuntimeException {

    RemoteExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}

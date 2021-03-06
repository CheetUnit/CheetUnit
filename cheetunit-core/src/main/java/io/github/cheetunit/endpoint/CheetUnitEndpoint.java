/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.cheetunit.endpoint;

import io.github.cheetunit.core.ExecutionRequest;
import io.github.cheetunit.core.SerializedObject;

public interface CheetUnitEndpoint {

    String CHEETUNIT_ENDPOINT = "/cheetunit-insider";

    /**
     * executes request call and returns serialized result
     */
    SerializedObject execute(ExecutionRequest executionRequest);
}

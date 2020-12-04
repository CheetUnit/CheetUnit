/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.endpoint;

import com.gepardec.cheetunit.core.ExecutionRequest;
import com.gepardec.cheetunit.core.SerializedObject;

public interface CheetUnitEndpoint {

    /**
     * executes request call and returns serialized result
     */
    SerializedObject execute(ExecutionRequest executionRequest);
}
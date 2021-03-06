/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.cheetunit.endpoint;

import io.github.cheetunit.core.CheetUnitExecutor;
import io.github.cheetunit.core.ExecutionRequest;
import io.github.cheetunit.core.SerializedObject;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(CheetUnitEndpoint.CHEETUNIT_ENDPOINT)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CheetUnitRESTEndpoint implements CheetUnitEndpoint {

    @Inject
    private CheetUnitExecutor executor;

    @Override
    @POST
    public SerializedObject execute(ExecutionRequest executionRequest) {
        return executor.execute(executionRequest);
    }
}

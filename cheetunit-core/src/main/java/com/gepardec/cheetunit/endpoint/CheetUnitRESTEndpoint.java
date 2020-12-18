/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.endpoint;

import com.gepardec.cheetunit.core.CheetUnitExecutor;
import com.gepardec.cheetunit.core.ExecutionRequest;
import com.gepardec.cheetunit.core.SerializedObject;

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
    private CheetUnitExecutor executer;

    @Override
    @POST
    public SerializedObject execute(ExecutionRequest executionRequest) {
        return executer.execute(executionRequest);
    }
}

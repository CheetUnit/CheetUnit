/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.core;

import java.util.List;
import java.util.UUID;

public class ComplexClass {
    private List<UUID> uuids;
    private SimpleClass[] simplies;

    public List<UUID> getUuids() {
        return uuids;
    }

    public void setUuids(List<UUID> uuids) {
        this.uuids = uuids;
    }

    public SimpleClass[] getSimplies() {
        return simplies;
    }

    public void setSimplies(SimpleClass[] simplies) {
        this.simplies = simplies;
    }
}

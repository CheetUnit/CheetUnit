/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.invokergenerator.helpers.testclasses.pojo;

public class GreetingValue {
    Salutation salutation;
    String name;

    public GreetingValue(Salutation salutation, String name) {
        this.salutation = salutation;
        this.name = name;
    }

    @Override
    public String toString() {
        return salutation.getValue() + " " + name;
    }
}

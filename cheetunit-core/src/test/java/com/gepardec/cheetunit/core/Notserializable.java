/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.core;

import java.time.LocalDate;

public class Notserializable {
    private LocalDate[] localDates;

    public LocalDate[] getLocalDates() {
        return localDates;
    }

    public void setLocalDates(LocalDate[] localDates) {
        this.localDates = localDates;
    }
}

/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.core;

import java.time.YearMonth;
import java.util.HashSet;
import java.util.Set;

public class VeryComplexClass extends ComplexClass {

    private YearMonth yearMonth;
    private final Set<YearMonth> yearMonthSet = new HashSet<>();

    public YearMonth getYearMonth() {
        return yearMonth;
    }

    public Set<YearMonth> getYearMonthSet() {
        return yearMonthSet;
    }

    public void setYearMonth(YearMonth yearMonth) {
        this.yearMonth = yearMonth;
    }

    public void addToYearMonthSet(YearMonth yearMonth) {
        yearMonthSet.add(yearMonth);
    }

    public String yearMonthToString(YearMonth ym) {
        return ym.toString();
    }
}

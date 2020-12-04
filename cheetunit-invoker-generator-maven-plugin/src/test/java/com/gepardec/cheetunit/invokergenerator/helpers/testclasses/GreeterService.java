/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.invokergenerator.helpers.testclasses;


import com.gepardec.cheetunit.invokergenerator.helpers.testclasses.pojo.GreetingValue;
import com.gepardec.cheetunit.invokergenerator.helpers.testclasses.pojo.Salutation;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@ApplicationScoped
public class GreeterService {

    public String greet(String name) {
        return "Hello " + name + "!";
    }

    public String greet(Salutation salutation, String name) {
        return "Hello " + salutation.getValue() + " " + name + "!";
    }

    public String greet(GreetingValue greetingValue) {
        return "Hello " + greetingValue.toString() + "!";
    }

    public String greet(String... names) {
        return "Hello " + String.join(", ", names) + "!";
    }

    /**
     * @see GreeterService#greet(Salutation, String, String...)
     */
    public String greet(Salutation salutation, String name, String... otherNames) {
        return "Hello " + salutation.getValue() + " " + name + (otherNames.length == 0 ? "" : ", ") + String.join(", ", otherNames) + "!";
    }

    public String tryToGreet() throws GreetingException {
        throw new GreetingException();
    }

    /**
     * Calls a private method for greeting
     *
     * @return a silent greeting string
     */
    public String greetSilent() {
        return greetSilentInternal();
    }

    private String greetSilentInternal() {
        return "Psssscht... hey";
    }

    public LocalDate getNextDay(LocalDate aDay) {
        return aDay.plusDays(1);
    }

    public YearMonth localDateToYearMonth(LocalDate localDate) {
        return YearMonth.from(localDate);
    }

    public YearMonth localDateTimeToYearMonth(LocalDateTime localDateTime) {
        return YearMonth.from(localDateTime);
    }
}

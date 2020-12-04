/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package com.gepardec.cheetunit.examples.greeter;

import com.gepardec.cheetunit.examples.greeter.pojo.GreetingValue;
import com.gepardec.cheetunit.examples.greeter.pojo.Salutation;
import com.gepardec.cheetunit.test.CheetUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class GreeterServiceIT {

    private static GreeterInvoker service;

    @BeforeAll
    static void beforeAll() {
        service = CheetUnit.createProxy(GreeterInvoker.class);
    }

    @Test
    public void getGreeting() {
        String result = service.greet("Albus");
        Assertions.assertEquals("Hello Albus!", result);
    }

    @Test
    public void getGreetingByGreetingValue() {
        GreetingValue greetingValue = new GreetingValue(Salutation.PROFESSOR, "Dumbledore");
        String result = service.greet(greetingValue);
        Assertions.assertEquals("Hello Prof. Dumbledore!", result);
    }

    @Test
    public void getGreetingBySalutationAndName() {
        String result = service.greet(Salutation.PROFESSOR, "Dumbledore");
        Assertions.assertEquals("Hello Prof. Dumbledore!", result);
    }

    @Test
    public void getGreetingBySalutationAndNamesTwo() {
        String result = service.greet(Salutation.PROFESSOR, "Dumbledore", new String[0]);
        Assertions.assertEquals("Hello Prof. Dumbledore!", result);
    }

    @Test
    public void getNextDay() {
        LocalDate today = LocalDate.now();
        LocalDate nextDay = service.getNextDay(today);
        Assertions.assertTrue(nextDay.compareTo(today) > 0);
    }
}

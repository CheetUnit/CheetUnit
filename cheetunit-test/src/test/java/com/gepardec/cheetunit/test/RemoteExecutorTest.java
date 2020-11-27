/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.test;

import com.gepardec.cheetunit.core.CheetUnitException;
import com.gepardec.cheetunit.core.SerializedObject;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.*;

@Disabled
class RemoteExecutorTest {

    private static final String PATH = "/cheetunit-insider";
    private static final int PORT = 8089; // don't use 8080 to avoid port already in usage problems
    private static final String HOST = "localhost";

    private static WireMockServer wireMockServer;
    private static RemoteExecutor remoteExecutor;

    @BeforeAll
    static void beforeAll() {
        WireMock.configureFor(HOST, PORT);
        wireMockServer = new WireMockServer(wireMockConfig().port(PORT));
        wireMockServer.start();
        remoteExecutor = RemoteExecutor.of(
                "http://" + HOST + ":" + PORT + PATH,
                Collections.singletonList(RemoteExecutorTest.class));
    }

    @AfterAll
    static void afterAll() {
        wireMockServer.stop();
    }

    @AfterEach
    void tearDown() {
        WireMock.reset();
    }

    @Test
    void execute_verifyResponse_simpleString() {
        setupStubWithResponse("hello world");

        Object result = remoteExecutor.execute("example method", new Class[]{String.class, Long.class, Double.class}, new Object[]{"arg1", 2L, 3D});
        assertEquals("hello world", result);
    }

    @Test
    void execute_verifyResponse_simplePojo() {
        Pojo pojo = new Pojo("Patzi", 28, LocalDate.of(1992, 5, 8));
        setupStubWithResponse(pojo);

        Object result = remoteExecutor.execute("example method", new Class[]{String.class, Long.class, Double.class}, new Object[]{"arg1", 2L, 3D});
        assertEquals(pojo, result);
    }

    @Test
    void execute_verifyResponse_null() {
        setupStubWithResponse(null);

        Object result = remoteExecutor.execute("example method", new Class[]{String.class, Long.class, Double.class}, new Object[]{"arg1", 2L, 3D});
        assertNull(result);
    }

    @Test
    void execute_verifyResponse_CheetUnitExceptionOccurs_ExceptionIsRethrown() {
        String message = "some NPE occured";
        NullPointerException cause = new NullPointerException();
        CheetUnitException cheetUnitException = new CheetUnitException(message, cause);
        setupStubWithResponse(cheetUnitException);

        assertThrows(CheetUnitException.class,
                () -> remoteExecutor.execute("example method", new Class[]{String.class, Long.class, Double.class}, new Object[]{"arg1", 2L, 3D}),
                message);
    }

    @Test
    void execute_verifyResponse_UnexpectedExceptionOccurs_ExceptionIsWrapped() {
        NullPointerException exception = new NullPointerException();
        setupStubWithResponse(exception);

        assertThrows(RemoteExecutionException.class,
                () -> remoteExecutor.execute("example method", new Class[]{String.class, Long.class, Double.class}, new Object[]{"arg1", 2L, 3D}));
    }

    @Test
    void execute_verifyRequest() {
        setupStubWithResponse("hello world");

        remoteExecutor.execute("example method", new Class[]{String.class, Long.class, Double.class}, new Object[]{"arg1", 2L, 3D});

        String expectedJson =
                "{\n" +
                        "  \"primaryClassName\" : \"com.gepardec.cheetunit.test.RemoteExecutorTest\",\n" +
                        "  \"methodName\" : \"example method\",\n" +
                        "  \"args\" : \"${json-unit.ignore}\",\n" +
                        "  \"classMap\" : \"${json-unit.ignore}\"\n" +
                        "}";

        verify(exactly(1), postRequestedFor(urlEqualTo(PATH))
                .withRequestBody(equalToJson(expectedJson)));
    }

    private static class Pojo implements Serializable {
        private final String name;
        private final int age;
        private final LocalDate birthDate;

        public Pojo(String name, int age, LocalDate birthDate) {
            this.name = name;
            this.age = age;
            this.birthDate = birthDate;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pojo pojo = (Pojo) o;
            return age == pojo.age &&
                    Objects.equals(name, pojo.name) &&
                    Objects.equals(birthDate, pojo.birthDate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age, birthDate);
        }
    }

    private void setupStubWithResponse(java.io.Serializable response) {
        // as we get a serialized and base64 encoded response from the server, we need to serialize and base64 on our own for tests
        SerializedObject body = serializeAndEncodeResponse(response);
        stubFor(post(urlEqualTo(PATH))
                .withHeader("Content-Type", equalTo("application/json; charset=UTF-8"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"className\" : \"" + body.getClassName() + "\", \"serializedInstance\" : \" " + body.getSerializedInstance() + "\"}")));
    }

    private SerializedObject serializeAndEncodeResponse(java.io.Serializable response) {
        return SerializedObject.of(response, response.getClass());
    }
}

/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.cheetunit.test;

import io.github.cheetunit.core.CheetUnitServerException;
import io.github.cheetunit.core.SerializedObject;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.github.cheetunit.endpoint.CheetUnitEndpoint;
import org.junit.jupiter.api.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.*;

class RemoteExecutorTest {

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
                "http://" + HOST + ":" + PORT + CheetUnitEndpoint.CHEETUNIT_ENDPOINT,
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

        Object result = remoteExecutor.execute("example method", new Object[]{"arg1", 2L, 3D});
        assertEquals("hello world", result);
    }

    @Test
    void execute_verifyResponse_simplePojo() {
        Pojo pojo = new Pojo("Patzi", 28, LocalDate.of(1992, 5, 8));
        setupStubWithResponse(pojo);

        Object result = remoteExecutor.execute("example method", new Object[]{"arg1", 2L, 3D});
        assertEquals(pojo, result);
    }

    @Test
    void execute_verifyResponse_null() {
        setupStubWithResponse(null);

        Object result = remoteExecutor.execute("example method", new Object[]{"arg1", 2L, 3D});
        assertNull(result);
    }

    @Test
    void execute_verifyResponse_CheetUnitExceptionOccurs_ExceptionIsRethrown() {
        String message = "some NPE occured";
        NullPointerException cause = new NullPointerException();
        CheetUnitServerException cheetUnitServerException = new CheetUnitServerException(message, cause);
        setupStubWithResponse(cheetUnitServerException);

        assertThrows(CheetUnitServerException.class,
                () -> remoteExecutor.execute("example method", new Object[]{"arg1", 2L, 3D}),
                message);
    }

    @Test
    void execute_verifyResponse_UnexpectedExceptionOccurs_ExceptionIsWrapped() {
        NullPointerException exception = new NullPointerException();
        setupStubWithResponse(exception);

        assertThrows(RemoteExecutionException.class,
                () -> remoteExecutor.execute("example method", new Object[]{"arg1", 2L, 3D}));
    }

    @Test
    void execute_verifyRequest() {
        setupStubWithResponse("hello world");

        remoteExecutor.execute("example method", new Object[]{"arg1", 2L, 3D});

        String expectedJson =
                "{\n" +
                        "  \"primaryClassName\" : \"io.github.cheetunit.test.RemoteExecutorTest\",\n" +
                        "  \"methodName\" : \"example method\",\n" +
                        "  \"args\" : \"${json-unit.ignore}\",\n" +
                        "  \"classMap\" : \"${json-unit.ignore}\"\n" +
                        "}";

        verify(exactly(1), postRequestedFor(urlEqualTo(CheetUnitEndpoint.CHEETUNIT_ENDPOINT))
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

    private void setupStubWithResponse(Object response) {
        // as we get a serialized and base64 encoded response from the server, we need to serialize and base64 on our own for tests
        SerializedObject body = serializeAndEncodeResponse(response);
        stubFor(post(urlEqualTo(CheetUnitEndpoint.CHEETUNIT_ENDPOINT))
                .withHeader("Content-Type", equalTo("application/json; charset=UTF-8"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"serializedInstance\" : \"" + body.getSerializedInstance() +  "\"" + "}")));
    }

    private SerializedObject serializeAndEncodeResponse(Object response) {
        return SerializedObject.of(response);
    }
}

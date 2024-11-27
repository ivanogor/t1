package ru.t1.java.demo.service.impl;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.test.StepVerifier;
import ru.t1.java.demo.dto.ClientStatusResponseDto;
import ru.t1.java.demo.model.enums.ClientStatus;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(properties = {
        "t1.client.service.url=http://localhost:8089"
})
public class ClientStatusServiceImplIntegrationTest {

    @Autowired
    private ClientStatusServiceImpl clientStatusService;

    private WireMockServer wireMockServer;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8089);
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testGetClientStatus_Success() {
        // Arrange
        String clientId = "123";
        ClientStatusResponseDto expectedResponse = new ClientStatusResponseDto(ClientStatus.NORMAL);

        wireMockServer.stubFor(get(urlEqualTo("/client/" + clientId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"status\":\"NORMAL\"}")));

        // Act
        Mono<ClientStatusResponseDto> responseMono = clientStatusService.getClientStatus(clientId);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(response -> assertEquals(expectedResponse, response))
                .verifyComplete();
    }

    @Test
    public void testGetClientStatus_NotFound() {
        // Arrange
        String clientId = "456";

        wireMockServer.stubFor(get(urlEqualTo("/client/" + clientId))
                .willReturn(aResponse()
                        .withStatus(404)));

        // Act
        Mono<ClientStatusResponseDto> responseMono = clientStatusService.getClientStatus(clientId);

        // Assert
        StepVerifier.create(responseMono)
                .expectError(WebClientResponseException.NotFound.class)
                .verify();
    }

    @Test
    public void testGetClientStatus_InternalServerError() {
        // Arrange
        String clientId = "789";

        wireMockServer.stubFor(get(urlEqualTo("/client/" + clientId))
                .willReturn(aResponse()
                        .withStatus(500)));

        // Act
        Mono<ClientStatusResponseDto> responseMono = clientStatusService.getClientStatus(clientId);

        // Assert
        StepVerifier.create(responseMono)
                .expectError(WebClientResponseException.InternalServerError.class)
                .verify();
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public WebClient.Builder webClientBuilder() {
            return WebClient.builder();
        }
    }
}
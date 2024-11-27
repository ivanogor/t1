//package ru.t1.java.demo.service.impl;
//
//import okhttp3.mockwebserver.MockResponse;
//import okhttp3.mockwebserver.MockWebServer;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import reactor.test.StepVerifier;
//import ru.t1.java.demo.dto.ClientStatusResponseDto;
//import ru.t1.java.demo.model.enums.ClientStatus;
//import ru.t1.java.demo.service.ClientStatusService;
//
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//@ActiveProfiles("test")
//public class ClientStatusServiceImplTest {
//
//    @Autowired
//    private ClientStatusService clientStatusService;
//
//    private MockWebServer mockWebServer;
//
//    @BeforeEach
//    public void setUp() throws IOException {
//        mockWebServer = new MockWebServer();
//        mockWebServer.start();
//
//        // Переопределяем URL внешнего сервиса на URL мок-сервера
//        System.setProperty("t1.client.service.url", mockWebServer.url("/").toString());
//    }
//
//    @AfterEach
//    public void tearDown() throws IOException {
//        mockWebServer.shutdown();
//    }
//
//    @Test
//    public void testGetClientStatus() {
//        String clientId = "client123";
//        ClientStatusResponseDto responseDto = new ClientStatusResponseDto(ClientStatus.NORMAL);
//
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(200)
//                .setBody("{\"clientStatus\":\"NORMAL\"}")
//                .addHeader("Content-Type", "application/json"));
//
//        StepVerifier.create(clientStatusService.getClientStatus(clientId))
//                .assertNext(clientStatusResponseDto -> {
//                    assertEquals(ClientStatus.NORMAL, clientStatusResponseDto.getClientStatus());
//                })
//                .verifyComplete();
//    }
//}

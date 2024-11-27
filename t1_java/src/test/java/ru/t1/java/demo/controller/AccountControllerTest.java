package ru.t1.java.demo.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.model.enums.AccountStatus;
import ru.t1.java.demo.model.enums.AccountType;

import java.math.BigDecimal;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private WireMockServer wireMockServer;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(8089); // Порт для WireMock
        wireMockServer.start();
        WireMock.configureFor("localhost", 8089);
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testCreateAccount() {
        // Подготовка заглушки
        wireMockServer.stubFor(post(urlEqualTo("/accounts"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"accountType\":\"DEBIT\",\"balance\":1000.00,\"clientId\":1,\"accountStatus\":\"OPEN\",\"accountId\":\"123e4567-e89b-12d3-a456-426614174000\",\"frozenAmount\":0.00}")));

        AccountDto accountDto = AccountDto.builder()
                .accountType(AccountType.DEBIT)
                .balance(new BigDecimal("1000.00"))
                .clientId(1L)
                .accountStatus(AccountStatus.OPEN)
                .accountId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .frozenAmount(new BigDecimal("0.00"))
                .build();

        ResponseEntity<AccountDto> response = restTemplate.postForEntity("/accounts", accountDto, AccountDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(AccountType.DEBIT, response.getBody().getAccountType());
        assertEquals(new BigDecimal("1000.00"), response.getBody().getBalance());
        assertEquals(1L, response.getBody().getClientId());
        assertEquals(AccountStatus.OPEN, response.getBody().getAccountStatus());
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), response.getBody().getAccountId());
        assertEquals(new BigDecimal("0.00"), response.getBody().getFrozenAmount());
    }

    @Test
    public void testGetAccountById() {
        // Подготовка заглушки
        wireMockServer.stubFor(get(urlEqualTo("/accounts/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"accountType\":\"DEBIT\",\"balance\":500.00,\"clientId\":2,\"accountStatus\":\"OPEN\",\"accountId\":\"123e4567-e89b-12d3-a456-426614174001\",\"frozenAmount\":100.00}")));

        ResponseEntity<AccountDto> response = restTemplate.getForEntity("/accounts/1", AccountDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(AccountType.DEBIT, response.getBody().getAccountType());
        assertEquals(new BigDecimal("500.00"), response.getBody().getBalance());
        assertEquals(2L, response.getBody().getClientId());
        assertEquals(AccountStatus.OPEN, response.getBody().getAccountStatus());
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"), response.getBody().getAccountId());
        assertEquals(new BigDecimal("100.00"), response.getBody().getFrozenAmount());
    }

    @Test
    public void testGetAllAccounts() {
        // Подготовка заглушки
        wireMockServer.stubFor(get(urlEqualTo("/accounts"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\":1,\"accountType\":\"DEBIT\",\"balance\":1000.00,\"clientId\":1,\"accountStatus\":\"OPEN\",\"accountId\":\"123e4567-e89b-12d3-a456-426614174000\",\"frozenAmount\":0.00},{\"id\":2,\"accountType\":\"CREDIT\",\"balance\":500.00,\"clientId\":2,\"accountStatus\":\"ARRESTED\",\"accountId\":\"123e4567-e89b-12d3-a456-426614174001\",\"frozenAmount\":100.00}]")));

        ResponseEntity<AccountDto[]> response = restTemplate.getForEntity("/accounts", AccountDto[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().length);

        AccountDto account1 = response.getBody()[0];
        assertEquals(AccountType.DEBIT, account1.getAccountType());
        assertEquals(new BigDecimal("1000.00"), account1.getBalance());
        assertEquals(1L, account1.getClientId());
        assertEquals(AccountStatus.OPEN, account1.getAccountStatus());
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), account1.getAccountId());
        assertEquals(new BigDecimal("0.00"), account1.getFrozenAmount());

        AccountDto account2 = response.getBody()[1];
        assertEquals(AccountType.CREDIT, account2.getAccountType());
        assertEquals(new BigDecimal("500.00"), account2.getBalance());
        assertEquals(2L, account2.getClientId());
        assertEquals(AccountStatus.ARRESTED, account2.getAccountStatus());
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"), account2.getAccountId());
        assertEquals(new BigDecimal("100.00"), account2.getFrozenAmount());
    }

    @Test
    public void testUpdateAccount() {
        // Подготовка заглушки
        wireMockServer.stubFor(put(urlEqualTo("/accounts/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"accountType\":\"DEBIT\",\"balance\":1500.00,\"clientId\":1,\"accountStatus\":\"BLOCKED\",\"accountId\":\"123e4567-e89b-12d3-a456-426614174000\",\"frozenAmount\":0.00}")));

        AccountDto accountDto = AccountDto.builder()
                .accountType(AccountType.DEBIT)
                .balance(new BigDecimal("1500.00"))
                .clientId(1L)
                .accountStatus(AccountStatus.BLOCKED)
                .accountId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .frozenAmount(new BigDecimal("0.00"))
                .build();

        HttpEntity<AccountDto> requestEntity = new HttpEntity<>(accountDto);
        ResponseEntity<AccountDto> response = restTemplate.exchange("/accounts/1", HttpMethod.PUT, requestEntity, AccountDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(AccountType.DEBIT, response.getBody().getAccountType());
        assertEquals(new BigDecimal("1500.00"), response.getBody().getBalance());
        assertEquals(1L, response.getBody().getClientId());
        assertEquals(AccountStatus.CLOSED, response.getBody().getAccountStatus());
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), response.getBody().getAccountId());
        assertEquals(new BigDecimal("0.00"), response.getBody().getFrozenAmount());
    }

    @Test
    public void testDeleteAccount() {
        // Подготовка заглушки
        wireMockServer.stubFor(delete(urlEqualTo("/accounts/1"))
                .willReturn(aResponse()
                        .withStatus(200)));

        ResponseEntity<Void> response = restTemplate.exchange("/accounts/1", HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
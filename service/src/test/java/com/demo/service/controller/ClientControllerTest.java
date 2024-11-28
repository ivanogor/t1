package com.demo.service.controller;

import com.demo.service.dto.ClientStatusResponseDto;
import com.demo.service.model.enums.ClientStatus;
import com.demo.service.service.ClientService;
import com.demo.service.service.security.JwtService;
import com.demo.service.service.security.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

@WebMvcTest(ClientController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @MockBean
    private UserService userService; // Добавляем мок для UserService

    @MockBean
    private JwtService jwtService; // Добавляем мок для JwtService

    @Test
    void testGetClientStatus() throws Exception {
        // Подготовка данных
        Long clientId = 1L;
        ClientStatusResponseDto responseDto = ClientStatusResponseDto.builder()
                .clientStatus(ClientStatus.BLACKLISTED)
                .build();

        // Мокирование поведения ClientService
        when(clientService.getRandomClientStatus()).thenReturn(responseDto);

        // Вызов тестируемого метода
        mockMvc.perform(MockMvcRequestBuilders.get("/client/{id}", clientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientStatus").value(ClientStatus.BLACKLISTED.name()));
    }
}


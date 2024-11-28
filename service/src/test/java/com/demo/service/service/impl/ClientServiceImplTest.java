package com.demo.service.service.impl;

import com.demo.service.dto.ClientStatusResponseDto;
import com.demo.service.model.enums.ClientStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    void testGetRandomClientStatus() {
        ClientStatus[] statuses = ClientStatus.values();
        int randomIndex = 1;


        ClientStatusResponseDto responseDto = clientService.getRandomClientStatus();

        // Проверка результатов
        assertNotNull(responseDto);
        assertNotNull(responseDto.getClientStatus());
        assertEquals(responseDto.getClientStatus(), statuses[randomIndex]);
    }
}
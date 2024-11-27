package com.demo.service.service.impl;

import com.demo.service.dto.ClientStatusResponseDto;
import com.demo.service.model.enums.ClientStatus;
import com.demo.service.service.ClientService;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Реализация сервиса для работы с клиентами.
 * Предоставляет методы для получения случайного статуса клиента.
 *
 * @author ivanogor
 * @version 1.0
 * @since 21.11.2024
 */
@Service
public class ClientServiceImpl implements ClientService {

    private final Random random = new Random();

    /**
     * Возвращает случайный статус клиента.
     *
     * @return Объект ответа, содержащий случайный статус клиента.
     */
    @Override
    public ClientStatusResponseDto getRandomClientStatus() {
        ClientStatus[] statuses = ClientStatus.values();
        ClientStatus randomStatus = statuses[random.nextInt(statuses.length)];
        return ClientStatusResponseDto.builder()
                .clientStatus(randomStatus)
                .build();
    }
}
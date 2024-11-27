package com.demo.service.service;

import com.demo.service.dto.ClientStatusResponseDto;

/**
 * Интерфейс сервиса для работы с клиентами.
 * Предоставляет методы для получения статуса клиента.
 *
 * @author ivanogor
 * @version 1.0
 * @since 21.11.2024
 */
public interface ClientService {

    /**
     * Возвращает случайный статус клиента.
     *
     * @return Объект ответа, содержащий случайный статус клиента.
     */
    ClientStatusResponseDto getRandomClientStatus();
}
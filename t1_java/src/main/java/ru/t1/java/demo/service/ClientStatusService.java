package ru.t1.java.demo.service;

import reactor.core.publisher.Mono;
import ru.t1.java.demo.dto.ClientStatusResponseDto;

/**
 * Интерфейс сервиса для получения статуса клиента.
 * Предоставляет метод для получения статуса клиента по его идентификатору.
 *
 * @author ivanogor
 * @version 1.0
 * @since 21.11.2024
 */
public interface ClientStatusService {

    /**
     * Получает статус клиента по его идентификатору.
     *
     * @param clientId Идентификатор клиента.
     * @return Mono, содержащий объект ответа с статусом клиента.
     */
    Mono<ClientStatusResponseDto> getClientStatus(String clientId);
}
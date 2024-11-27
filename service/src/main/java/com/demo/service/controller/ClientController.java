package com.demo.service.controller;

import com.demo.service.dto.ClientStatusResponseDto;
import com.demo.service.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для работы с клиентами.
 * Предоставляет REST API для получения статуса клиента.
 *
 * @author ivanogor
 * @version 1.0
 * @since 21.11.2024
 */
@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    /**
     * Получает статус клиента по его идентификатору.
     *
     * @param id Идентификатор клиента.
     * @return Объект ответа, содержащий случайный статус клиента.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClientStatusResponseDto> getClientStatus(@PathVariable Long id) {
        ClientStatusResponseDto randomStatus = clientService.getRandomClientStatus();
        return ResponseEntity.ok(randomStatus);
    }
}
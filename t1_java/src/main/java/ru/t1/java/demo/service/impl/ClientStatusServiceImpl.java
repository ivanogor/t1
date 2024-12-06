package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.t1.java.demo.dto.ClientStatusResponseDto;
import ru.t1.java.demo.service.ClientStatusService;

/**
 * Реализация сервиса для получения статуса клиента.
 * Использует WebClient для выполнения HTTP-запросов к внешнему сервису.
 *
 * @author ivanogor
 * @version 1.0
 * @since 21.11.2024
 */
@RequiredArgsConstructor
@Service
public class ClientStatusServiceImpl implements ClientStatusService {

    private final WebClient.Builder webClientBuilder;

    @Value("${t1.client.service2.url}")
    private String clientServiceUrl;

    /**
     * Получает статус клиента по его идентификатору.
     *
     * @param clientId Идентификатор клиента.
     * @return Mono, содержащий объект ответа с статусом клиента.
     */
    @Override
    public Mono<ClientStatusResponseDto> getClientStatus(String clientId) {
        return webClientBuilder.build()
                .get()
                .uri(clientServiceUrl + "/client/" + clientId)
                .retrieve()
                .bodyToMono(ClientStatusResponseDto.class);
    }
}
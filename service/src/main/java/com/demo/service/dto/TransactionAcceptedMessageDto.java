package com.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Представляет DTO (Data Transfer Object) для сообщения, указывающего на то, что транзакция была принята.
 * Этот класс используется для передачи деталей принятия транзакции между различными слоями приложения.
 *
 * @author ivanogor
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionAcceptedMessageDto implements Serializable {
    /**
     * Уникальный идентификатор клиента.
     */
    private UUID clientId;

    /**
     * Уникальный идентификатор счета.
     */
    private UUID accountId;

    /**
     * Уникальный идентификатор транзакции.
     */
    private UUID transactionId;

    /**
     * Временная метка, указывающая на момент принятия транзакции.
     */
    private LocalDateTime timestamp;

    /**
     * Сумма транзакции.
     */
    private BigDecimal amount;

    /**
     * Баланс счета после выполнения транзакции.
     */
    private BigDecimal balance;
}
package ru.t1.java.demo.dto;

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
 * DTO (Data Transfer Object) для передачи сообщения о принятой транзакции.
 * Содержит информацию о клиенте, счете, транзакции, времени выполнения, сумме и балансе.
 *
 * @author ivanogor
 * @version 1.0
 * @since 7.11.2024
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
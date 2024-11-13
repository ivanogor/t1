package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.java.demo.model.TransactionStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) для передачи данных о транзакции.
 * Содержит информацию о сумме транзакции, связанном счете, статусе транзакции, времени создания и уникальном идентификаторе транзакции.
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
public class TransactionDto implements Serializable {

    /**
     * Идентификатор транзакции.
     */
    private Long id;

    /**
     * Сумма транзакции.
     */
    private BigDecimal amount;

    /**
     * Счет, связанный с транзакцией.
     */
    private Long accountId;

    /**
     * Статус транзакции.
     */
    private TransactionStatus transactionStatus;

    /**
     * Время создания транзакции.
     */
    private LocalDateTime createdAt;

    /**
     * Уникальный идентификатор транзакции.
     */
    private UUID transactionId;
}
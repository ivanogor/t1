package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.java.demo.model.TransactionStatus;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) для передачи сообщения о результате транзакции.
 * Содержит информацию о статусе транзакции и связанном счете.
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
public class TransactionResultMessageDto implements Serializable {

    /**
     * Уникальный идентификатор транзакции.
     */
    private UUID transactionId;

    /**
     * Статус транзакции.
     */
    private TransactionStatus status;

    /**
     * Уникальный идентификатор счета, связанного с транзакцией.
     */
    private UUID accountId;
}
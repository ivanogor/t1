package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.java.demo.model.Account;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) для передачи данных о транзакции.
 * Содержит информацию о сумме транзакции и связанном счете.
 *
 * @author ivanogor
 * @version 1.0
 * @since 30.10.2024
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
    private Account account;
}
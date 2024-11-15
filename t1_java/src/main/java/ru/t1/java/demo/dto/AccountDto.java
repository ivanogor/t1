package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.java.demo.model.enums.AccountStatus;
import ru.t1.java.demo.model.enums.AccountType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) для передачи данных о банковском счете.
 * Содержит информацию о типе счета, его балансе, статусе, идентификаторе клиента и других атрибутах.
 *
 * @author ivanogor
 * @version 2.0
 * @since 7.11.2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDto implements Serializable {

    /**
     * Идентификатор счета.
     */
    private Long id;

    /**
     * Тип банковского счета.
     */
    private AccountType accountType;

    /**
     * Баланс счета.
     */
    private BigDecimal balance;

    /**
     * Идентификатор клиента, которому принадлежит счет.
     */
    private Long clientId;

    /**
     * Статус счета.
     */
    private AccountStatus accountStatus;

    /**
     * Уникальный идентификатор счета.
     */
    private UUID accountId;

    /**
     * Замороженная сумма на счете.
     */
    private BigDecimal frozenAmount;
}
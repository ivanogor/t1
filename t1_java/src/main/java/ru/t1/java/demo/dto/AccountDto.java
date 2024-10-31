package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.java.demo.model.AccountType;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) для передачи данных о банковском счете.
 * Содержит информацию о типе счета и его балансе.
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
public class AccountDto {

    /**
     * Тип банковского счета.
     */
    private AccountType accountType;

    /**
     * Баланс счета.
     */
    private BigDecimal balance;
}
package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Представляет сущность банковского счета в системе.
 * Этот класс отображается на таблицу "accounts" в базе данных.
 *
 * @author ivanogor
 * @version 1.0
 * @since 30.10.2024
 */
@Entity
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Table(name = "accounts")
public class Account extends AbstractPersistable<Long> {

    /**
     * Тип счета.
     * Это поле отображается на столбец "account_type" в базе данных.
     */
    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    /**
     * Баланс счета.
     * Это поле отображается на столбец "balance" в базе данных с точностью 19 и масштабом 2.
     */
    @Column(name = "balance", precision = 19, scale = 2)
    private BigDecimal balance;

    /**
     * Список транзакций, связанных с этим счетом.
     * Это поле отображается на отношение "один ко многим" с сущностью Transaction.
     * Все изменения в счете будут каскадно отражаться на связанных транзакциях,
     * и они будут удаляться, если счет будет удален.
     */
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
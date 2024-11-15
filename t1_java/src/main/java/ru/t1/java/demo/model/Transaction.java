package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.AbstractPersistable;
import ru.t1.java.demo.model.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Представляет сущность транзакции в системе.
 * Этот класс отображается на таблицу "transactions" в базе данных.
 *
 * @author ivanogor
 * @version 2.0
 * @since 7.11.2024
 */
@Entity
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction extends AbstractPersistable<Long> {

    /**
     * Уникальный идентификатор транзакции.
     * Это поле отображается на столбец "transaction_id" в базе данных.
     * По умолчанию генерируется новый UUID при создании транзакции.
     */
    @Builder.Default
    @Column(name = "transaction_id", nullable = false, unique = true)
    private UUID transactionId = UUID.randomUUID();

    /**
     * Сумма транзакции.
     * Это поле отображается на столбец "amount" в базе данных с точностью 19 и масштабом 2.
     */
    @Column(name = "amount", precision = 19, scale = 2)
    private BigDecimal amount;

    /**
     * Дата и время создания транзакции.
     * Это поле автоматически заполняется при создании записи.
     * Отображается на столбец "created_at" в базе данных.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Связь с банковским счетом, к которому относится транзакция.
     * Это поле отображается на отношение "многие к одному" с сущностью Account.
     * Связь осуществляется через столбец "account_id" в базе данных.
     */
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    /**
     * Статус транзакции.
     * Это поле отображается на столбец "transaction_status" в базе данных.
     */
    @Column(name = "transaction_status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;
}
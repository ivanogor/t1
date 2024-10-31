package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Представляет сущность транзакции в системе.
 * Этот класс отображается на таблицу "transactions" в базе данных.
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
@Table(name = "transactions")
public class Transaction extends AbstractPersistable<Long> {

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
     * Дата и время последнего обновления транзакции.
     * Это поле автоматически обновляется при изменении записи.
     * Отображается на столбец "updated_at" в базе данных.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Связь с банковским счетом, к которому относится транзакция.
     * Это поле отображается на отношение "многие к одному" с сущностью Account.
     * Связь осуществляется через столбец "account_id" в базе данных.
     */
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
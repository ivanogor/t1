package ru.t1.java.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Представляет сущность журнала ошибок источника данных в системе.
 * Этот класс отображается на таблицу "data_source_error_logs" в базе данных.
 *
 * @author ivanogor
 * @version 1.0
 * @since 30.10.24
 */
@Entity
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Table(name = "data_source_error_logs")
public class DataSourceErrorLog extends AbstractPersistable<Long> {

    /**
     * Стек вызовов, связанный с ошибкой.
     * Это поле отображается на столбец "stack_trace" в базе данных с типом данных TEXT.
     */
    @Column(name = "stack_trace", columnDefinition = "TEXT")
    private String stackTrace;

    /**
     * Сообщение об ошибке.
     * Это поле отображается на столбец "message" в базе данных с типом данных TEXT.
     */
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    /**
     * Сигнатура метода, в котором произошла ошибка.
     * Это поле отображается на столбец "method_signature" в базе данных с типом данных TEXT.
     */
    @Column(name = "method_signature", columnDefinition = "TEXT")
    private String methodSignature;
}
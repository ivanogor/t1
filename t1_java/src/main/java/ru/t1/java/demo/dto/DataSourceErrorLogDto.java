package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для передачи данных о логе ошибок источника данных.
 * Содержит информацию об идентификаторе, стеке вызовов, сообщении об ошибке и сигнатуре метода.
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
public class DataSourceErrorLogDto {
    /**
     * Стек вызовов, связанный с ошибкой.
     */
    private String stackTrace;

    /**
     * Сообщение об ошибке.
     */
    private String message;

    /**
     * Сигнатура метода, в котором произошла ошибка.
     */
    private String methodSignature;
}
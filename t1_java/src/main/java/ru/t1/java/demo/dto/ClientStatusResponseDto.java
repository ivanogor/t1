package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.java.demo.model.enums.ClientStatus;

/**
 * DTO (Data Transfer Object) для передачи статуса клиента.
 * Содержит информацию о статусе клиента.
 *
 * @author ivanogor
 * @version 1.0
 * @since 21.11.2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientStatusResponseDto {

    /**
     * Статус клиента.
     */
    private ClientStatus clientStatus;
}
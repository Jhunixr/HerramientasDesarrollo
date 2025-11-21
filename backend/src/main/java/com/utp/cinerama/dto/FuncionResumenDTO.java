package com.utp.cinerama.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para resumen de función en confirmación
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuncionResumenDTO {
    private Long id;
    private LocalDateTime fechaHora;
    private String salaNombre;
}



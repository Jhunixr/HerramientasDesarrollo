package com.utp.cinerama.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para resumen de película en confirmación
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeliculaResumenDTO {
    private Long id;
    private String titulo;
    private String posterUrl;
}



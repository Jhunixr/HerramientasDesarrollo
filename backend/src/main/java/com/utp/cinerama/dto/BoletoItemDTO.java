package com.utp.cinerama.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para un boleto en el cálculo de total
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoletoItemDTO {
    private Long asientoId;
    private String codigoAsiento; // Ej: "A1", "B5"
    private Double precio;
}



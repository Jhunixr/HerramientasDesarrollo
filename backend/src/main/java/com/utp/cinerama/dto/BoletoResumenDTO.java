package com.utp.cinerama.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para resumen de boleto en confirmación
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoletoResumenDTO {
    private Long boletoId;
    private String codigoAsiento;
    private Double precio;
}



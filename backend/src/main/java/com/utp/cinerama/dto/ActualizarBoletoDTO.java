package com.utp.cinerama.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

/**
 * DTO para actualizar un boleto existente
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarBoletoDTO {

    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    @DecimalMax(value = "1000.0", message = "El precio no puede exceder 1000")
    private Double precio;

    private String estado; // RESERVADO, PAGADO, CANCELADO, USADO
}


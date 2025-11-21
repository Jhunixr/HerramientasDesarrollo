package com.utp.cinerama.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO para crear un nuevo boleto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearBoletoDTO {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;

    @NotNull(message = "El ID de la función es obligatorio")
    private Long funcionId;

    @NotNull(message = "El ID del asiento es obligatorio")
    private Long asientoId;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    @DecimalMax(value = "1000.0", message = "El precio no puede exceder 1000")
    private Double precio;
}


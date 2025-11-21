package com.utp.cinerama.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO para actualizar una función existente
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarFuncionDTO {

    @Future(message = "La función debe ser en el futuro")
    private LocalDateTime fechaHora;

    @Min(value = 0, message = "Los asientos disponibles no pueden ser negativos")
    private Integer asientosDisponibles;

    @Min(value = 1, message = "Debe haber al menos 1 asiento")
    @Max(value = 500, message = "No puede haber más de 500 asientos")
    private Integer asientosTotales;
}


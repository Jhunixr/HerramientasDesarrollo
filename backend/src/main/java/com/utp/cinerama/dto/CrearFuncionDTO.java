package com.utp.cinerama.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO para crear una nueva función
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearFuncionDTO {

    @NotNull(message = "El ID de la sala es obligatorio")
    private Long salaId;

    @NotNull(message = "El ID de la película es obligatorio")
    private Long peliculaId;

    @NotNull(message = "La fecha y hora son obligatorias")
    @Future(message = "La función debe ser en el futuro")
    private LocalDateTime fechaHora;

    @NotNull(message = "Los asientos totales son obligatorios")
    @Min(value = 1, message = "Debe haber al menos 1 asiento")
    @Max(value = 500, message = "No puede haber más de 500 asientos")
    private Integer asientosTotales;
}


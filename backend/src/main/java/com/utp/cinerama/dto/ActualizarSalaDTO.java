package com.utp.cinerama.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO para actualizar una sala existente
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarSalaDTO {

    @Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    @Max(value = 500, message = "La capacidad no puede exceder 500")
    private Integer capacidad;

    private String tipo; // NORMAL, CINE_2D

    private Boolean activa;
}


package com.utp.cinerama.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO para crear una nueva sala
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearSalaDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    @Max(value = 500, message = "La capacidad no puede exceder 500")
    private Integer capacidad;

    @NotBlank(message = "El tipo de sala es obligatorio")
    private String tipo; // NORMAL, CINE_2D

    @Builder.Default
    private Boolean activa = true;
}


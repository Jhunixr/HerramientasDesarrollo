package com.utp.cinerama.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO para actualizar un producto existente
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarProductoDTO {

    @Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @Size(max = 50, message = "La categoría no puede exceder 50 caracteres")
    private String categoria;

    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    @DecimalMax(value = "1000.0", message = "El precio no puede exceder 1000")
    private Double precio;

    @Min(value = 0, message = "El stock no puede ser negativo")
    @Max(value = 10000, message = "El stock no puede exceder 10000")
    private Integer stock;

    private Boolean activo;
}


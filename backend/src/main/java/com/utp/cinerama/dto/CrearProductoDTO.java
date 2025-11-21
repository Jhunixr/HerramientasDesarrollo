package com.utp.cinerama.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO para crear un nuevo producto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearProductoDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @NotBlank(message = "La categoría es obligatoria")
    @Size(max = 50, message = "La categoría no puede exceder 50 caracteres")
    private String categoria;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    @DecimalMax(value = "1000.0", message = "El precio no puede exceder 1000")
    private Double precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Max(value = 10000, message = "El stock no puede exceder 10000")
    private Integer stock;

    @Builder.Default
    private Boolean activo = true;
}


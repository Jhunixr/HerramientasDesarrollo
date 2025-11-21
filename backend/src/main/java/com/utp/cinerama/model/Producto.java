package com.utp.cinerama.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    @Column(nullable = false)
    private String descripcion;

    @NotBlank(message = "La categoría es obligatoria")
    @Size(max = 50, message = "La categoría no puede exceder 50 caracteres")
    @Column(nullable = false)
    private String categoria;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    @DecimalMax(value = "1000.0", message = "El precio no puede exceder 1000")
    @Column(nullable = false)
    private Double precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Max(value = 10000, message = "El stock no puede exceder 10000")
    @Column(nullable = false)
    private Integer stock;

    @NotNull(message = "El estado activo es obligatorio")
    @Column(nullable = false)
    private Boolean activo;
}

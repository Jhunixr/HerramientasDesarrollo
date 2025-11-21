package com.utp.cinerama.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "salas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Sala {

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

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    @Max(value = 500, message = "La capacidad no puede exceder 500")
    @Column(nullable = false)
    private Integer capacidad;

    @NotNull(message = "El tipo de sala es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoSala tipo;

    @NotNull(message = "El estado activo es obligatorio")
    @Column(nullable = false)
    private Boolean activa;

    public enum TipoSala {
        NORMAL,
        CINE_2D
    }
}

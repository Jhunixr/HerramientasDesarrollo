package com.utp.cinerama.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "funciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Funcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La sala es obligatoria")
    @ManyToOne
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;

    @NotNull(message = "La película es obligatoria")
    @ManyToOne
    @JoinColumn(name = "pelicula_id", nullable = false)
    private Pelicula pelicula;

    @NotNull(message = "La fecha y hora son obligatorias")
    @Future(message = "La función debe ser en el futuro")
    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @NotNull(message = "Los asientos disponibles son obligatorios")
    @Min(value = 0, message = "Los asientos disponibles no pueden ser negativos")
    @Column(nullable = false)
    private Integer asientosDisponibles;

    @NotNull(message = "Los asientos totales son obligatorios")
    @Min(value = 1, message = "Debe haber al menos 1 asiento")
    @Max(value = 500, message = "No puede haber más de 500 asientos")
    @Column(nullable = false)
    private Integer asientosTotales;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    @Column(nullable = false)
    private Double precio;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activa = true;
}

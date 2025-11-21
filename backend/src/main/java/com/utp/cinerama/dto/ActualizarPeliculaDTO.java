package com.utp.cinerama.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO para actualizar una película existente
 * Todos los campos son opcionales para permitir actualizaciones parciales
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarPeliculaDTO {

    @Size(min = 1, max = 255, message = "El título debe tener entre 1 y 255 caracteres")
    private String titulo;

    @Size(max = 255, message = "El título original no puede exceder 255 caracteres")
    private String tituloOriginal;

    @Size(max = 10, message = "El idioma original no puede exceder 10 caracteres")
    private String idiomaOriginal;

    private String genero;

    @Min(value = 1, message = "La duración debe ser al menos 1 minuto")
    @Max(value = 600, message = "La duración no puede exceder 600 minutos")
    private Integer duracion;

    private String clasificacion;

    private String sinopsis;

    private String resumen;

    @DecimalMin(value = "0.0", message = "La popularidad no puede ser negativa")
    private Double popularidad;

    private String posterUrl;

    private String backdropUrl;

    @PastOrPresent(message = "La fecha de estreno no puede ser futura")
    private LocalDate fechaEstreno;

    @DecimalMin(value = "0.0", message = "El voto promedio no puede ser negativo")
    @DecimalMax(value = "10.0", message = "El voto promedio no puede exceder 10.0")
    private Double votoPromedio;

    @Min(value = 0, message = "El total de votos no puede ser negativo")
    private Integer totalVotos;

    private Boolean adult;

    private Boolean activa;
}


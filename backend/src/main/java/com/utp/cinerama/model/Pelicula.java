package com.utp.cinerama.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "peliculas", indexes = {
    @Index(name = "idx_tmdb_id", columnList = "tmdb_id", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID original de TMDb (único)
    @Column(name = "tmdb_id", unique = true)
    private Long tmdbId;

    // Campos originales (ahora opcionales para flexibilidad)
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 1, max = 255, message = "El título debe tener entre 1 y 255 caracteres")
    @Column(nullable = false)
    private String titulo;

    @Size(max = 255, message = "El título original no puede exceder 255 caracteres")
    @Column(name = "titulo_original")
    private String tituloOriginal;

    @Size(max = 10, message = "El idioma original no puede exceder 10 caracteres")
    @Column(name = "idioma_original", length = 10)
    private String idiomaOriginal;

    @Column(columnDefinition = "TEXT")
    private String genero; // Guarda géneros concatenados: "Acción, Comedia"

    @Min(value = 1, message = "La duración debe ser al menos 1 minuto")
    @Max(value = 600, message = "La duración no puede exceder 600 minutos")
    @Column
    private Integer duracion; // Duración en minutos (puede venir vacío de TMDb)

    @Column
    private String clasificacion; // Ej: PG-13, R, etc.

    @Column(columnDefinition = "TEXT")
    private String sinopsis; // Sinopsis local/editada

    @Column(columnDefinition = "TEXT")
    private String resumen; // Campo "overview" directo de TMDb

    // Nuevos campos de TMDb
    @DecimalMin(value = "0.0", message = "La popularidad no puede ser negativa")
    @Column
    private Double popularidad;

    @Column(name = "poster_url")
    private String posterUrl; // URL del poster

    @Column(name = "backdrop_url")
    private String backdropUrl; // URL del backdrop

    @PastOrPresent(message = "La fecha de estreno no puede ser futura")
    @Column(name = "fecha_estreno")
    private LocalDate fechaEstreno;

    @DecimalMin(value = "0.0", message = "El voto promedio no puede ser negativo")
    @DecimalMax(value = "10.0", message = "El voto promedio no puede exceder 10.0")
    @Column(name = "voto_promedio")
    private Double votoPromedio;

    @Min(value = 0, message = "El total de votos no puede ser negativo")
    @Column(name = "total_votos")
    private Integer totalVotos;

    @Column(name = "adult")
    private Boolean adult; // Contenido para adultos

    @Builder.Default
    @Column(name = "activa")
    private Boolean activa = true; // Para control interno del cine

    // equals y hashCode basados en tmdbId
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pelicula pelicula = (Pelicula) o;
        return Objects.equals(tmdbId, pelicula.tmdbId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tmdbId);
    }
}

package com.utp.cinerama.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO para mapear la respuesta de películas individuales desde TMDb API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TMDbMovieDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("original_title")
    private String originalTitle;

    @JsonProperty("original_language")
    private String originalLanguage;

    @JsonProperty("overview")
    private String overview;

    @JsonProperty("genre_ids")
    private List<Integer> genreIds;

    @JsonProperty("popularity")
    private Double popularity;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("backdrop_path")
    private String backdropPath;

    @JsonProperty("release_date")
    private String releaseDate; // TMDb envía como String "yyyy-MM-dd"

    @JsonProperty("vote_average")
    private Double voteAverage;

    @JsonProperty("vote_count")
    private Integer voteCount;

    @JsonProperty("adult")
    private Boolean adult;

    @JsonProperty("video")
    private Boolean video;

    // Método auxiliar para convertir releaseDate String a LocalDate
    public LocalDate getReleaseDateAsLocalDate() {
        try {
            return releaseDate != null && !releaseDate.isEmpty() 
                ? LocalDate.parse(releaseDate) 
                : null;
        } catch (Exception e) {
            return null;
        }
    }

    // Método para obtener URL completa del poster
    public String getFullPosterPath() {
        return posterPath != null 
            ? "https://image.tmdb.org/t/p/w500" + posterPath 
            : null;
    }

    // Método para obtener URL completa del backdrop
    public String getFullBackdropPath() {
        return backdropPath != null 
            ? "https://image.tmdb.org/t/p/original" + backdropPath 
            : null;
    }
}


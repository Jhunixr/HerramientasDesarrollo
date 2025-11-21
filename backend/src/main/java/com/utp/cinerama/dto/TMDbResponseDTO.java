package com.utp.cinerama.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para mapear la respuesta completa de TMDb API (Now Playing, Popular, etc.)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TMDbResponseDTO {

    @JsonProperty("page")
    private Integer page;

    @JsonProperty("results")
    private List<TMDbMovieDTO> results;

    @JsonProperty("total_pages")
    private Integer totalPages;

    @JsonProperty("total_results")
    private Integer totalResults;

    @JsonProperty("dates")
    private TMDbDatesDTO dates;

    /**
     * DTO interno para las fechas de la respuesta (now_playing)
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TMDbDatesDTO {
        
        @JsonProperty("maximum")
        private String maximum;

        @JsonProperty("minimum")
        private String minimum;
    }
}


package com.utp.cinerama.controller;

import com.utp.cinerama.dto.ApiResponse;
import com.utp.cinerama.dto.TMDbMovieDTO;
import com.utp.cinerama.service.TMDbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para explorar películas desde TMDb sin guardarlas
 * Solo permite explorar, no modifica la base de datos
 */
@RestController
@RequestMapping("/api/tmdb")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class TMDbController {

    private final TMDbService tmdbService;

    /**
     * Obtiene películas en cartelera desde TMDb (sin guardar)
     * GET /api/tmdb/en-cartelera?page=1
     */
    @GetMapping("/en-cartelera")
    public ResponseEntity<ApiResponse<List<TMDbMovieDTO>>> obtenerEnCartelera(
            @RequestParam(defaultValue = "1") Integer page) {
        try {
            log.info("Explorando películas en cartelera desde TMDb (página {})", page);
            List<TMDbMovieDTO> peliculas = tmdbService.getNowPlayingMovies(page);
            return ResponseEntity.ok(
                ApiResponse.success("Películas en cartelera obtenidas exitosamente", peliculas)
            );
        } catch (Exception e) {
            log.error("Error al obtener películas en cartelera: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(
                ApiResponse.<List<TMDbMovieDTO>>error("Error al obtener películas: " + e.getMessage())
            );
        }
    }

    /**
     * Obtiene películas próximamente desde TMDb (sin guardar)
     * GET /api/tmdb/proximamente?page=1
     */
    @GetMapping("/proximamente")
    public ResponseEntity<ApiResponse<List<TMDbMovieDTO>>> obtenerProximamente(
            @RequestParam(defaultValue = "1") Integer page) {
        try {
            log.info("Explorando películas próximamente desde TMDb (página {})", page);
            List<TMDbMovieDTO> peliculas = tmdbService.getUpcomingMovies(page);
            return ResponseEntity.ok(
                ApiResponse.success("Películas próximamente obtenidas exitosamente", peliculas)
            );
        } catch (Exception e) {
            log.error("Error al obtener películas próximamente: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(
                ApiResponse.<List<TMDbMovieDTO>>error("Error al obtener películas: " + e.getMessage())
            );
        }
    }

    /**
     * Obtiene películas populares desde TMDb (sin guardar)
     * GET /api/tmdb/populares?page=1
     */
    @GetMapping("/populares")
    public ResponseEntity<ApiResponse<List<TMDbMovieDTO>>> obtenerPopulares(
            @RequestParam(defaultValue = "1") Integer page) {
        try {
            log.info("Explorando películas populares desde TMDb (página {})", page);
            List<TMDbMovieDTO> peliculas = tmdbService.getPopularMovies(page);
            return ResponseEntity.ok(
                ApiResponse.success("Películas populares obtenidas exitosamente", peliculas)
            );
        } catch (Exception e) {
            log.error("Error al obtener películas populares: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(
                ApiResponse.<List<TMDbMovieDTO>>error("Error al obtener películas: " + e.getMessage())
            );
        }
    }
}



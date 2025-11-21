package com.utp.cinerama.controller;

import com.utp.cinerama.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controlador para la ruta raíz
 * Evita errores cuando se accede a "/"
 */
@RestController
@Slf4j
public class RootController {

    @GetMapping("/")
    public ResponseEntity<ApiResponse<Map<String, String>>> root() {
        log.info("Acceso a la ruta raíz");
        Map<String, String> info = Map.of(
            "message", "API de Cinerama",
            "version", "1.0.0",
            "endpoints", "/api/peliculas, /api/funciones, /api/auth, etc."
        );
        return ResponseEntity.ok(ApiResponse.success("API de Cinerama funcionando correctamente", info));
    }
}








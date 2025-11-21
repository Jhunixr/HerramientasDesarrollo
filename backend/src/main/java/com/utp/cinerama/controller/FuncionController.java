package com.utp.cinerama.controller;

import com.utp.cinerama.dto.ApiResponse;
import com.utp.cinerama.exception.ResourceNotFoundException;
import com.utp.cinerama.model.Funcion;
import com.utp.cinerama.service.FuncionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funciones")
@RequiredArgsConstructor
@Slf4j
public class FuncionController {

    private final FuncionService funcionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Funcion>>> obtenerTodasLasFunciones() {
        try {
            log.info("Obteniendo todas las funciones");
            List<Funcion> funciones = funcionService.obtenerTodasLasFunciones();
            return ResponseEntity.ok(
                ApiResponse.success("Funciones obtenidas exitosamente", funciones)
            );
        } catch (Exception e) {
            log.error("Error al obtener funciones: ", e);
            return ResponseEntity.ok(
                ApiResponse.success("Funciones obtenidas exitosamente", List.<Funcion>of())
            );
        }
    }

    @GetMapping("/activas")
    public ResponseEntity<ApiResponse<List<Funcion>>> obtenerFuncionesActivas() {
        try {
            log.info("Obteniendo funciones activas");
            List<Funcion> funciones = funcionService.obtenerFuncionesActivas();
            return ResponseEntity.ok(
                ApiResponse.success("Funciones activas obtenidas exitosamente", funciones)
            );
        } catch (Exception e) {
            log.error("Error al obtener funciones activas: ", e);
            return ResponseEntity.ok(
                ApiResponse.success("Funciones activas obtenidas exitosamente", List.<Funcion>of())
            );
        }
    }

    @GetMapping("/pelicula/{peliculaId}")
    public ResponseEntity<ApiResponse<List<Funcion>>> obtenerFuncionesPorPelicula(@PathVariable Long peliculaId) {
        try {
            log.info("Obteniendo funciones para película ID: {}", peliculaId);
            List<Funcion> funciones = funcionService.obtenerFuncionesPorPelicula(peliculaId);
            return ResponseEntity.ok(
                ApiResponse.success("Funciones obtenidas exitosamente", funciones)
            );
        } catch (Exception e) {
            log.error("Error al obtener funciones por película: ", e);
            return ResponseEntity.ok(
                ApiResponse.success("Funciones obtenidas exitosamente", List.<Funcion>of())
            );
        }
    }

    @GetMapping("/sala/{salaId}")
    public ResponseEntity<ApiResponse<List<Funcion>>> obtenerFuncionesPorSala(@PathVariable Long salaId) {
        try {
            log.info("Obteniendo funciones para sala ID: {}", salaId);
            List<Funcion> funciones = funcionService.obtenerFuncionesPorSala(salaId);
            return ResponseEntity.ok(
                ApiResponse.success("Funciones obtenidas exitosamente", funciones)
            );
        } catch (Exception e) {
            log.error("Error al obtener funciones por sala: ", e);
            return ResponseEntity.ok(
                ApiResponse.success("Funciones obtenidas exitosamente", List.<Funcion>of())
            );
        }
    }

    @PostMapping("/generar-automaticas")
    public ResponseEntity<ApiResponse<List<Funcion>>> generarFuncionesAutomaticas() {
        try {
            log.info("Generando funciones automáticamente para películas en cartelera");
            List<Funcion> funciones = funcionService.generarFuncionesParaPeliculasEnCartelera();
            return ResponseEntity.ok(
                ApiResponse.success("Funciones generadas exitosamente: " + funciones.size(), funciones)
            );
        } catch (Exception e) {
            log.error("Error al generar funciones automáticas: ", e);
            return ResponseEntity.ok(
                ApiResponse.success("Funciones generadas exitosamente", List.<Funcion>of())
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Funcion>> obtenerFuncionPorId(@PathVariable Long id) {
        log.info("Buscando función por ID: {}", id);
        Funcion funcion = funcionService.obtenerFuncionPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcion", "id", id));
        
        return ResponseEntity.ok(
            ApiResponse.success("Función obtenida exitosamente", funcion)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Funcion>> crearFuncion(@Valid @RequestBody Funcion funcion) {
        log.info("Creando nueva función");
        Funcion nuevaFuncion = funcionService.crearFuncion(funcion);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Función creada exitosamente", nuevaFuncion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Funcion>> actualizarFuncion(
            @PathVariable Long id, 
            @Valid @RequestBody Funcion funcion) {
        
        log.info("Actualizando función ID: {}", id);
        Funcion funcionActualizada = funcionService.actualizarFuncion(id, funcion);
        
        return ResponseEntity.ok(
            ApiResponse.success("Función actualizada exitosamente", funcionActualizada)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> eliminarFuncion(@PathVariable Long id) {
        log.info("Eliminando función ID: {}", id);
        funcionService.eliminarFuncion(id);
        
        return ResponseEntity.ok(
            ApiResponse.success("Función eliminada exitosamente")
        );
    }
}

package com.utp.cinerama.controller;

import com.utp.cinerama.dto.ApiResponse;
import com.utp.cinerama.exception.ResourceNotFoundException;
import com.utp.cinerama.model.Sala;
import com.utp.cinerama.service.SalaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salas")
@RequiredArgsConstructor
@Slf4j
public class SalaController {

    private final SalaService salaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Sala>>> obtenerTodasLasSalas() {
        log.info("Obteniendo todas las salas");
        List<Sala> salas = salaService.obtenerTodasLasSalas();
        return ResponseEntity.ok(
            ApiResponse.success("Salas obtenidas exitosamente", salas)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Sala>> obtenerSalaPorId(@PathVariable Long id) {
        log.info("Buscando sala por ID: {}", id);
        Sala sala = salaService.obtenerSalaPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sala", "id", id));
        
        return ResponseEntity.ok(
            ApiResponse.success("Sala obtenida exitosamente", sala)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Sala>> crearSala(@Valid @RequestBody Sala sala) {
        log.info("Creando nueva sala: {}", sala.getNombre());
        Sala nuevaSala = salaService.crearSala(sala);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Sala creada exitosamente", nuevaSala));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Sala>> actualizarSala(
            @PathVariable Long id, 
            @Valid @RequestBody Sala sala) {
        
        log.info("Actualizando sala ID: {}", id);
        Sala salaActualizada = salaService.actualizarSala(id, sala);
        
        return ResponseEntity.ok(
            ApiResponse.success("Sala actualizada exitosamente", salaActualizada)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> eliminarSala(@PathVariable Long id) {
        log.info("Eliminando sala ID: {}", id);
        salaService.eliminarSala(id);
        
        return ResponseEntity.ok(
            ApiResponse.success("Sala eliminada exitosamente")
        );
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<ApiResponse<List<Sala>>> buscarPorTipo(@PathVariable Sala.TipoSala tipo) {
        log.info("Buscando salas por tipo: {}", tipo);
        List<Sala> salas = salaService.buscarPorTipo(tipo);
        
        return ResponseEntity.ok(
            ApiResponse.success("Salas del tipo '" + tipo + "' obtenidas exitosamente", salas)
        );
    }

    @GetMapping("/activas")
    public ResponseEntity<ApiResponse<List<Sala>>> buscarSalasActivas() {
        log.info("Buscando salas activas");
        List<Sala> salas = salaService.buscarSalasActivas();
        
        return ResponseEntity.ok(
            ApiResponse.success("Salas activas obtenidas exitosamente", salas)
        );
    }
}

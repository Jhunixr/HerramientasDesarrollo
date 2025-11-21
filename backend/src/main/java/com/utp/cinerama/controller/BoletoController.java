package com.utp.cinerama.controller;

import com.utp.cinerama.dto.ApiResponse;
import com.utp.cinerama.exception.ResourceNotFoundException;
import com.utp.cinerama.model.Boleto;
import com.utp.cinerama.service.BoletoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boletos")
@RequiredArgsConstructor
@Slf4j
public class BoletoController {

    private final BoletoService boletoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Boleto>>> obtenerTodosLosBoletos() {
        log.info("Obteniendo todos los boletos");
        List<Boleto> boletos = boletoService.obtenerTodosLosBoletos();
        return ResponseEntity.ok(
            ApiResponse.success("Boletos obtenidos exitosamente", boletos)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Boleto>> obtenerBoletoPorId(@PathVariable Long id) {
        log.info("Buscando boleto por ID: {}", id);
        Boleto boleto = boletoService.obtenerBoletoPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Boleto", "id", id));
        
        return ResponseEntity.ok(
            ApiResponse.success("Boleto obtenido exitosamente", boleto)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Boleto>> crearBoleto(@Valid @RequestBody Boleto boleto) {
        log.info("Creando nuevo boleto");
        Boleto nuevoBoleto = boletoService.crearBoleto(boleto);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Boleto creado exitosamente", nuevoBoleto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Boleto>> actualizarBoleto(
            @PathVariable Long id, 
            @Valid @RequestBody Boleto boleto) {
        
        log.info("Actualizando boleto ID: {}", id);
        Boleto boletoActualizado = boletoService.actualizarBoleto(id, boleto);
        
        return ResponseEntity.ok(
            ApiResponse.success("Boleto actualizado exitosamente", boletoActualizado)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> eliminarBoleto(@PathVariable Long id) {
        log.info("Eliminando boleto ID: {}", id);
        boletoService.eliminarBoleto(id);
        
        return ResponseEntity.ok(
            ApiResponse.success("Boleto eliminado exitosamente")
        );
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<ApiResponse<List<Boleto>>> buscarPorCliente(@PathVariable Long clienteId) {
        log.info("Buscando boletos del cliente ID: {}", clienteId);
        List<Boleto> boletos = boletoService.buscarPorCliente(clienteId);
        
        return ResponseEntity.ok(
            ApiResponse.success("Boletos del cliente obtenidos exitosamente", boletos)
        );
    }

    @GetMapping("/funcion/{funcionId}")
    public ResponseEntity<ApiResponse<List<Boleto>>> buscarPorFuncion(@PathVariable Long funcionId) {
        log.info("Buscando boletos de la función ID: {}", funcionId);
        List<Boleto> boletos = boletoService.buscarPorFuncion(funcionId);
        
        return ResponseEntity.ok(
            ApiResponse.success("Boletos de la función obtenidos exitosamente", boletos)
        );
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponse<List<Boleto>>> buscarPorEstado(@PathVariable Boleto.EstadoBoleto estado) {
        log.info("Buscando boletos con estado: {}", estado);
        List<Boleto> boletos = boletoService.buscarPorEstado(estado);
        
        return ResponseEntity.ok(
            ApiResponse.success("Boletos con estado '" + estado + "' obtenidos exitosamente", boletos)
        );
    }
}

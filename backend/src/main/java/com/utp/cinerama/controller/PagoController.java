package com.utp.cinerama.controller;

import com.utp.cinerama.dto.ApiResponse;
import com.utp.cinerama.exception.ResourceNotFoundException;
import com.utp.cinerama.model.Pago;
import com.utp.cinerama.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Slf4j
public class PagoController {

    private final PagoService pagoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Pago>>> obtenerTodosLosPagos() {
        log.info("Obteniendo todos los pagos");
        List<Pago> pagos = pagoService.obtenerTodosLosPagos();
        return ResponseEntity.ok(ApiResponse.success("Pagos obtenidos exitosamente", pagos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Pago>> obtenerPagoPorId(@PathVariable Long id) {
        log.info("Buscando pago con ID: {}", id);
        Pago pago = pagoService.obtenerPagoPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con ID: " + id));
        return ResponseEntity.ok(ApiResponse.success("Pago obtenido exitosamente", pago));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Pago>> crearPago(@Valid @RequestBody Pago pago) {
        log.info("Creando nuevo pago");
        Pago nuevoPago = pagoService.crearPago(pago);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Pago creado exitosamente", nuevoPago));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarPago(@PathVariable Long id) {
        log.info("Eliminando pago con ID: {}", id);
        pagoService.eliminarPago(id);
        return ResponseEntity.ok(ApiResponse.success("Pago eliminado exitosamente", null));
    }
}

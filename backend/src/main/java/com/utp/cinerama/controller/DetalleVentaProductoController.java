package com.utp.cinerama.controller;

import com.utp.cinerama.dto.ApiResponse;
import com.utp.cinerama.exception.ResourceNotFoundException;
import com.utp.cinerama.model.DetalleVentaProducto;
import com.utp.cinerama.service.DetalleVentaProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalle-ventas-productos")
@RequiredArgsConstructor
@Slf4j
public class DetalleVentaProductoController {

    private final DetalleVentaProductoService detalleVentaProductoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DetalleVentaProducto>>> obtenerTodosLosDetalles() {
        log.info("Obteniendo todos los detalles de ventas de productos");
        List<DetalleVentaProducto> detalles = detalleVentaProductoService.obtenerTodosLosDetalles();
        return ResponseEntity.ok(ApiResponse.success("Detalles obtenidos exitosamente", detalles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DetalleVentaProducto>> obtenerDetallePorId(@PathVariable Long id) {
        log.info("Buscando detalle de venta con ID: {}", id);
        DetalleVentaProducto detalle = detalleVentaProductoService.obtenerDetallePorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de venta no encontrado con ID: " + id));
        return ResponseEntity.ok(ApiResponse.success("Detalle obtenido exitosamente", detalle));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DetalleVentaProducto>> crearDetalle(
            @Valid @RequestBody DetalleVentaProducto detalle) {
        log.info("Creando nuevo detalle de venta de producto");
        DetalleVentaProducto nuevoDetalle = detalleVentaProductoService.crearDetalle(detalle);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Detalle de venta creado exitosamente", nuevoDetalle));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DetalleVentaProducto>> actualizarDetalle(
            @PathVariable Long id, 
            @Valid @RequestBody DetalleVentaProducto detalle) {
        log.info("Actualizando detalle de venta con ID: {}", id);
        DetalleVentaProducto detalleActualizado = detalleVentaProductoService.actualizarDetalle(id, detalle);
        return ResponseEntity.ok(ApiResponse.success("Detalle actualizado exitosamente", detalleActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarDetalle(@PathVariable Long id) {
        log.info("Eliminando detalle de venta con ID: {}", id);
        detalleVentaProductoService.eliminarDetalle(id);
        return ResponseEntity.ok(ApiResponse.success("Detalle de venta eliminado exitosamente", null));
    }

    @GetMapping("/venta/{ventaProductoId}")
    public ResponseEntity<ApiResponse<List<DetalleVentaProducto>>> buscarPorVentaProducto(
            @PathVariable Long ventaProductoId) {
        log.info("Buscando detalles de venta para venta producto ID: {}", ventaProductoId);
        List<DetalleVentaProducto> detalles = detalleVentaProductoService.buscarPorVentaProducto(ventaProductoId);
        return ResponseEntity.ok(ApiResponse.success("Detalles obtenidos exitosamente", detalles));
    }
}

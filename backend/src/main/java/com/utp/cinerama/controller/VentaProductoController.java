package com.utp.cinerama.controller;

import com.utp.cinerama.dto.ApiResponse;
import com.utp.cinerama.exception.BadRequestException;
import com.utp.cinerama.exception.ResourceNotFoundException;
import com.utp.cinerama.model.Cliente;
import com.utp.cinerama.model.VentaProducto;
import com.utp.cinerama.service.VentaProductoService;
import com.utp.cinerama.service.ClienteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas-productos")
@RequiredArgsConstructor
@Slf4j
public class VentaProductoController {

    private final VentaProductoService ventaProductoService;
    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<VentaProducto>>> obtenerTodasLasVentas() {
        log.info("Obteniendo todas las ventas de productos");
        List<VentaProducto> ventas = ventaProductoService.obtenerTodasLasVentas();
        return ResponseEntity.ok(ApiResponse.success("Ventas obtenidas exitosamente", ventas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VentaProducto>> obtenerVentaPorId(@PathVariable Long id) {
        log.info("Buscando venta de producto con ID: {}", id);
        VentaProducto venta = ventaProductoService.obtenerVentaPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta de producto no encontrada con ID: " + id));
        return ResponseEntity.ok(ApiResponse.success("Venta obtenida exitosamente", venta));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VentaProducto>> crearVenta(
            @RequestParam Long clienteId, 
            @RequestParam String metodoPago) {
        log.info("Creando nueva venta de producto para cliente ID: {}", clienteId);
        Cliente cliente = clienteService.obtenerClientePorId(clienteId)
                .orElseThrow(() -> new BadRequestException("Cliente no encontrado con ID: " + clienteId));
        
        VentaProducto nuevaVenta = ventaProductoService.crearVenta(new VentaProducto(cliente, metodoPago, false));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Venta de producto creada exitosamente", nuevaVenta));
    }

    @PostMapping("/{id}/completar")
    public ResponseEntity<ApiResponse<VentaProducto>> completarVenta(@PathVariable Long id) {
        log.info("Completando venta de producto con ID: {}", id);
        VentaProducto ventaCompletada = ventaProductoService.completarVenta(id);
        return ResponseEntity.ok(ApiResponse.success("Venta completada exitosamente", ventaCompletada));
    }
}

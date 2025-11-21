package com.utp.cinerama.controller;

import com.utp.cinerama.dto.ApiResponse;
import com.utp.cinerama.exception.ResourceNotFoundException;
import com.utp.cinerama.model.Producto;
import com.utp.cinerama.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Slf4j
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Producto>>> obtenerTodosLosProductos() {
        log.info("Obteniendo todos los productos");
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        return ResponseEntity.ok(
            ApiResponse.success("Productos obtenidos exitosamente", productos)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Producto>> obtenerProductoPorId(@PathVariable Long id) {
        log.info("Buscando producto por ID: {}", id);
        Producto producto = productoService.obtenerProductoPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));
        
        return ResponseEntity.ok(
            ApiResponse.success("Producto obtenido exitosamente", producto)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Producto>> crearProducto(@Valid @RequestBody Producto producto) {
        log.info("Creando nuevo producto: {}", producto.getNombre());
        Producto nuevoProducto = productoService.crearProducto(producto);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Producto creado exitosamente", nuevoProducto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Producto>> actualizarProducto(
            @PathVariable Long id, 
            @Valid @RequestBody Producto producto) {
        
        log.info("Actualizando producto ID: {}", id);
        Producto productoActualizado = productoService.actualizarProducto(id, producto);
        
        return ResponseEntity.ok(
            ApiResponse.success("Producto actualizado exitosamente", productoActualizado)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> eliminarProducto(@PathVariable Long id) {
        log.info("Eliminando producto ID: {}", id);
        productoService.eliminarProducto(id);
        
        return ResponseEntity.ok(
            ApiResponse.success("Producto eliminado exitosamente")
        );
    }
}

package com.utp.cinerama.controller;

import com.utp.cinerama.dto.ApiResponse;
import com.utp.cinerama.exception.ResourceNotFoundException;
import com.utp.cinerama.model.Cliente;
import com.utp.cinerama.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Cliente>>> obtenerTodosLosClientes() {
        log.info("Obteniendo todos los clientes");
        List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
        return ResponseEntity.ok(ApiResponse.success("Clientes obtenidos exitosamente", clientes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Cliente>> obtenerClientePorId(@PathVariable Long id) {
        log.info("Buscando cliente con ID: {}", id);
        Cliente cliente = clienteService.obtenerClientePorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        return ResponseEntity.ok(ApiResponse.success("Cliente obtenido exitosamente", cliente));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Cliente>> crearCliente(@Valid @RequestBody Cliente cliente) {
        log.info("Creando nuevo cliente: {}", cliente.getNombre());
        Cliente nuevoCliente = clienteService.crearCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Cliente creado exitosamente", nuevoCliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Cliente>> actualizarCliente(
            @PathVariable Long id, 
            @Valid @RequestBody Cliente cliente) {
        log.info("Actualizando cliente con ID: {}", id);
        Cliente clienteActualizado = clienteService.actualizarCliente(id, cliente);
        return ResponseEntity.ok(ApiResponse.success("Cliente actualizado exitosamente", clienteActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarCliente(@PathVariable Long id) {
        log.info("Eliminando cliente con ID: {}", id);
        clienteService.eliminarCliente(id);
        return ResponseEntity.ok(ApiResponse.success("Cliente eliminado exitosamente", null));
    }
}

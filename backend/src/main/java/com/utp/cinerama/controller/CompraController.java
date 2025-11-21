package com.utp.cinerama.controller;

import com.utp.cinerama.dto.ApiResponse;
import com.utp.cinerama.dto.CalcularTotalRequestDTO;
import com.utp.cinerama.dto.CalcularTotalResponseDTO;
import com.utp.cinerama.dto.ConfirmarCompraRequestDTO;
import com.utp.cinerama.dto.ConfirmarCompraResponseDTO;
import com.utp.cinerama.model.Usuario;
import com.utp.cinerama.service.CompraService;
import com.utp.cinerama.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Orquestador de compras
 * Maneja el proceso completo de compra: cálculo de total y confirmación
 */
@RestController
@RequestMapping("/api/compras")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class CompraController {

    private final CompraService compraService;
    private final UsuarioService usuarioService;

    /**
     * Calcula el total de una compra antes de confirmar
     * POST /api/compras/calcular-total
     */
    @PostMapping("/calcular-total")
    public ResponseEntity<ApiResponse<CalcularTotalResponseDTO>> calcularTotal(
            @RequestBody CalcularTotalRequestDTO request) {
        try {
            Long clienteId = obtenerClienteIdDesdeAuth();
            CalcularTotalResponseDTO response = compraService.calcularTotal(request, clienteId);
            return ResponseEntity.ok(
                ApiResponse.success("Total calculado exitosamente", response)
            );
        } catch (IllegalArgumentException e) {
            log.error("Error de validación al calcular total: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                ApiResponse.<CalcularTotalResponseDTO>error(e.getMessage())
            );
        } catch (Exception e) {
            log.error("Error al calcular total: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.<CalcularTotalResponseDTO>error("Error al calcular total: " + e.getMessage())
            );
        }
    }

    /**
     * Confirma una compra (crea boletos, productos y pago de forma atómica)
     * POST /api/compras/confirmar
     */
    @PostMapping("/confirmar")
    public ResponseEntity<ApiResponse<ConfirmarCompraResponseDTO>> confirmarCompra(
            @RequestBody ConfirmarCompraRequestDTO request) {
        try {
            Long clienteId = obtenerClienteIdDesdeAuth();
            ConfirmarCompraResponseDTO response = compraService.confirmarCompra(request, clienteId);
            return ResponseEntity.ok(
                ApiResponse.success("Compra confirmada exitosamente", response)
            );
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Error de validación al confirmar compra: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                ApiResponse.<ConfirmarCompraResponseDTO>error(e.getMessage())
            );
        } catch (Exception e) {
            log.error("Error al confirmar compra: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.<ConfirmarCompraResponseDTO>error("Error al confirmar compra: " + e.getMessage())
            );
        }
    }

    /**
     * Obtiene el ID del cliente desde el contexto de seguridad
     */
    private Long obtenerClienteIdDesdeAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Usuario no autenticado");
        }
        
        // Obtener el username del token JWT
        String username = authentication.getName();
        
        // Obtener usuario completo
        Usuario usuario = usuarioService.obtenerPorUsername(username)
            .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        
        // Obtener clienteId
        if (usuario.getCliente() == null) {
            throw new IllegalStateException("El usuario no tiene un cliente asociado");
        }
        
        return usuario.getCliente().getId();
    }
}


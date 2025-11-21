package com.utp.cinerama.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO para respuestas de error
 * Proporciona informacion detallada sobre errores para el frontend
 * 
 * Uso en Angular:
 * - Mostrar mensajes de error al usuario
 * - Manejar validaciones campo por campo
 * - Debugging con timestamp y path
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    /**
     * Codigo de error (para manejo programatico en Angular)
     * Ejemplos: VALIDATION_ERROR, NOT_FOUND, UNAUTHORIZED
     */
    private String code;
    
    /**
     * Mensaje principal del error
     * Para mostrar al usuario
     */
    private String message;
    
    /**
     * Detalles adicionales del error
     * Para validaciones: mapa de campo -> mensaje
     * Para otros: informacion adicional
     */
    private Map<String, String> details;
    
    /**
     * Timestamp del error
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    /**
     * Path del endpoint donde ocurrio el error
     * Util para debugging
     */
    private String path;
    
    /**
     * Status HTTP del error
     */
    private int status;
}


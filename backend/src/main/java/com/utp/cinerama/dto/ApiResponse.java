package com.utp.cinerama.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta estandarizado para todas las APIs
 * Proporciona una estructura consistente para el frontend Angular
 * 
 * Uso:
 * - ApiResponse.success(data) - Para respuestas exitosas
 * - ApiResponse.error(message) - Para respuestas con error
 * 
 * @param <T> Tipo de dato que se retorna
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    /**
     * Indica si la operacion fue exitosa
     * true = exito, false = error
     */
    private boolean success;
    
    /**
     * Mensaje descriptivo (opcional)
     * Util para mostrar al usuario
     */
    private String message;
    
    /**
     * Datos de la respuesta
     * Puede ser un objeto, lista, etc.
     */
    private T data;
    
    /**
     * Timestamp de cuando se genero la respuesta
     * Util para debugging y logs
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * Metodo estatico para crear respuesta exitosa
     * 
     * @param data Datos a retornar
     * @return ApiResponse con success=true
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Metodo estatico para crear respuesta exitosa con mensaje
     * 
     * @param message Mensaje descriptivo
     * @param data Datos a retornar
     * @return ApiResponse con success=true y mensaje
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Metodo estatico para crear respuesta de error
     * 
     * @param message Mensaje de error
     * @return ApiResponse con success=false
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Metodo estatico para crear respuesta de error con datos
     * 
     * @param message Mensaje de error
     * @param data Datos adicionales del error
     * @return ApiResponse con success=false
     */
    public static <T> ApiResponse<T> error(String message, T data) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}


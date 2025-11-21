package com.utp.cinerama.exception;

/**
 * Excepcion lanzada cuando la solicitud del cliente es invalida
 * Ejemplo: Datos faltantes, formato incorrecto, reglas de negocio violadas
 */
public class BadRequestException extends RuntimeException {
    
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}


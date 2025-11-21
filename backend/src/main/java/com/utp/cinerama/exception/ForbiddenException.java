package com.utp.cinerama.exception;

/**
 * Excepcion lanzada cuando un usuario autenticado no tiene permisos
 * Ejemplo: Cliente intentando acceder a funciones de ADMIN
 */
public class ForbiddenException extends RuntimeException {
    
    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}


package com.utp.cinerama.exception;

/**
 * Excepcion lanzada cuando un usuario no esta autorizado
 * Ejemplo: Token invalido, sesion expirada, credenciales incorrectas
 */
public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}


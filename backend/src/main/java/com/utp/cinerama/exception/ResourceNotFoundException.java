package com.utp.cinerama.exception;

/**
 * Excepcion lanzada cuando un recurso no se encuentra en la base de datos
 * Ejemplo: Pelicula no encontrada, Cliente no encontrado, etc.
 */
public class ResourceNotFoundException extends RuntimeException {
    
    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s no encontrado con %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}


package com.utp.cinerama.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para mensajes simples de respuesta
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensajeDTO {
    
    private String mensaje;
    
    @Builder.Default
    private Boolean exitoso = true;
}


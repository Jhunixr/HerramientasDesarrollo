package com.utp.cinerama.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para respuesta de login con JWT token
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {

    private String token;
    
    @Builder.Default
    private String tipo = "Bearer";
    
    private String username;
    private String email;
    private List<String> roles;
    
    // IDs importantes para el frontend
    private Long userId;       // ID del usuario en la tabla Usuario
    private Long clienteId;    // ID del cliente asociado (si existe)
    
    // Información adicional
    private String nombreCompleto;
}


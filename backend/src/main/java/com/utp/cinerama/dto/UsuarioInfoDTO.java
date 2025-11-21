package com.utp.cinerama.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para información del usuario actual
 * Usado en endpoint /api/auth/me
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioInfoDTO {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private Long clienteId;
    private Boolean activo;
}


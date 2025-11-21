package com.utp.cinerama.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para login de usuarios
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDTO {

    private String username;
    private String password;
}


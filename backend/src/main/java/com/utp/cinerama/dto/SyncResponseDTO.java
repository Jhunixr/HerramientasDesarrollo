package com.utp.cinerama.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuesta de sincronización de películas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncResponseDTO {
    
    private Integer totalPeliculasAPI;
    private Integer peliculasNuevas;
    private Integer peliculasActualizadas;
    private Integer peliculasOmitidas;
    private String mensaje;
}


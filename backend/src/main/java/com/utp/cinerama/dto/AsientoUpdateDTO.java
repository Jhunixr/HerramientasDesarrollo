package com.utp.cinerama.dto;

import com.utp.cinerama.model.Asiento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsientoUpdateDTO {
    private Long asientoId;
    private Long funcionId;
    private Asiento.EstadoAsiento estado;
    private Long reservadoPor;
    private String accion; // "reservar", "liberar", "confirmar"
}






package com.utp.cinerama.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para calcular el total de una compra
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalcularTotalRequestDTO {
    private Long funcionId;
    private List<Long> asientoIds; // IDs de los asientos seleccionados
    private List<ProductoItemDTO> productos; // Productos opcionales (cancha, gaseosas, combos, etc.)
}



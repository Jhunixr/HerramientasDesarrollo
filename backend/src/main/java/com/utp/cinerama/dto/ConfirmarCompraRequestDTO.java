package com.utp.cinerama.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para confirmar una compra
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmarCompraRequestDTO {
    private Long funcionId;
    private List<Long> asientoIds;
    private List<ProductoItemDTO> productos;
    private String metodoPago; // EFECTIVO, TARJETA, TRANSFERENCIA
    private String tipoComprobante; // BOLETA, FACTURA
}



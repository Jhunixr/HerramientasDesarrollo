package com.utp.cinerama.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO con el desglose de precios de la compra
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalcularTotalResponseDTO {
    private Double subtotalBoletos;
    private Double subtotalProductos;
    private Double total;
    private List<BoletoItemDTO> boletos;
    private List<ProductoDetalleDTO> productos;
}



package com.utp.cinerama.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para detalle de producto en el cálculo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoDetalleDTO {
    private Long productoId;
    private String nombre;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}



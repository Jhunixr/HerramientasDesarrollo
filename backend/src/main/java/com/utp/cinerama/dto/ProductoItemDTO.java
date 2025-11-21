package com.utp.cinerama.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para un item de producto en la compra
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoItemDTO {
    private Long productoId;
    private Integer cantidad;
}



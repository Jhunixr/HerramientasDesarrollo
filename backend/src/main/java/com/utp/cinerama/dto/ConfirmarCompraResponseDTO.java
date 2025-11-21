package com.utp.cinerama.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO con la respuesta de confirmación de compra
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmarCompraResponseDTO {
    private String numeroConfirmacion; // Número único de confirmación
    private LocalDateTime fechaCompra;
    private Double total;
    private String metodoPago;
    private String tipoComprobante;
    private List<BoletoResumenDTO> boletos;
    private List<ProductoDetalleDTO> productos;
    private PeliculaResumenDTO pelicula;
    private FuncionResumenDTO funcion;
}



package com.utp.cinerama.service;

import com.utp.cinerama.model.DetalleVentaProducto;

import java.util.List;
import java.util.Optional;

public interface DetalleVentaProductoService {
    List<DetalleVentaProducto> obtenerTodosLosDetalles();
    Optional<DetalleVentaProducto> obtenerDetallePorId(Long id);
    DetalleVentaProducto crearDetalle(DetalleVentaProducto detalleVentaProducto);
    DetalleVentaProducto actualizarDetalle(Long id, DetalleVentaProducto detalleVentaProducto);
    void eliminarDetalle(Long id);
    List<DetalleVentaProducto> buscarPorVentaProducto(Long ventaProductoId);
}

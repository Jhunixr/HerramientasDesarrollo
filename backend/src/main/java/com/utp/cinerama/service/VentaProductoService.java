package com.utp.cinerama.service;

import com.utp.cinerama.model.VentaProducto;

import java.util.List;
import java.util.Optional;

public interface VentaProductoService {
    List<VentaProducto> obtenerTodasLasVentas();
    Optional<VentaProducto> obtenerVentaPorId(Long id);
    VentaProducto crearVenta(VentaProducto ventaProducto);
    VentaProducto completarVenta(Long id);
}

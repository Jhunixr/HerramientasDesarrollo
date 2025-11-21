package com.utp.cinerama.service;

import com.utp.cinerama.model.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> obtenerTodosLosProductos();
    Optional<Producto> obtenerProductoPorId(Long id);
    Producto crearProducto(Producto producto);
    Producto actualizarProducto(Long id, Producto producto);
    void eliminarProducto(Long id);
}

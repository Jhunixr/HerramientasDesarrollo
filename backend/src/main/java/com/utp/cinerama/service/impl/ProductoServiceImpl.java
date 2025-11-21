package com.utp.cinerama.service.impl;

import com.utp.cinerama.model.Producto;
import com.utp.cinerama.repository.ProductoRepository;
import com.utp.cinerama.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Producto actualizarProducto(Long id, Producto producto) {
        return productoRepository.findById(id)
                .map(p -> {
                    p.setNombre(producto.getNombre());
                    p.setDescripcion(producto.getDescripcion());
                    p.setCategoria(producto.getCategoria());
                    p.setPrecio(producto.getPrecio());
                    p.setStock(producto.getStock());
                    p.setActivo(producto.getActivo());
                    return productoRepository.save(p);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @Override
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}

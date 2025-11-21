package com.utp.cinerama.service.impl;

import com.utp.cinerama.model.VentaProducto;
import com.utp.cinerama.repository.VentaProductoRepository;
import com.utp.cinerama.service.VentaProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VentaProductoServiceImpl implements VentaProductoService {

    @Autowired
    private VentaProductoRepository ventaProductoRepository;

    @Override
    public List<VentaProducto> obtenerTodasLasVentas() {
        return ventaProductoRepository.findAll();
    }

    @Override
    public Optional<VentaProducto> obtenerVentaPorId(Long id) {
        return ventaProductoRepository.findById(id);
    }

    @Override
    public VentaProducto crearVenta(VentaProducto ventaProducto) {
        return ventaProductoRepository.save(ventaProducto);
    }

    @Override
    public VentaProducto completarVenta(Long id) {
        return ventaProductoRepository.findById(id)
                .map(venta -> {
                    venta.setCompletada(true);
                    return ventaProductoRepository.save(venta);
                })
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
    }
}

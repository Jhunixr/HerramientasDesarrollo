package com.utp.cinerama.service.impl;

import com.utp.cinerama.model.DetalleVentaProducto;
import com.utp.cinerama.repository.DetalleVentaProductoRepository;
import com.utp.cinerama.service.DetalleVentaProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleVentaProductoServiceImpl implements DetalleVentaProductoService {

    @Autowired
    private DetalleVentaProductoRepository detalleVentaProductoRepository;

    @Override
    public List<DetalleVentaProducto> obtenerTodosLosDetalles() {
        return detalleVentaProductoRepository.findAll();
    }

    @Override
    public Optional<DetalleVentaProducto> obtenerDetallePorId(Long id) {
        return detalleVentaProductoRepository.findById(id);
    }

    @Override
    public DetalleVentaProducto crearDetalle(DetalleVentaProducto detalleVentaProducto) {
        return detalleVentaProductoRepository.save(detalleVentaProducto);
    }

    @Override
    public DetalleVentaProducto actualizarDetalle(Long id, DetalleVentaProducto detalleVentaProducto) {
        return detalleVentaProductoRepository.findById(id)
                .map(detalle -> {
                    detalle.setProducto(detalleVentaProducto.getProducto());
                    detalle.setCantidad(detalleVentaProducto.getCantidad());
                    return detalleVentaProductoRepository.save(detalle);
                })
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));
    }

    @Override
    public void eliminarDetalle(Long id) {
        detalleVentaProductoRepository.deleteById(id);
    }

    @Override
    public List<DetalleVentaProducto> buscarPorVentaProducto(Long ventaProductoId) {
        return detalleVentaProductoRepository.findByVentaProductoId(ventaProductoId);
    }
}

package com.utp.cinerama.service.impl;

import com.utp.cinerama.model.Pago;
import com.utp.cinerama.repository.PagoRepository;
import com.utp.cinerama.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PagoServiceImpl implements PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Override
    public List<Pago> obtenerTodosLosPagos() {
        return pagoRepository.findAll();
    }

    @Override
    public Optional<Pago> obtenerPagoPorId(Long id) {
        return pagoRepository.findById(id);
    }

    @Override
    public Pago crearPago(Pago pago) {
        return pagoRepository.save(pago);
    }

    @Override
    public void eliminarPago(Long id) {
        pagoRepository.deleteById(id);
    }

    public List<Pago> buscarPorEstado(Pago.EstadoPago estado) {
    return pagoRepository.findByEstado(estado);
}
}

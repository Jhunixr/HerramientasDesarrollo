package com.utp.cinerama.service;

import com.utp.cinerama.model.Boleto;

import java.util.List;
import java.util.Optional;

public interface BoletoService {
    List<Boleto> obtenerTodosLosBoletos();
    Optional<Boleto> obtenerBoletoPorId(Long id);
    Boleto crearBoleto(Boleto boleto);
    Boleto actualizarBoleto(Long id, Boleto boleto);
    void eliminarBoleto(Long id);
    List<Boleto> buscarPorCliente(Long clienteId);
    List<Boleto> buscarPorFuncion(Long funcionId);
    List<Boleto> buscarPorEstado(Boleto.EstadoBoleto estado); 
}

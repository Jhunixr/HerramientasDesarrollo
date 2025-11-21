package com.utp.cinerama.service;

import com.utp.cinerama.model.Sala;

import java.util.List;
import java.util.Optional;

public interface SalaService {
    List<Sala> obtenerTodasLasSalas();
    Optional<Sala> obtenerSalaPorId(Long id);
    Sala crearSala(Sala sala);
    Sala actualizarSala(Long id, Sala sala);
    void eliminarSala(Long id);
    List<Sala> buscarPorTipo(Sala.TipoSala tipo);
    List<Sala> buscarSalasActivas();
    List<Sala> buscarPorCapacidadMinima(Integer capacidad);
}

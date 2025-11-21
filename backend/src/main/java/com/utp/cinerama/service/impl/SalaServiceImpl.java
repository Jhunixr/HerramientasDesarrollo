package com.utp.cinerama.service.impl;

import com.utp.cinerama.model.Sala;
import com.utp.cinerama.repository.SalaRepository;
import com.utp.cinerama.service.SalaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalaServiceImpl implements SalaService {

    @Autowired
    private SalaRepository salaRepository;

    @Override
    public List<Sala> obtenerTodasLasSalas() {
        return salaRepository.findAll();
    }

    @Override
    public Optional<Sala> obtenerSalaPorId(Long id) {
        return salaRepository.findById(id);
    }

    @Override
    public Sala crearSala(Sala sala) {
        return salaRepository.save(sala);
    }

    @Override
    public Sala actualizarSala(Long id, Sala sala) {
        return salaRepository.findById(id)
                .map(s -> {
                    s.setNombre(sala.getNombre());
                    s.setDescripcion(sala.getDescripcion());
                    s.setCapacidad(sala.getCapacidad());
                    s.setTipo(sala.getTipo());
                    s.setActiva(sala.getActiva());
                    return salaRepository.save(s);
                })
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));
    }

    @Override
    public void eliminarSala(Long id) {
        salaRepository.deleteById(id);
    }

    @Override
    public List<Sala> buscarPorTipo(Sala.TipoSala tipo) {
        return salaRepository.findByTipo(tipo);
    }

    @Override
    public List<Sala> buscarSalasActivas() {
        return salaRepository.findByActivaTrue();
    }

    @Override
    public List<Sala> buscarPorCapacidadMinima(Integer capacidad) {
        return salaRepository.findByCapacidadGreaterThanEqual(capacidad);
    }
}

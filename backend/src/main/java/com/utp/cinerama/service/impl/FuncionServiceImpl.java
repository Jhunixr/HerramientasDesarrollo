package com.utp.cinerama.service.impl;

import com.utp.cinerama.model.Funcion;
import com.utp.cinerama.model.Pelicula;
import com.utp.cinerama.model.Sala;
import com.utp.cinerama.repository.FuncionRepository;
import com.utp.cinerama.repository.PeliculaRepository;
import com.utp.cinerama.repository.SalaRepository;
import com.utp.cinerama.service.FuncionService;
import com.utp.cinerama.service.PeliculaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FuncionServiceImpl implements FuncionService {

    private final FuncionRepository funcionRepository;
    private final PeliculaRepository peliculaRepository;
    private final SalaRepository salaRepository;
    private final PeliculaService peliculaService;

    @Override
    public List<Funcion> obtenerTodasLasFunciones() {
        try {
            return funcionRepository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener todas las funciones: {}", e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public Optional<Funcion> obtenerFuncionPorId(Long id) {
        return funcionRepository.findById(id);
    }

    @Override
    public Funcion crearFuncion(Funcion funcion) {
        return funcionRepository.save(funcion);
    }

    @Override
    public Funcion actualizarFuncion(Long id, Funcion funcion) {
        return funcionRepository.findById(id)
                .map(f -> {
                    f.setPelicula(funcion.getPelicula());
                    f.setSala(funcion.getSala());
                    f.setFechaHora(funcion.getFechaHora());
                    f.setAsientosDisponibles(funcion.getAsientosDisponibles());
                    f.setAsientosTotales(funcion.getAsientosTotales());
                    f.setPrecio(funcion.getPrecio());
                    f.setActiva(funcion.getActiva());
                    return funcionRepository.save(f);
                })
                .orElseThrow(() -> new RuntimeException("Función no encontrada"));
    }

    @Override
    public void eliminarFuncion(Long id) {
        funcionRepository.deleteById(id);
    }

    @Override
    public List<Funcion> obtenerFuncionesPorPelicula(Long peliculaId) {
        try {
            return funcionRepository.findByPeliculaId(peliculaId);
        } catch (Exception e) {
            log.error("Error al obtener funciones por película: {}", e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public List<Funcion> obtenerFuncionesActivas() {
        try {
            return funcionRepository.findAll().stream()
                    .filter(f -> f.getActiva() != null && f.getActiva())
                    .filter(f -> f.getFechaHora() != null && f.getFechaHora().isAfter(LocalDateTime.now()))
                    .toList();
        } catch (Exception e) {
            log.error("Error al obtener funciones activas: {}", e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public List<Funcion> obtenerFuncionesPorSala(Long salaId) {
        try {
            return funcionRepository.findBySalaId(salaId);
        } catch (Exception e) {
            log.error("Error al obtener funciones por sala: {}", e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    @Transactional
    public List<Funcion> generarFuncionesParaPeliculasEnCartelera() {
        log.info("Generando funciones automáticamente para películas en cartelera");
        List<Funcion> funcionesGeneradas = new ArrayList<>();
        
        try {
            // Obtener películas activas
            List<Pelicula> peliculasActivas = peliculaService.obtenerPeliculasActivas();
            if (peliculasActivas.isEmpty()) {
                log.warn("No hay películas activas para generar funciones");
                return funcionesGeneradas;
            }

            // Obtener salas activas
            List<Sala> salasActivas = salaRepository.findByActivaTrue();
            if (salasActivas.isEmpty()) {
                log.warn("No hay salas activas. Creando sala por defecto...");
                // Crear una sala por defecto si no existe
                Sala salaDefault = Sala.builder()
                        .nombre("Sala 1")
                        .descripcion("Sala principal")
                        .capacidad(100)
                        .tipo(Sala.TipoSala.NORMAL)
                        .activa(true)
                        .build();
                salasActivas = List.of(salaRepository.save(salaDefault));
            }

            // Horarios típicos de cine (10:00, 13:00, 16:00, 19:00, 22:00)
            LocalTime[] horarios = {
                LocalTime.of(10, 0),
                LocalTime.of(13, 0),
                LocalTime.of(16, 0),
                LocalTime.of(19, 0),
                LocalTime.of(22, 0)
            };

            LocalDate hoy = LocalDate.now();
            LocalDate finSemana = hoy.plusDays(7); // Generar funciones para la próxima semana

            // Generar funciones para cada película activa
            for (Pelicula pelicula : peliculasActivas) {
                // Generar funciones para los próximos 7 días
                for (LocalDate fecha = hoy; fecha.isBefore(finSemana) || fecha.isEqual(finSemana); fecha = fecha.plusDays(1)) {
                    // Asignar a diferentes salas rotativamente
                    Sala sala = salasActivas.get((int) (fecha.toEpochDay() % salasActivas.size()));
                    
                    // Generar 3-4 funciones por día (no todas las horas)
                    int funcionesPorDia = 3;
                    for (int i = 0; i < funcionesPorDia && i < horarios.length; i++) {
                        LocalTime horario = horarios[i + (fecha.getDayOfWeek().getValue() % 2)]; // Variar horarios
                        LocalDateTime fechaHora = LocalDateTime.of(fecha, horario);
                        
                        // Solo crear si es en el futuro
                        if (fechaHora.isAfter(LocalDateTime.now())) {
                            // Verificar si ya existe una función similar
                            boolean existe = funcionRepository.findAll().stream()
                                    .anyMatch(f -> f.getPelicula().getId().equals(pelicula.getId())
                                            && f.getSala().getId().equals(sala.getId())
                                            && f.getFechaHora().equals(fechaHora));
                            
                            if (!existe) {
                                Funcion funcion = Funcion.builder()
                                        .pelicula(pelicula)
                                        .sala(sala)
                                        .fechaHora(fechaHora)
                                        .asientosTotales(sala.getCapacidad())
                                        .asientosDisponibles(sala.getCapacidad())
                                        .precio(15.0) // Precio por defecto
                                        .activa(true)
                                        .build();
                                
                                Funcion funcionGuardada = funcionRepository.save(funcion);
                                funcionesGeneradas.add(funcionGuardada);
                                log.info("Función generada: {} - {} - {}", pelicula.getTitulo(), sala.getNombre(), fechaHora);
                            }
                        }
                    }
                }
            }

            log.info("Total de funciones generadas: {}", funcionesGeneradas.size());
            return funcionesGeneradas;
            
        } catch (Exception e) {
            log.error("Error al generar funciones: {}", e.getMessage(), e);
            return funcionesGeneradas;
        }
    }
}

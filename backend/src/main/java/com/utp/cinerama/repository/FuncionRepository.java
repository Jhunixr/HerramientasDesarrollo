package com.utp.cinerama.repository;

import com.utp.cinerama.model.Funcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FuncionRepository extends JpaRepository<Funcion, Long> {
    @Query("SELECT f FROM Funcion f WHERE f.pelicula.id = :peliculaId")
    List<Funcion> findByPeliculaId(@Param("peliculaId") Long peliculaId);

    @Query("SELECT f FROM Funcion f WHERE f.sala.id = :salaId")
    List<Funcion> findBySalaId(@Param("salaId") Long salaId);
    
    // Buscar funciones por fecha (solo la parte de fecha de fechaHora)
    @Query("SELECT f FROM Funcion f WHERE DATE(f.fechaHora) = :fecha")
    List<Funcion> findByFecha(@Param("fecha") LocalDate fecha);
    
    // Buscar funciones entre dos fechas y horas
    List<Funcion> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
}

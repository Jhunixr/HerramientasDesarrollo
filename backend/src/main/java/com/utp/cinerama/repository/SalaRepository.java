package com.utp.cinerama.repository;

import com.utp.cinerama.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {
    @Query("SELECT s FROM Sala s WHERE s.tipo = :tipo")
    List<Sala> findByTipo(@Param("tipo") Sala.TipoSala tipo);

    @Query("SELECT s FROM Sala s WHERE s.activa = TRUE")
    List<Sala> findByActivaTrue();

    @Query("SELECT s FROM Sala s WHERE s.capacidad >= :capacidad")
    List<Sala> findByCapacidadGreaterThanEqual(@Param("capacidad") Integer capacidad);
}

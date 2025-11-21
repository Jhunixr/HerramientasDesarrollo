package com.utp.cinerama.repository;

import com.utp.cinerama.model.Pelicula;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {
    
    // Buscar por TMDb ID (unico)
    @Query("SELECT p FROM Pelicula p WHERE p.tmdbId = :tmdbId")
    Optional<Pelicula> findByTmdbId(@Param("tmdbId") Long tmdbId);
    
    // Verificar si existe por TMDb ID
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM Pelicula p WHERE p.tmdbId = :tmdbId")
    boolean existsByTmdbId(@Param("tmdbId") Long tmdbId);
    
    // Busquedas SIN paginacion (para compatibilidad)
    @Query("SELECT p FROM Pelicula p WHERE LOWER(p.genero) LIKE LOWER(CONCAT('%', :genero, '%'))")
    List<Pelicula> findByGeneroContainingIgnoreCase(@Param("genero") String genero);

    @Query("SELECT p FROM Pelicula p WHERE LOWER(p.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))")
    List<Pelicula> findByTituloContainingIgnoreCase(@Param("titulo") String titulo);

    @Query("SELECT p FROM Pelicula p WHERE p.clasificacion = :clasificacion")
    List<Pelicula> findByClasificacion(@Param("clasificacion") String clasificacion);
    
    // Busquedas CON paginacion
    @Query(value = "SELECT p FROM Pelicula p WHERE LOWER(p.genero) LIKE LOWER(CONCAT('%', :genero, '%'))")
    Page<Pelicula> findByGeneroContainingIgnoreCase(@Param("genero") String genero, Pageable pageable);

    @Query(value = "SELECT p FROM Pelicula p WHERE LOWER(p.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))")
    Page<Pelicula> findByTituloContainingIgnoreCase(@Param("titulo") String titulo, Pageable pageable);

    @Query(value = "SELECT p FROM Pelicula p WHERE p.clasificacion = :clasificacion")
    Page<Pelicula> findByClasificacion(@Param("clasificacion") String clasificacion, Pageable pageable);
    
    // Buscar peliculas activas
    @Query("SELECT p FROM Pelicula p WHERE p.activa = TRUE")
    List<Pelicula> findByActivaTrue();

    @Query(value = "SELECT p FROM Pelicula p WHERE p.activa = TRUE")
    Page<Pelicula> findByActivaTrue(Pageable pageable);
    
    // Buscar por popularidad (con manejo de nulls)
    @Query("SELECT p FROM Pelicula p WHERE p.popularidad IS NOT NULL ORDER BY p.popularidad DESC")
    List<Pelicula> findByOrderByPopularidadDesc();

    @Query(value = "SELECT p FROM Pelicula p WHERE p.popularidad IS NOT NULL ORDER BY p.popularidad DESC")
    Page<Pelicula> findByOrderByPopularidadDesc(Pageable pageable);
    
    // Buscar por voto promedio (con manejo de nulls)
    @Query("SELECT p FROM Pelicula p WHERE p.votoPromedio IS NOT NULL ORDER BY p.votoPromedio DESC")
    List<Pelicula> findByOrderByVotoPromedioDesc();

    @Query(value = "SELECT p FROM Pelicula p WHERE p.votoPromedio IS NOT NULL ORDER BY p.votoPromedio DESC")
    Page<Pelicula> findByOrderByVotoPromedioDesc(Pageable pageable);
}

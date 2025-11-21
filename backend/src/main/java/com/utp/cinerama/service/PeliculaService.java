package com.utp.cinerama.service;

import com.utp.cinerama.dto.SyncResponseDTO;
import com.utp.cinerama.model.Pelicula;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface PeliculaService {
    // Metodos sin paginacion (para compatibilidad)
    List<Pelicula> obtenerTodasLasPeliculas();
    Optional<Pelicula> obtenerPeliculaPorId(Long id);
    Pelicula crearPelicula(Pelicula pelicula);
    Pelicula actualizarPelicula(Long id, Pelicula pelicula);
    void eliminarPelicula(Long id);
    List<Pelicula> buscarPorGenero(String genero);
    List<Pelicula> buscarPorTitulo(String titulo);
    List<Pelicula> buscarPorClasificacion(String clasificacion);
    
    // Nuevos metodos CON paginacion
    Page<Pelicula> obtenerPeliculasPaginadas(int page, int size, String sortBy);
    Page<Pelicula> buscarPorGeneroPaginado(String genero, int page, int size);
    Page<Pelicula> buscarPorTituloPaginado(String titulo, int page, int size);
    
    // Metodos para integracion con TMDb
    SyncResponseDTO sincronizarPeliculasDesdeAPI(Integer paginas);
    List<Pelicula> obtenerPeliculasActivas();
    List<Pelicula> obtenerPeliculasPorPopularidad();
    List<Pelicula> obtenerPeliculasPorVoto();
    Optional<Pelicula> obtenerPorTmdbId(Long tmdbId);
    Pelicula agregarPeliculaDesdeTMDb(Long tmdbId);
}

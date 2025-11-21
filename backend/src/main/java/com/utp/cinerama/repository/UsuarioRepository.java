package com.utp.cinerama.repository;

import com.utp.cinerama.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    /**
     * Buscar usuario por username (para login)
     */
    Optional<Usuario> findByUsername(String username);
    /**
     * Buscar usuario por email
     */
    Optional<Usuario> findByEmail(String email);
    /**
     * Verificar si existe un username
     */
    Boolean existsByUsername(String username);
    /**
     * Verificar si existe un email
     */
    Boolean existsByEmail(String email);
    /**
     * Buscar usuarios activos
     */
    List<Usuario> findByActivoTrue();
    /**
     * Buscar usuarios por rol
     */
    @Query("SELECT u FROM Usuario u JOIN u.roles r WHERE r.nombre = :nombreRol")
    List<Usuario> findByRolNombre(@Param("nombreRol") String nombreRol);
    /**
     * Buscar usuarios con cliente asociado
     */
    @Query("SELECT u FROM Usuario u WHERE u.cliente IS NOT NULL")
    List<Usuario> findUsuariosConCliente();
    /**
     * Buscar usuarios sin cliente asociado
     */
    @Query("SELECT u FROM Usuario u WHERE u.cliente IS NULL")
    List<Usuario> findUsuariosSinCliente();
    /**
     * Buscar usuarios inactivos por más de X días
     */
    @Query("SELECT u FROM Usuario u WHERE u.ultimoAcceso < :fechaLimite")
    List<Usuario> findUsuariosInactivos(@Param("fechaLimite") LocalDateTime fechaLimite);
    /**
     * Contar usuarios por rol
     */
    @Query("SELECT COUNT(u) FROM Usuario u JOIN u.roles r WHERE r.nombre = :nombreRol")
    Long countByRolNombre(@Param("nombreRol") String nombreRol);
}


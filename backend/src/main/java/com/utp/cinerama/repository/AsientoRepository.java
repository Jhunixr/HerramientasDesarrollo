package com.utp.cinerama.repository;

import com.utp.cinerama.model.Asiento;
import com.utp.cinerama.model.Asiento.EstadoAsiento;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AsientoRepository extends JpaRepository<Asiento, Long> {
    /**
     * Obtiene un asiento con bloqueo pesimista para evitar condiciones de carrera
     * Uso: Durante reserva simultánea de asientos
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Asiento a WHERE a.id = :id")
    Optional<Asiento> findByIdWithLock(@Param("id") Long id);
    /**
     * Busca asiento por función, fila y número con bloqueo
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Asiento a WHERE a.funcion.id = :funcionId " +
           "AND a.fila = :fila AND a.numero = :numero")
    Optional<Asiento> findByFuncionIdAndFilaAndNumeroWithLock(
            @Param("funcionId") Long funcionId,
            @Param("fila") String fila,
            @Param("numero") Integer numero
    );
    /**
     * Obtiene todos los asientos de una función (para mostrar mapa)
     */
    @Query("SELECT a FROM Asiento a WHERE a.funcion.id = :funcionId ORDER BY a.fila ASC, a.numero ASC")
    List<Asiento> findByFuncionIdOrderByFilaAscNumeroAsc(@Param("funcionId") Long funcionId);

    /**
     * Obtiene asientos por estado de una función
     */
    @Query("SELECT a FROM Asiento a WHERE a.funcion.id = :funcionId AND a.estado = :estado")
    List<Asiento> findByFuncionIdAndEstado(@Param("funcionId") Long funcionId, @Param("estado") EstadoAsiento estado);

    /**
     * Encuentra asientos reservados que expiraron
     */
    @Query("SELECT a FROM Asiento a WHERE a.estado = 'RESERVADO' " +
           "AND a.fechaExpiracionReserva < :fechaActual")
    List<Asiento> findAsientosReservadosExpirados(@Param("fechaActual") LocalDateTime fechaActual);

    /**
     * Cuenta asientos por estado en una función
     */
    @Query("SELECT COUNT(a) FROM Asiento a WHERE a.funcion.id = :funcionId AND a.estado = :estado")
    long countByFuncionIdAndEstado(@Param("funcionId") Long funcionId, @Param("estado") EstadoAsiento estado);

    /**
     * Libera múltiples asientos expirados (usado por scheduler)
     */
    @Modifying
    @Query("UPDATE Asiento a SET a.estado = 'DISPONIBLE', a.reservadoPor = NULL, " +
           "a.fechaReserva = NULL, a.fechaExpiracionReserva = NULL " +
           "WHERE a.estado = 'RESERVADO' AND a.fechaExpiracionReserva < :fechaActual")
    int liberarAsientosExpirados(@Param("fechaActual") LocalDateTime fechaActual);

    /**
     * Verifica si existe un asiento específico
     */
    boolean existsByFuncionIdAndFilaAndNumero(Long funcionId, String fila, Integer numero);

       /**
        * Obtiene asientos por tipo (solo NORMAL en Cinerama Chimbote)
        */
    @Query("SELECT a FROM Asiento a WHERE a.funcion.id = :funcionId AND a.tipo = :tipo")
    List<Asiento> findByFuncionIdAndTipo(@Param("funcionId") Long funcionId, @Param("tipo") Asiento.TipoAsiento tipo);
}


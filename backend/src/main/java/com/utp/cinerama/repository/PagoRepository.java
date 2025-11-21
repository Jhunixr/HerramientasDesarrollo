package com.utp.cinerama.repository;

import com.utp.cinerama.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    @Query("SELECT p FROM Pago p WHERE p.cliente.id = :clienteId")
    List<Pago> findByClienteId(@Param("clienteId") Long clienteId);

    @Query("SELECT p FROM Pago p WHERE p.estado = :estado")
    List<Pago> findByEstado(@Param("estado") Pago.EstadoPago estado);
}

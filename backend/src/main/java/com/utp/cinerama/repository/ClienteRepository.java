package com.utp.cinerama.repository;

import com.utp.cinerama.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("SELECT c FROM Cliente c WHERE LOWER(c.email) = LOWER(:email)")
    Optional<Cliente> findByEmail(@Param("email") String email);

    @Query("SELECT c FROM Cliente c WHERE c.telefono = :telefono")
    Optional<Cliente> findByTelefono(@Param("telefono") String telefono);

    Boolean existsByEmailIgnoreCase(String email);

    Boolean existsByTelefono(String telefono);

    Boolean existsByNumeroDocumento(String numeroDocumento);
}

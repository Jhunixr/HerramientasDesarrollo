package com.utp.cinerama.repository;

import com.utp.cinerama.model.DetalleVentaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleVentaProductoRepository extends JpaRepository<DetalleVentaProducto, Long> {
    @Query("SELECT d FROM DetalleVentaProducto d WHERE d.ventaProducto.id = :ventaProductoId")
    List<DetalleVentaProducto> findByVentaProductoId(@Param("ventaProductoId") Long ventaProductoId);
}

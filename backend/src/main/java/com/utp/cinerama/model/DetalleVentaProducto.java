package com.utp.cinerama.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detalle_venta_producto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DetalleVentaProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "venta_producto_id", nullable = false)
    private VentaProducto ventaProducto;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;
}

package com.utp.cinerama.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas_productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VentaProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private String metodoPago;

    @Column(nullable = false)
    private Boolean completada;

    // Relación con DetalleVentaProducto
    @OneToMany(mappedBy = "ventaProducto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVentaProducto> detalles = new ArrayList<>();

    // Constructor adicional
    public VentaProducto(Cliente cliente, String metodoPago, Boolean completada) {
        this.cliente = cliente;
        this.metodoPago = metodoPago;
        this.completada = completada;
    }
}

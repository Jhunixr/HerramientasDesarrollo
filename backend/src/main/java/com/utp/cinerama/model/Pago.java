package com.utp.cinerama.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pagos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false)
    private String metodoPago; // Tarjeta, Efectivo, Yape, Plin

    @Column(nullable = false)
    private String tipoComprobante; // Boleta, Factura

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estado;

    // Enum para el estado del pago
    public enum EstadoPago {
        PENDIENTE,
        COMPLETADO,
        CANCELADO
    }
}

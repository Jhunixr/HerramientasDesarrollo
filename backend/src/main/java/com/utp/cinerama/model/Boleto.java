package com.utp.cinerama.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "boletos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Boleto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "funcion_id", nullable = false)
    private Funcion funcion;

    // ⭐ Nuevo: Relación con Asiento
    @OneToOne
    @JoinColumn(name = "asiento_id", nullable = false)
    private Asiento asiento;

    @Column(nullable = false)
    private Double precio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoBoleto estado;

    @Column(nullable = false)
    private LocalDateTime fechaCompra;

    public enum EstadoBoleto {
        RESERVADO,
        PAGADO,
        CANCELADO,
        USADO
    }
}

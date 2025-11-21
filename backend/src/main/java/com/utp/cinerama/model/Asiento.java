package com.utp.cinerama.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "asientos", 
    uniqueConstraints = @UniqueConstraint(columnNames = {"funcion_id", "fila", "numero"}),
    indexes = {
        @Index(name = "idx_funcion_estado", columnList = "funcion_id, estado"),
        @Index(name = "idx_reserva_expiracion", columnList = "fecha_expiracion_reserva")
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "funcion")
public class Asiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcion_id", nullable = false)
    private Funcion funcion;

    @Column(nullable = false, length = 2)
    private String fila; 

    @Column(nullable = false)
    private Integer numero; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoAsiento tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoAsiento estado;

    @Column(nullable = false)
    private Double precio;

    // Para reservas temporales
    @Column(name = "reservado_por")
    private Long reservadoPor; // ID del cliente que reservó

    @Column(name = "fecha_reserva")
    private LocalDateTime fechaReserva;

    @Column(name = "fecha_expiracion_reserva")
    private LocalDateTime fechaExpiracionReserva;

    // Relación con Boleto (cuando se confirma la compra)
    @OneToOne(mappedBy = "asiento")
    private Boleto boleto;

    public enum TipoAsiento {
        
        NORMAL("Normal", 1.0);

        private final String descripcion;
        private final Double multiplicadorPrecio;

        TipoAsiento(String descripcion, Double multiplicadorPrecio) {
            this.descripcion = descripcion;
            this.multiplicadorPrecio = multiplicadorPrecio;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public Double getMultiplicadorPrecio() {
            return multiplicadorPrecio;
        }
    }

    public enum EstadoAsiento {
        DISPONIBLE("Disponible"),
        RESERVADO("Reservado temporalmente"),
        OCUPADO("Ocupado"),
        BLOQUEADO("Bloqueado/Mantenimiento");

        private final String descripcion;

        EstadoAsiento(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    // Método helper para obtener código del asiento
    public String getCodigoAsiento() {
        return fila + numero;
    }

    // Verificar si el asiento está disponible
    public boolean estaDisponible() {
        return estado == EstadoAsiento.DISPONIBLE;
    }

    // Verificar si la reserva ha expirado
    public boolean reservaExpirada() {
        if (estado != EstadoAsiento.RESERVADO) {
            return false;
        }
        return fechaExpiracionReserva != null && 
               LocalDateTime.now().isAfter(fechaExpiracionReserva);
    }

    // Reservar asiento temporalmente (10 minutos)
    public void reservarTemporal(Long clienteId) {
        this.estado = EstadoAsiento.RESERVADO;
        this.reservadoPor = clienteId;
        this.fechaReserva = LocalDateTime.now();
        this.fechaExpiracionReserva = LocalDateTime.now().plusMinutes(10);
    }

    // Confirmar reserva (al pagar)
    public void confirmar() {
        this.estado = EstadoAsiento.OCUPADO;
        this.fechaExpiracionReserva = null;
    }

    // Liberar asiento
    public void liberar() {
        this.estado = EstadoAsiento.DISPONIBLE;
        this.reservadoPor = null;
        this.fechaReserva = null;
        this.fechaExpiracionReserva = null;
    }
}


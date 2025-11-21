package com.utp.cinerama.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "usuario")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ⭐ NUEVO: Relación con Usuario para autenticación
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String telefono;

    @Column(name = "numero_documento", nullable = false, unique = true)
    private String numeroDocumento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false)
    private TipoDocumento tipoDocumento;

    // ⭐ NUEVO: Sistema de fidelización
    @Column(name = "puntos_acumulados")
    @Builder.Default
    private Integer puntosAcumulados = 0;

    @Column(name = "nivel_fidelizacion", length = 20)
    @Builder.Default
    private String nivelFidelizacion = "BRONCE"; // BRONCE, PLATA, ORO, PLATINO

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;

    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
    }

    // Enum para tipo de documento
    public enum TipoDocumento {
        DNI("DNI"),
        PASAPORTE("Pasaporte"),
        CARNET_EXTRANJERIA("Carnet de Extranjería");

        private final String descripcion;

        TipoDocumento(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }
}

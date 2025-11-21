package com.utp.cinerama.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad Rol para sistema de autenticacion
 * Ejemplos: ROLE_ADMIN, ROLE_CLIENTE
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "usuarios")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre; // ROLE_ADMIN, ROLE_CLIENTE, etc.

    @Column(length = 255)
    private String descripcion;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    // Relacion con Usuarios (muchos a muchos - lado inverso)
    @ManyToMany(mappedBy = "roles")
    @Builder.Default
    private Set<Usuario> usuarios = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}


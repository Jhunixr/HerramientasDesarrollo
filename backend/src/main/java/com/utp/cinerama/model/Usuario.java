package com.utp.cinerama.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "usuarios",
    indexes = {
        @Index(name = "idx_username", columnList = "username"),
        @Index(name = "idx_email", columnList = "email")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"password", "roles"})
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password; 

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @Column(name = "cuenta_no_expirada")
    @Builder.Default
    private Boolean cuentaNoExpirada = true;

    @Column(name = "cuenta_no_bloqueada")
    @Builder.Default
    private Boolean cuentaNoBloqueada = true;

    @Column(name = "credenciales_no_expiradas")
    @Builder.Default
    private Boolean credencialesNoExpiradas = true;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;

    // Relación con Cliente (uno a uno)
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cliente cliente;

    // Relación con Roles (muchos a muchos)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    @Builder.Default
    private Set<Rol> roles = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    // ========== Implementacion de UserDetails ==========

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convertir roles a GrantedAuthority
        return roles.stream()
            .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
            .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return cuentaNoExpirada;
    }

    @Override
    public boolean isAccountNonLocked() {
        return cuentaNoBloqueada;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credencialesNoExpiradas;
    }

    @Override
    public boolean isEnabled() {
        return activo;
    }

    
    public void agregarRol(Rol rol) {
        this.roles.add(rol);
        rol.getUsuarios().add(this);
    }

    public void removerRol(Rol rol) {
        this.roles.remove(rol);
        rol.getUsuarios().remove(this);
    }

    /**
     * Verificar si tiene un rol especifico
     */
    public boolean tieneRol(String nombreRol) {
        return roles.stream()
            .anyMatch(rol -> rol.getNombre().equals(nombreRol));
    }


    public Set<String> getNombresRoles() {
        return roles.stream()
            .map(Rol::getNombre)
            .collect(Collectors.toSet());
    }
}


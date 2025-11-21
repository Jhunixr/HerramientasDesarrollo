package com.utp.cinerama.security;

import com.utp.cinerama.model.Rol;
import com.utp.cinerama.model.Usuario;
import com.utp.cinerama.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Servicio para cargar detalles del usuario durante la autenticacion
 * Implementa UserDetailsService de Spring Security
 * 
 * PATRON SIMPLIFICADO: Busca por username o email y retorna User de Spring Security
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Cargar usuario por username o email
     * Spring Security llama este metodo automaticamente durante la autenticacion
     * 
     * FLUJO:
     * 1. Buscar usuario en BD por username o email
     * 2. Verificar que este activo
     * 3. Convertir roles a GrantedAuthority
     * 4. Retornar UserDetails (User de Spring Security)
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        log.info("Buscando usuario: {}", usernameOrEmail);

        // Buscar por username o email
        Usuario usuario = usuarioRepository.findByUsername(usernameOrEmail)
                .or(() -> usuarioRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado: {}", usernameOrEmail);
                    return new UsernameNotFoundException(
                        "Usuario no encontrado con username o email: " + usernameOrEmail
                    );
                });

        // Verificar si el usuario esta activo
        if (!usuario.getActivo()) {
            log.error("Usuario inactivo: {}", usernameOrEmail);
            throw new UsernameNotFoundException("Usuario inactivo: " + usernameOrEmail);
        }

        log.info("Usuario encontrado: {} con {} roles", 
            usuario.getUsername(), 
            usuario.getRoles().size()
        );

        // Convertir roles a GrantedAuthority
        Collection<GrantedAuthority> authorities = getAuthorities(usuario.getRoles());

        // Retornar User de Spring Security
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(!usuario.getActivo())
                .credentialsExpired(false)
                .disabled(!usuario.getActivo())
                .build();
    }

    /**
     * Convertir roles a GrantedAuthority
     * 
     * FORMATO:
     * - Roles: ROLE_ADMIN, ROLE_CLIENTE
     * 
     * Ejemplo: Usuario con ROLE_CLIENTE tendra: [ROLE_CLIENTE]
     */
    private Collection<GrantedAuthority> getAuthorities(Set<Rol> roles) {
        return roles.stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
                .collect(Collectors.toSet());
    }

    
    @Transactional(readOnly = true)
    public Usuario obtenerUsuarioCompleto(String usernameOrEmail) {
        return usuarioRepository.findByUsername(usernameOrEmail)
                .or(() -> usuarioRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException(
                    "Usuario no encontrado: " + usernameOrEmail
                ));
    }

    
    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con id: " + id
                ));

        // Verificar que este activo
        if (!usuario.getActivo()) {
            throw new UsernameNotFoundException("Usuario inactivo con id: " + id);
        }

        Collection<GrantedAuthority> authorities = getAuthorities(usuario.getRoles());

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(!usuario.getActivo())
                .credentialsExpired(false)
                .disabled(!usuario.getActivo())
                .build();
    }
}


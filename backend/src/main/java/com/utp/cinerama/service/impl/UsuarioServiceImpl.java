package com.utp.cinerama.service.impl;

import com.utp.cinerama.dto.LoginDTO;
import com.utp.cinerama.dto.LoginResponseDTO;
import com.utp.cinerama.dto.RegistroDTO;
import com.utp.cinerama.model.Cliente;
import com.utp.cinerama.model.Rol;
import com.utp.cinerama.model.Usuario;
import com.utp.cinerama.repository.ClienteRepository;
import com.utp.cinerama.repository.RolRepository;
import com.utp.cinerama.repository.UsuarioRepository;
import com.utp.cinerama.util.JwtUtil;
import com.utp.cinerama.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public Usuario registrar(RegistroDTO registroDTO) {
        log.info("Registrando nuevo usuario: {}", registroDTO.getUsername());

        // 1. Validar que el username no exista
        if (usuarioRepository.existsByUsername(registroDTO.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El username '" + registroDTO.getUsername() + "' ya está en uso");
        }

        // 2. Validar que el email no exista
        if (usuarioRepository.existsByEmail(registroDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email '" + registroDTO.getEmail() + "' ya está registrado");
        }

        // 3. Crear usuario
        Usuario usuario = Usuario.builder()
                .username(registroDTO.getUsername())
                .email(registroDTO.getEmail())
                .password(passwordEncoder.encode(registroDTO.getPassword()))
                .activo(true)
                .cuentaNoExpirada(true)
                .cuentaNoBloqueada(true)
                .credencialesNoExpiradas(true)
                .build();

        // 4. Asignar rol CLIENTE por defecto
    Rol rolCliente = rolRepository.findByNombre("ROLE_CLIENTE")
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol ROLE_CLIENTE no encontrado"));
        
        usuario.agregarRol(rolCliente);

        // 5. Validar datos del cliente ANTES de guardar el usuario
        // Validar que el email no esté en uso en Cliente
        if (clienteRepository.existsByEmailIgnoreCase(registroDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email '" + registroDTO.getEmail() + "' ya está registrado en otro cliente");
        }

        // Validar que el teléfono no esté en uso
        if (clienteRepository.existsByTelefono(registroDTO.getTelefono())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El teléfono '" + registroDTO.getTelefono() + "' ya está registrado");
        }

        // Validar que el número de documento no esté en uso
        if (clienteRepository.existsByNumeroDocumento(registroDTO.getNumeroDocumento())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El número de documento '" + registroDTO.getNumeroDocumento() + "' ya está registrado");
        }

        // Validar y convertir tipoDocumento
        Cliente.TipoDocumento tipoDocumento;
        try {
            tipoDocumento = Cliente.TipoDocumento.valueOf(registroDTO.getTipoDocumento());
        } catch (IllegalArgumentException e) {
            log.error("Tipo de documento inválido: {}", registroDTO.getTipoDocumento());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Tipo de documento inválido. Debe ser: DNI, PASAPORTE o CARNET_EXTRANJERIA");
        }

        // 6. Guardar usuario
        usuario = usuarioRepository.save(usuario);
        log.info("Usuario creado con ID: {}", usuario.getId());

        // 10. Crear cliente asociado
        Cliente cliente = Cliente.builder()
                .usuario(usuario)
                .nombre(registroDTO.getNombre())
                .apellido(registroDTO.getApellido())
                .email(registroDTO.getEmail())
                .telefono(registroDTO.getTelefono())
                .numeroDocumento(registroDTO.getNumeroDocumento())
                .tipoDocumento(tipoDocumento)
                .puntosAcumulados(0)
                .nivelFidelizacion("BRONCE")
                .build();

        clienteRepository.save(cliente);
        log.info("Cliente asociado creado con ID: {}", cliente.getId());

        return usuario;
    }

    @Override
    @Transactional
    public LoginResponseDTO login(LoginDTO loginDTO) {
        log.info("Intento de login: {}", loginDTO.getUsername());

        try {
            // 1. Autenticar con Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );

            // 2. Establecer autenticación en el contexto
            SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generar JWT token (versión simple) por compatibilidad si este método es usado
        List<String> roles = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .filter(auth -> auth.startsWith("ROLE_"))
            .collect(Collectors.toList());
        String rol = roles.isEmpty() ? "" : roles.get(0);
        String jwt = jwtUtil.generateToken(loginDTO.getUsername(), rol);

            // 4. Obtener detalles del usuario
        Usuario usuario = usuarioRepository.findByUsername(loginDTO.getUsername())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

            // 5. Actualizar último acceso
            usuario.setUltimoAcceso(LocalDateTime.now());
            usuarioRepository.save(usuario);

        // 6. Obtener roles (ya calculados arriba)

            // 7. Construir respuesta
            LoginResponseDTO response = LoginResponseDTO.builder()
                    .token(jwt)
                    .tipo("Bearer")
                    .username(usuario.getUsername())
                    .email(usuario.getEmail())
                    .roles(roles)
                    .build();

            // 8. Agregar información del cliente si existe
            if (usuario.getCliente() != null) {
                Cliente cliente = usuario.getCliente();
                response.setClienteId(cliente.getId());
                response.setNombreCompleto(cliente.getNombre() + " " + cliente.getApellido());
            }

            log.info("Login exitoso para: {}", usuario.getUsername());
            return response;

        } catch (Exception e) {
            log.error("Error en login: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Boolean existePorUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    @Override
    public Boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void asignarRol(Long usuarioId, String nombreRol) {
        log.info("Asignando rol {} al usuario {}", nombreRol, usuarioId);

        Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Rol rol = rolRepository.findByNombre(nombreRol)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado: " + nombreRol));

        usuario.agregarRol(rol);
        usuarioRepository.save(usuario);

        log.info("Rol asignado exitosamente");
    }

    @Override
    @Transactional
    public void removerRol(Long usuarioId, String nombreRol) {
        log.info("Removiendo rol {} del usuario {}", nombreRol, usuarioId);

        Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Rol rol = rolRepository.findByNombre(nombreRol)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado: " + nombreRol));

        usuario.removerRol(rol);
        usuarioRepository.save(usuario);

        log.info("Rol removido exitosamente");
    }

    @Override
    @Transactional
    public void cambiarPassword(Long usuarioId, String passwordActual, String passwordNueva) {
        log.info("Cambiando contrasena para usuario {}", usuarioId);

        Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Validar contraseña actual
        if (!passwordEncoder.matches(passwordActual, usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña actual es incorrecta");
        }

        // Actualizar contraseña
        usuario.setPassword(passwordEncoder.encode(passwordNueva));
        usuarioRepository.save(usuario);

        log.info("Contrasena actualizada exitosamente");
    }

    @Override
    @Transactional
    public void cambiarEstado(Long usuarioId, Boolean activo) {
        log.info("Cambiando estado de usuario {} a {}", usuarioId, activo ? "activo" : "inactivo");

        Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        usuario.setActivo(activo);
        usuarioRepository.save(usuario);

        log.info("Estado actualizado exitosamente");
    }

    @Override
    @Transactional
    public void actualizarUltimoAcceso(String username) {
        usuarioRepository.findByUsername(username).ifPresent(usuario -> {
            usuario.setUltimoAcceso(LocalDateTime.now());
            usuarioRepository.save(usuario);
        });
    }
}


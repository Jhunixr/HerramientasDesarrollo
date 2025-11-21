package com.utp.cinerama.service;

import com.utp.cinerama.dto.LoginDTO;
import com.utp.cinerama.dto.LoginResponseDTO;
import com.utp.cinerama.dto.RegistroDTO;
import com.utp.cinerama.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    /**
     * Registrar un nuevo usuario con rol CLIENTE
     */
    Usuario registrar(RegistroDTO registroDTO);

    /**
     * Autenticar usuario y generar token JWT
     */
    LoginResponseDTO login(LoginDTO loginDTO);

    /**
     * Obtener usuario por username
     */
    Optional<Usuario> obtenerPorUsername(String username);

    /**
     * Obtener usuario por email
     */
    Optional<Usuario> obtenerPorEmail(String email);

    /**
     * Obtener usuario por ID
     */
    Optional<Usuario> obtenerPorId(Long id);

    /**
     * Listar todos los usuarios
     */
    List<Usuario> listarTodos();

    /**
     * Verificar si existe un username
     */
    Boolean existePorUsername(String username);

    /**
     * Verificar si existe un email
     */
    Boolean existePorEmail(String email);

    /**
     * Asignar un rol a un usuario
     */
    void asignarRol(Long usuarioId, String nombreRol);

    /**
     * Remover un rol de un usuario
     */
    void removerRol(Long usuarioId, String nombreRol);

    /**
     * Cambiar contraseña
     */
    void cambiarPassword(Long usuarioId, String passwordActual, String passwordNueva);

    /**
     * Activar/desactivar usuario
     */
    void cambiarEstado(Long usuarioId, Boolean activo);

    /**
     * Actualizar último acceso
     */
    void actualizarUltimoAcceso(String username);
}


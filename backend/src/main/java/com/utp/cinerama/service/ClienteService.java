package com.utp.cinerama.service;

import com.utp.cinerama.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {
    List<Cliente> obtenerTodosLosClientes();
    Optional<Cliente> obtenerClientePorId(Long id);
    Cliente crearCliente(Cliente cliente);
    Cliente actualizarCliente(Long id, Cliente cliente);
    void eliminarCliente(Long id);
}

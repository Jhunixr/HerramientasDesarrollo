package com.utp.cinerama.service.impl;

import com.utp.cinerama.model.Cliente;
import com.utp.cinerama.repository.ClienteRepository;
import com.utp.cinerama.service.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    @Override
    public Cliente crearCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente actualizarCliente(Long id, Cliente cliente) {
        return clienteRepository.findById(id)
                .map(c -> {
                    c.setNombre(cliente.getNombre());
                    c.setApellido(cliente.getApellido());
                    c.setEmail(cliente.getEmail());
                    c.setTelefono(cliente.getTelefono());
                    c.setNumeroDocumento(cliente.getNumeroDocumento());
                    c.setTipoDocumento(cliente.getTipoDocumento());
                    return clienteRepository.save(c);
                })
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    @Override
    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }
}

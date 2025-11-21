package com.utp.cinerama.service;

import com.utp.cinerama.dto.CalcularTotalRequestDTO;
import com.utp.cinerama.dto.CalcularTotalResponseDTO;
import com.utp.cinerama.dto.ConfirmarCompraRequestDTO;
import com.utp.cinerama.dto.ConfirmarCompraResponseDTO;

public interface CompraService {
    CalcularTotalResponseDTO calcularTotal(CalcularTotalRequestDTO request, Long clienteId);
    ConfirmarCompraResponseDTO confirmarCompra(ConfirmarCompraRequestDTO request, Long clienteId);
}



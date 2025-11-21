package com.utp.cinerama.service;

import com.utp.cinerama.model.Asiento;
import com.utp.cinerama.model.Asiento.EstadoAsiento;
import com.utp.cinerama.model.Asiento.TipoAsiento;

import java.util.List;

public interface AsientoService {

    /**
     * 🗺️ Obtiene mapa completo de asientos para una función
     */
    List<Asiento> obtenerMapaAsientos(Long funcionId);

    /**
     * 🔒 Reserva temporalmente un asiento (5 minutos)
     * @throws IllegalStateException si el asiento no está disponible
     */
    Asiento reservarAsiento(Long asientoId);

    /**
     * ✅ Confirma la reserva y marca como OCUPADO
     * @throws IllegalStateException si el asiento no está en SELECCIONADO
     */
    Asiento confirmarReserva(Long asientoId);

    /**
     * 🔓 Libera un asiento reservado (vuelve a DISPONIBLE)
     */
    Asiento liberarAsiento(Long asientoId);

    /**
     * 🏗️ Genera asientos para una función basándose en la capacidad de la sala
     * @param funcionId ID de la función
     * @return Lista de asientos creados
     */
    List<Asiento> generarAsientosParaFuncion(Long funcionId);

    /**
     * ✔️ Verifica si un asiento específico está disponible
     */
    boolean verificarDisponibilidad(Long funcionId, String fila, Integer numero);

    /**
     * 📊 Obtiene asientos por estado
     */
    List<Asiento> obtenerAsientosPorEstado(Long funcionId, EstadoAsiento estado);

    /**
     * 🎭 Obtiene asientos por tipo (VIP, NORMAL, etc.)
     */
    List<Asiento> obtenerAsientosPorTipo(Long funcionId, TipoAsiento tipo);

    /**
     * 📈 Obtiene estadísticas de ocupación de una función
     */
    EstatisticasAsientos obtenerEstadisticas(Long funcionId);

    /**
     * 🧹 Libera asientos expirados (llamado por scheduler)
     */
    int liberarAsientosExpirados();

    /**
     * DTO para estadísticas
     */
    record EstatisticasAsientos(
            long total,
            long disponibles,
            long reservados,
            long ocupados,
            long bloqueados,
            double porcentajeOcupacion
    ) {}
}


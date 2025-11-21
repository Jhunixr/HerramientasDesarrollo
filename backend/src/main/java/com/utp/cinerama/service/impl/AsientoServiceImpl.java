package com.utp.cinerama.service.impl;

import com.utp.cinerama.model.Asiento;
import com.utp.cinerama.model.Asiento.EstadoAsiento;
import com.utp.cinerama.model.Asiento.TipoAsiento;
import com.utp.cinerama.model.Funcion;
import com.utp.cinerama.model.Sala;
import com.utp.cinerama.repository.AsientoRepository;
import com.utp.cinerama.repository.FuncionRepository;
import com.utp.cinerama.service.AsientoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsientoServiceImpl implements AsientoService {

    private final AsientoRepository asientoRepository;
    private final FuncionRepository funcionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Asiento> obtenerMapaAsientos(Long funcionId) {
        log.info("Obteniendo mapa de asientos para función: {}", funcionId);
        return asientoRepository.findByFuncionIdOrderByFilaAscNumeroAsc(funcionId);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Asiento reservarAsiento(Long asientoId) {
        log.info("Reservando asiento: {}", asientoId);

        // 🔒 Obtener asiento con bloqueo pesimista
        Asiento asiento = asientoRepository.findByIdWithLock(asientoId)
                .orElseThrow(() -> new IllegalArgumentException("Asiento no encontrado: " + asientoId));

        // ✅ Verificar que esté disponible
        if (!asiento.estaDisponible()) {
            throw new IllegalStateException("El asiento " + asiento.getCodigoAsiento() + 
                    " no está disponible. Estado actual: " + asiento.getEstado());
        }

        // 🕐 Usar método helper para reservar temporalmente (5 minutos)
        asiento.reservarTemporal(null); // null = sin cliente específico por ahora

        return asientoRepository.save(asiento);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Asiento confirmarReserva(Long asientoId) {
        log.info("Confirmando reserva de asiento: {}", asientoId);

        Asiento asiento = asientoRepository.findByIdWithLock(asientoId)
                .orElseThrow(() -> new IllegalArgumentException("Asiento no encontrado: " + asientoId));

        // ✅ Solo se puede confirmar si está RESERVADO
        if (asiento.getEstado() != EstadoAsiento.RESERVADO) {
            throw new IllegalStateException("El asiento no está reservado. Estado actual: " + asiento.getEstado());
        }

        // 🕐 Verificar que no haya expirado usando el método helper
        if (asiento.reservaExpirada()) {
            // Expiró, liberar automáticamente
            asiento.liberar();
            asientoRepository.save(asiento);
            throw new IllegalStateException("La reserva expiró. Por favor, vuelva a seleccionar el asiento.");
        }

        // ✅ Confirmar reserva → OCUPADO usando método helper
        asiento.confirmar();
        return asientoRepository.save(asiento);
    }

    @Override
    @Transactional
    public Asiento liberarAsiento(Long asientoId) {
        log.info("Liberando asiento: {}", asientoId);

        Asiento asiento = asientoRepository.findById(asientoId)
                .orElseThrow(() -> new IllegalArgumentException("Asiento no encontrado: " + asientoId));

        // 🔓 Solo se pueden liberar asientos RESERVADOS
        if (asiento.getEstado() != EstadoAsiento.RESERVADO) {
            throw new IllegalStateException("Solo se pueden liberar asientos reservados");
        }

        // Usar método helper para liberar
        asiento.liberar();
        return asientoRepository.save(asiento);
    }

    @Override
    @Transactional
    public List<Asiento> generarAsientosParaFuncion(Long funcionId) {
        log.info("Generando asientos para función: {}", funcionId);

        Funcion funcion = funcionRepository.findById(funcionId)
                .orElseThrow(() -> new IllegalArgumentException("Función no encontrada: " + funcionId));

        Sala sala = funcion.getSala();
        int capacidad = sala.getCapacidad();

        // 🏗️ Calcular distribución de asientos
    int asientosPorFila = 10; // Estándar
        int totalFilas = (int) Math.ceil((double) capacidad / asientosPorFila);

        List<Asiento> asientos = new ArrayList<>();

        for (int f = 0; f < totalFilas; f++) {
            String fila = String.valueOf((char) ('A' + f)); // A, B, C, ...

            for (int n = 1; n <= asientosPorFila && asientos.size() < capacidad; n++) {
                // 🎭 En Cinerama Chimbote solo hay asientos NORMAL
                TipoAsiento tipo = TipoAsiento.NORMAL;

                // 💰 Precio plano para asientos NORMAL
                Double precio = calcularPrecioAsiento(tipo);

                Asiento asiento = Asiento.builder()
                        .funcion(funcion)
                        .fila(fila)
                        .numero(n)
                        .tipo(tipo)
                        .estado(EstadoAsiento.DISPONIBLE)
                        .precio(precio)
                        .build();

                asientos.add(asiento);
            }
        }

        return asientoRepository.saveAll(asientos);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verificarDisponibilidad(Long funcionId, String fila, Integer numero) {
        return asientoRepository.existsByFuncionIdAndFilaAndNumero(funcionId, fila, numero);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asiento> obtenerAsientosPorEstado(Long funcionId, EstadoAsiento estado) {
        return asientoRepository.findByFuncionIdAndEstado(funcionId, estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asiento> obtenerAsientosPorTipo(Long funcionId, TipoAsiento tipo) {
        return asientoRepository.findByFuncionIdAndTipo(funcionId, tipo);
    }

    @Override
    @Transactional(readOnly = true)
    public EstatisticasAsientos obtenerEstadisticas(Long funcionId) {
        long total = asientoRepository.countByFuncionIdAndEstado(funcionId, EstadoAsiento.DISPONIBLE)
                + asientoRepository.countByFuncionIdAndEstado(funcionId, EstadoAsiento.RESERVADO)
                + asientoRepository.countByFuncionIdAndEstado(funcionId, EstadoAsiento.OCUPADO)
                + asientoRepository.countByFuncionIdAndEstado(funcionId, EstadoAsiento.BLOQUEADO);

        long disponibles = asientoRepository.countByFuncionIdAndEstado(funcionId, EstadoAsiento.DISPONIBLE);
        long reservados = asientoRepository.countByFuncionIdAndEstado(funcionId, EstadoAsiento.RESERVADO);
        long ocupados = asientoRepository.countByFuncionIdAndEstado(funcionId, EstadoAsiento.OCUPADO);
        long bloqueados = asientoRepository.countByFuncionIdAndEstado(funcionId, EstadoAsiento.BLOQUEADO);

        double porcentajeOcupacion = total > 0 ? ((double) ocupados / total) * 100 : 0;

        return new EstatisticasAsientos(total, disponibles, reservados, ocupados, bloqueados, porcentajeOcupacion);
    }

    @Override
    @Transactional
    public int liberarAsientosExpirados() {
        LocalDateTime ahora = LocalDateTime.now();
        int liberados = asientoRepository.liberarAsientosExpirados(ahora);
        
        if (liberados > 0) {
            log.info("✅ Liberados {} asientos expirados", liberados);
        }
        
        return liberados;
    }

    // 🎭 Métodos auxiliares

    /**
     * Calcula el precio según el tipo de asiento
     */
    private Double calcularPrecioAsiento(TipoAsiento tipo) {
        // Precio único para asientos NORMAL
        return 15.0;
    }
}


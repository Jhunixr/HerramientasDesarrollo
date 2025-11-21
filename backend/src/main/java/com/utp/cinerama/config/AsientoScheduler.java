package com.utp.cinerama.config;

import com.utp.cinerama.service.AsientoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ⏰ Scheduler para liberar asientos reservados que expiraron
 * Se ejecuta cada minuto para liberar asientos en estado RESERVADO
 * que tienen más de 10 minutos desde su reserva
 */
@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class AsientoScheduler {

    private final AsientoService asientoService;

    /**
     * 🧹 Tarea programada: Libera asientos expirados cada 1 minuto
     * Cron expression: segundos minutos horas dia mes dia-semana
     * "0 * * * * *" = cada minuto exacto (segundo 0)
     */
    @Scheduled(cron = "0 * * * * *")
    public void liberarAsientosExpirados() {
        try {
            int liberados = asientoService.liberarAsientosExpirados();
            
            if (liberados > 0) {
                log.info("🧹 [SCHEDULER] Liberados {} asientos expirados", liberados);
            }
        } catch (Exception e) {
            log.error("❌ [SCHEDULER] Error al liberar asientos expirados: {}", e.getMessage());
        }
    }
}


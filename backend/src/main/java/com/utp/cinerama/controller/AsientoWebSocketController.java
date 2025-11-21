package com.utp.cinerama.controller;

import com.utp.cinerama.dto.AsientoUpdateDTO;
import com.utp.cinerama.model.Asiento;
import com.utp.cinerama.service.AsientoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AsientoWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final AsientoService asientoService;

    @MessageMapping("/asientos/reservar")
    public void reservarAsiento(@Payload AsientoUpdateDTO updateDTO) {
        log.info("Reservando asiento {} para función {}", updateDTO.getAsientoId(), updateDTO.getFuncionId());
        try {
            if (updateDTO.getAsientoId() != null && updateDTO.getFuncionId() != null) {
                asientoService.reservarAsiento(updateDTO.getAsientoId());
                updateDTO.setEstado(Asiento.EstadoAsiento.RESERVADO);
                updateDTO.setAccion("reservar");
                
                // Notificar a todos los clientes sobre el cambio
                messagingTemplate.convertAndSend(
                    "/topic/asientos/funcion/" + updateDTO.getFuncionId(),
                    updateDTO
                );
            }
        } catch (Exception e) {
            log.error("Error al reservar asiento: {}", e.getMessage(), e);
        }
    }

    @MessageMapping("/asientos/liberar")
    public void liberarAsiento(@Payload AsientoUpdateDTO updateDTO) {
        log.info("Liberando asiento {} de función {}", updateDTO.getAsientoId(), updateDTO.getFuncionId());
        try {
            if (updateDTO.getAsientoId() != null && updateDTO.getFuncionId() != null) {
                asientoService.liberarAsiento(updateDTO.getAsientoId());
                updateDTO.setEstado(Asiento.EstadoAsiento.DISPONIBLE);
                updateDTO.setAccion("liberar");
                
                // Notificar a todos los clientes sobre el cambio
                messagingTemplate.convertAndSend(
                    "/topic/asientos/funcion/" + updateDTO.getFuncionId(),
                    updateDTO
                );
            }
        } catch (Exception e) {
            log.error("Error al liberar asiento: {}", e.getMessage(), e);
        }
    }

    @MessageMapping("/asientos/confirmar")
    public void confirmarAsiento(@Payload AsientoUpdateDTO updateDTO) {
        log.info("Confirmando asiento {} de función {}", updateDTO.getAsientoId(), updateDTO.getFuncionId());
        try {
            if (updateDTO.getAsientoId() != null && updateDTO.getFuncionId() != null) {
                asientoService.confirmarReserva(updateDTO.getAsientoId());
                updateDTO.setEstado(Asiento.EstadoAsiento.OCUPADO);
                updateDTO.setAccion("confirmar");
                
                // Notificar a todos los clientes sobre el cambio
                messagingTemplate.convertAndSend(
                    "/topic/asientos/funcion/" + updateDTO.getFuncionId(),
                    updateDTO
                );
            }
        } catch (Exception e) {
            log.error("Error al confirmar asiento: {}", e.getMessage(), e);
        }
    }
}


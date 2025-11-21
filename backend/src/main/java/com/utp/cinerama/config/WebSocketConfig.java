package com.utp.cinerama.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Habilita un broker simple en memoria para enviar mensajes a clientes
        config.enableSimpleBroker("/topic", "/queue");
        // Prefijo para mensajes destinados a métodos anotados con @MessageMapping
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registra el endpoint WebSocket
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // Permite conexiones desde cualquier origen
                .withSockJS(); // Habilita SockJS para compatibilidad con navegadores antiguos
    }
}






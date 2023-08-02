package com.example.honjarang.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${spring.rabbitmq.stomp.host}")
    private String rabbitMqHost;

    @Value("${spring.rabbitmq.stomp.port}")
    private int rabbitMqPort;

    @Value("${spring.rabbitmq.stomp.username}")
    private String stompLogin;

    @Value("${spring.rabbitmq.stomp.passcode}")
    private String stompPasscode;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableStompBrokerRelay("/topic")
                .setRelayHost(rabbitMqHost)
                .setRelayPort(rabbitMqPort)
                .setClientLogin(stompLogin)
                .setClientPasscode(stompPasscode);
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").setAllowedOrigins("http://localhost:63342").withSockJS(); // WebSocket 연결을 위한 엔드포인트
    }
}
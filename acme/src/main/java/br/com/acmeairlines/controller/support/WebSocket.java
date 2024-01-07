package br.com.acmeairlines.controller.support;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocket implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/ticket");
        config.setApplicationDestinationPrefixes("/support");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry config) {
        config.addEndpoint("connect");
        config.addEndpoint("connect").withSockJS();
    }

}

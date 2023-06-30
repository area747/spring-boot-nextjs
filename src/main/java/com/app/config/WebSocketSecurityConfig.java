package com.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry message) {
        message
                .nullDestMatcher().permitAll()
                // .anyMessage().permitAll()
                .simpDestMatchers("/publish/**").permitAll()
                .simpSubscribeDestMatchers("/subscribe/**").permitAll()
                // .anyMessage().denyAll()
                ;
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
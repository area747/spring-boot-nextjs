package com.app.handler;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketConnectHandler {

    @EventListener
    public void handleWebSocketConnect(SessionConnectEvent event) {
    }
}

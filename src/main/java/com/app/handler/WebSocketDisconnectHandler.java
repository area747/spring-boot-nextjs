package com.app.handler;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketDisconnectHandler {

    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
    }
}

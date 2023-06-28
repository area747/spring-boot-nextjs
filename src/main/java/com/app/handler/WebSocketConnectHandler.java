package com.app.handler;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import com.app.dto.WebSocketUserDTO;
import com.app.service.WebSocketService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketConnectHandler {

    private final WebSocketService service;

    @EventListener
    public void handleWebSocketDisconnect(SessionConnectEvent event) {
        WebSocketUserDTO socketUser = service.makeWebSocketUserFromSocketEvent(event);
        service.addSocketUser(socketUser);
    }
}

package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;

import com.app.component.WebSocketUserMap;
import com.app.dto.WebSocketUserDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketService {

    private final WebSocketUserMap webSocketUserMap;

    public WebSocketUserDTO makeWebSocketUserFromSocketEvent(AbstractSubProtocolEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String ip = getIpAddress(accessor);
        String sessionId = accessor.getSessionId();
        WebSocketUserDTO webSocketUser = WebSocketUserDTO.builder().ip(ip).sessionId(sessionId).build();

        return webSocketUser;
    }

    public void addSocketUser(WebSocketUserDTO webSocketUser) {
        webSocketUserMap.addUser(webSocketUser);
    }

    public void removeSocketUser(WebSocketUserDTO webSocketUser) {
        webSocketUserMap.removeUser(webSocketUser);
    }

    private String getIpAddress(SimpMessageHeaderAccessor accessor) {
        List<String> nativeIpHeaders = accessor.getNativeHeader("X-FORWARDED-FOR");
        if (nativeIpHeaders != null && !nativeIpHeaders.isEmpty()) {
            return nativeIpHeaders.get(0);
        } else {
            return accessor.getSessionAttributes().get("ip").toString();
        }
    }
}

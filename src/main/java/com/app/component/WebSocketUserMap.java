package com.app.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.app.dto.ChatMessage;
import com.app.dto.WebSocketUserDTO;
import com.app.service.WebSocketService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketUserMap {
    private Map<String, WebSocketUserDTO> webSocketUserMap = new HashMap<String, WebSocketUserDTO>();
    private Map<String, List<String>> webSocketUserSessionListMap = new HashMap<String, List<String>>();
    private final SimpMessagingTemplate template;

    public void addUser(WebSocketUserDTO user) {
        if (webSocketUserMap.containsKey(user.getIp())) {
            webSocketUserSessionListMap.get(user.getIp()).add(user.getSessionId());
        } else {
            webSocketUserMap.put(user.getIp(), user);
            webSocketUserSessionListMap.put(user.getIp(), new ArrayList<String>());
        }
    }

    public void removeUser(WebSocketUserDTO user) {
        if (webSocketUserMap.containsKey(user.getIp())) {
            webSocketUserSessionListMap.remove(user.getSessionId());
            if (webSocketUserSessionListMap.get(user.getIp()).size() == 0) {
                webSocketUserMap.remove(user.getIp());
            }
        }
    }
}
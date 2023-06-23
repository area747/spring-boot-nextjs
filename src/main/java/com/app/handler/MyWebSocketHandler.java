package com.app.handler;

import java.io.IOException;
import java.util.*;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyWebSocketHandler extends TextWebSocketHandler {

    private static final Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        String userId = extractUserIdFromSession(session);
        session.getAttributes().put("userId", userId);
        String message = "User " + userId + " connected.";
        broadcast(message);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 메시지 처리 로직 구현
        String userId = (String) session.getAttributes().get("userId");
        String payload = (String) message.getPayload();
        String formattedMessage = "[" + userId + "]: " + payload;
        broadcast(formattedMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        String userId = (String) session.getAttributes().get("userId");
        String message = "User " + userId + " disconnected.";
        broadcast(message);
    }

    private void broadcast(String message) {
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String extractUserIdFromSession(WebSocketSession session) {
        return session.getId();
    }
}

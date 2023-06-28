package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.ChatMessage;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate template;

    @MessageMapping("/chat/join")
    public void join(@Payload ChatMessage message) {
        template.convertAndSend("/subscribe/chat/room/" + message.getChatRoomId(), message);
    }

    @MessageMapping("/chat/message")
    public void message(@Payload ChatMessage message) {
        System.out.println(message.toString());
        template.convertAndSend("/subscribe/chat/room/" + message.getChatRoomId(), message);
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

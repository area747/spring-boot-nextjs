package com.app.dto;

import com.app.enumList.MessageTypeEnum;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    private String chatRoomId;
    private String writer;
    private String message;
    private MessageTypeEnum type;
}
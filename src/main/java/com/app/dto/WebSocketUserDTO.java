package com.app.dto;

import java.util.List;

import com.app.enumList.WebSocketUserTypeEnum;

import lombok.*;

@Getter
@Setter
@Builder
public class WebSocketUserDTO {
    WebSocketUserTypeEnum userType;
    String ip;
    String userId;
    String sessionId;
}

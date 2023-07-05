package com.app.dto;

import java.security.Principal;
import java.util.List;

import com.app.enumList.WebSocketUserTypeEnum;

import lombok.*;

@Getter
@Setter
@Builder
public class WebSocketUserDTO {
    WebSocketUserTypeEnum userType;
    Principal principal;
    String ip;
    String userId;
    String sessionId;
}

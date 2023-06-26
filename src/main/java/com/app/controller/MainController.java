package com.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.UserDto;

@RestController
@RequestMapping("/api")
public class MainController {

    @GetMapping
    public String index() {
        System.out.println("Hello, Spring");
        return "Hello, Spring!";
    }

    @GetMapping("/user")
    public UserDto user() {
        UserDto userDto = UserDto.builder().id("123").password(null).build();
        return userDto;
    }
}

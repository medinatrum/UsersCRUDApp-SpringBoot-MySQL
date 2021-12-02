package com.example.demo.controller;

import com.example.demo.model.EntityResponseDto;
import com.example.demo.model.LoginRequestDto;
import com.example.demo.model.UserResponseDto;
import com.example.demo.service.UserService;
import com.example.demo.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private UserService service;

    @Autowired
    private JwtUtil jwt;

    @PostMapping("/api/login")
    public ResponseEntity<EntityResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) throws Exception {

        UserResponseDto userResponseDto = service.authenticateUser(loginRequestDto);

        return ResponseEntity.ok(EntityResponseDto.builder()
                .code(200)
                .message("Success")
                .data(jwt.createToken(userResponseDto.getEmail(), userResponseDto.getId()))
                .build());
    }

}

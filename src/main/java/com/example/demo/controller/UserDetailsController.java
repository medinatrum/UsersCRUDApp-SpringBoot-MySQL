package com.example.demo.controller;

import com.example.demo.exception.UnauthorizedException;
import com.example.demo.model.EntityResponseDto;
import com.example.demo.model.UserDetailsDto;
import com.example.demo.service.UserService;
import com.example.demo.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@RestController
public class UserDetailsController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/api/userDetails")
    public ResponseEntity<EntityResponseDto> addUserDetails(@RequestBody UserDetailsDto userDetailsDto, HttpServletRequest request) throws ConstraintViolationException {

        Integer userIdFromJwt = jwtUtil.getUserIdFromToken(request);
        Integer userIdFromRequest = userService.saveUserDetails(userDetailsDto, userIdFromJwt);
        if (!userIdFromJwt.equals(userIdFromRequest)) {
            throw new UnauthorizedException();
        }
        if(userIdFromRequest == 0) {
            return ResponseEntity.ok(EntityResponseDto.builder()
                    .code(200)
                    .message("Success")
                    .data("Invalid additional email address. Try again! " + userIdFromRequest)
                    .build());
        }

        return ResponseEntity.ok(EntityResponseDto.builder()
                .code(200)
                .message("Success")
                .data("User details added for user with id: " + userIdFromRequest)
                .build());
    }
}

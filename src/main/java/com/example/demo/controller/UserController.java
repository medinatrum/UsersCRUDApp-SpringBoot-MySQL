package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.model.EntityResponseDto;
import com.example.demo.model.UserRequestDto;
import com.example.demo.model.UserResponseDto;
import com.example.demo.service.UserService;
import com.example.demo.utility.JwtUtil;
import com.example.demo.utility.MapperUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/api/user/{id}")
    public ResponseEntity<?> findUserById(@PathVariable int id, HttpServletRequest request) throws ResourceNotFoundException, Exception {
        Integer userIdFromJwt = jwtUtil.getUserIdFromToken(request);
        if (!userIdFromJwt.equals(id)) {
            throw new UnauthorizedException();
        }

        return ResponseEntity.ok(EntityResponseDto.builder()
                .code(200)
                .message("Success")
                .data(service.getUserById(id))
                .build());
    }

    @PostMapping("/api/user")
    public ResponseEntity<EntityResponseDto> addUser(@RequestBody UserRequestDto userDto) throws ConstraintViolationException {
        UserResponseDto user = service.saveUser(userDto);
        User userForDetails = MapperUtility.map(user, User.class);

        return ResponseEntity.ok(EntityResponseDto.builder()
                .code(user == null ? 400 : 201)
                .message(user == null ? "Wrong security credentials! Try again." : "Created")
                .data(user)
                .build());
    }

    @DeleteMapping("/api/user/{id}")
    public ResponseEntity<EntityResponseDto> deleteUser(@PathVariable int id, HttpServletRequest request) throws ResourceNotFoundException {
        Integer useIdFromJwt = jwtUtil.getUserIdFromToken(request);
        if (!useIdFromJwt.equals(id)) {
            throw new UnauthorizedException();
        }

        service.deleteUserById(id);

        return ResponseEntity.ok(EntityResponseDto.builder()
                .code(200)
                .message("User deleted")
                .build());
    }
}

package com.example.demo.controller;

import com.example.demo.entity.Workspace;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.EntityResponseDto;
import com.example.demo.model.WorkspaceDto;
import com.example.demo.service.UserService;
import com.example.demo.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.Set;

@RestController
public class WorkspaceController {

    @Autowired
    UserService service;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/api/workspace")
    public ResponseEntity<?> addWorkspace(@RequestBody WorkspaceDto workspaceDto, HttpServletRequest request) throws ConstraintViolationException, ResourceNotFoundException {
        Integer userIdFromJwt = jwtUtil.getUserIdFromToken(request);

        if (service.validateNumberOfSeats(workspaceDto.getName(), workspaceDto.getSeats())) {
            service.saveWorkspace(workspaceDto, userIdFromJwt);

            return ResponseEntity.ok(EntityResponseDto.builder()
                    .code(200)
                    .message("Successfully added workspace for user with id " + userIdFromJwt)
                    .data(workspaceDto)
                    .build());
        } else {
            return ResponseEntity.ok(EntityResponseDto.builder()
                    .code(404)
                    .message("Not found seat for user on workspace with id: " + userIdFromJwt)
                    .data(workspaceDto)
                    .build());
        }
    }

    @GetMapping("/api/workspace")                 //by userid from jwt
    public ResponseEntity<?> findWorkspacesByUserId(HttpServletRequest request) throws ResourceNotFoundException, Exception {
        Integer userIdFromJwt = jwtUtil.getUserIdFromToken(request);
        Set<Workspace> workspaces = service.findAllWorkspacesByUserId(userIdFromJwt);

        return ResponseEntity.ok(EntityResponseDto.builder()
                .code(200)
                .message("Success")
                .data(workspaces)
                .build());
    }

    @GetMapping("/api/workspace/{id}")                //by workspace id
    public ResponseEntity<?> findWorkspaceByWorkspaceId(@PathVariable long id, HttpServletRequest request) throws ResourceNotFoundException, Exception {
        Optional<Workspace> workspace = service.findAllWorkspacesById(id);

        if (workspace != null) {
            return ResponseEntity.ok(EntityResponseDto.builder()
                    .code(200)
                    .message("Success")
                    .data(workspace)
                    .build());
        } else {
            return ResponseEntity.ok(EntityResponseDto.builder()
                    .code(404)
                    .message("Not found workspace with given user ID!")
                    .build());
        }
    }

    @DeleteMapping("/api/workspace/{id}")
    public ResponseEntity<?> deleteWorkspace(@PathVariable long id, HttpServletRequest request) throws ResourceNotFoundException {
        service.removeWorkspace(id);

        return ResponseEntity.ok(EntityResponseDto.builder()
                .code(200)
                .message("Deleted workspace with id: " + id)
                .build());
    }

    @PutMapping("/api/workspace")         //by workspace id
    public ResponseEntity<?> updateWorkspace(@RequestBody WorkspaceDto workspaceDto, HttpServletRequest request) throws ResourceNotFoundException, Exception {
        Integer userIdFromJwt = jwtUtil.getUserIdFromToken(request);

        return ResponseEntity.ok(EntityResponseDto.builder()
                .code(200)
                .message("Workspace updated!")
                .data(service.updateWorkspace(userIdFromJwt, workspaceDto))
                .build());
    }
}

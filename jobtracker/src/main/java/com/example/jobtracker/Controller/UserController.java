package com.example.jobtracker.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jobtracker.Dto.UserRequestDto;
import com.example.jobtracker.Dto.UserResponseDto;
import com.example.jobtracker.Service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;

    // @PreAuthorize("hasRole(")
    @PostMapping("/newuser")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto user){
        UserResponseDto userDto =userService.createUser(user);
        return ResponseEntity.status(201).body(userDto);
    }
}

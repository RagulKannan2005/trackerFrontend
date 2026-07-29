package com.example.jobtracker.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PatchMapping("/email/{id}")
    public ResponseEntity<UserResponseDto> updateemail(@PathVariable Long id, @RequestBody UserRequestDto user){
        UserResponseDto userDto=userService.updateemail(id, user);
        return ResponseEntity.ok(userDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PatchMapping("/password/{id}")
    public ResponseEntity<UserResponseDto> updatepassword(@PathVariable Long id, @RequestBody UserRequestDto user){
        UserResponseDto userDto=userService.updatePassword(id, user);
        return ResponseEntity.ok(userDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/allusers")
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        List<UserResponseDto> userDto=userService.getAllUsers();
        return ResponseEntity.ok(userDto);
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserResponseDto>> getByRole(@PathVariable String role){
        List<UserResponseDto> userDto=userService.findByRole(role);
        return ResponseEntity.ok(userDto);
    }
}

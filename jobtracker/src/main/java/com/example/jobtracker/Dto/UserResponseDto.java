package com.example.jobtracker.Dto;

import com.example.jobtracker.Enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    

    private Long id;
    private String username;
    private String email;
    private String password;
    private Role role;
    private Boolean active;
    
}

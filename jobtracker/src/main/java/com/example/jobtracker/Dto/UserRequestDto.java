package com.example.jobtracker.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {
    
    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "role is required admin or user ")
    private String role;

    @NotNull(message = "active is required true or false")
    private Boolean active;

}
